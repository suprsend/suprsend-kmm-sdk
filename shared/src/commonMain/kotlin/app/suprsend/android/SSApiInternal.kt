package app.suprsend.android

import app.suprsend.android.base.Logger
import app.suprsend.android.base.SSConstants
import app.suprsend.android.base.ioDispatcher
import app.suprsend.android.base.toKotlinJsonObject
import app.suprsend.android.base.uuid
import app.suprsend.android.config.ConfigHelper
import app.suprsend.android.database.DatabaseDriverFactory
import app.suprsend.android.database.SSDatabaseWrapper
import app.suprsend.android.event.EventFlushHandler
import app.suprsend.android.event.EventLocalDatasource
import app.suprsend.android.event.EventModel
import app.suprsend.android.event.PayloadCreator
import app.suprsend.android.network.httpClientEngine
import app.suprsend.android.sprop.SuperPropertiesLocalDataSource
import app.suprsend.android.user.UserEventLocalDataSource
import app.suprsend.android.user.UserLocalDatasource
import app.suprsend.android.user.api.UserApiInternalContract
import app.suprsend.android.user.api.UserApiInternalImpl
import com.squareup.sqldelight.internal.Atomic
import io.ktor.client.*
import kotlin.native.concurrent.SharedImmutable
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject

@SharedImmutable
internal val GLOBAL_SUPR_SEND_DATABASE_WRAPPER: Atomic<SSDatabaseWrapper?> = Atomic(null)

@SharedImmutable
internal val globalNetwork: Atomic<HttpClient?> = Atomic(null)

internal object SSApiInternal {

    private val userImpl = UserApiInternalImpl()

    var apiKey: String = ""
    private var flushing: Boolean = false

    fun purchaseMade(properties: String) {
        GlobalScope.launch(ioDispatcher() + coroutineExceptionHandler) {
            track(eventName = SSConstants.S_EVENT_PURCHASE_MADE, propertiesJO = properties.toKotlinJsonObject())
        }
    }

    fun notificationSubscribed() {
        GlobalScope.launch(ioDispatcher() + coroutineExceptionHandler) {
            track(eventName = SSConstants.S_EVENT_NOTIFICATION_SUBSCRIBED, propertiesJO = buildJsonObject { })
        }
    }

    fun notificationUnSubscribed() {
        GlobalScope.launch(ioDispatcher() + coroutineExceptionHandler) {
            track(eventName = SSConstants.S_EVENT_NOTIFICATION_UNSUBSCRIBED, propertiesJO = buildJsonObject { })
        }
    }

    fun pageVisited() {
        GlobalScope.launch(ioDispatcher() + coroutineExceptionHandler) {
            track(eventName = SSConstants.S_EVENT_PAGE_VISITED, propertiesJO = buildJsonObject { })
        }
    }

    fun identify(uniqueId: String) {
        GlobalScope.launch(ioDispatcher() + coroutineExceptionHandler) {
            Logger.i(TAG, "identity : $uniqueId")
            val userLocalDatasource = UserLocalDatasource()
            UserEventLocalDataSource()
                .track(
                    EventModel(
                        value = PayloadCreator
                            .buildIdentityEventPayload(
                                identifiedId = uniqueId,
                                anonymousId = userLocalDatasource.getIdentity(),
                            ),
                        id = uuid()
                    )
                )
            userLocalDatasource.identify(uniqueId)
            userImpl.setAndroidPush(getFcmToken())
            track(SSConstants.S_EVENT_USER_LOGIN, buildJsonObject {  }.toString())
            flush()
        }
    }

    fun setSuperProperties(propertiesJsonObject: String?) {
        GlobalScope.launch(ioDispatcher() + coroutineExceptionHandler) {
            Logger.i(TAG, "Setting super properties")
            val superPropertiesRepository = SuperPropertiesLocalDataSource()
            superPropertiesRepository.add(propertiesJsonObject.toKotlinJsonObject())
        }
    }

    fun track(eventName: String, propertiesJsonString: String?) {
        GlobalScope.launch(ioDispatcher() + coroutineExceptionHandler) {
            track(eventName, propertiesJsonString.toKotlinJsonObject())
        }
    }

    fun track(eventName: String, propertiesJO: JsonObject?) {
        try {
            Logger.i(TAG, "Track Event : $eventName")
            if (eventName.isBlank()) {
                Logger.i(TAG, "event name cannot be blank")
                return
            }

            val userLocalDatasource = UserLocalDatasource()
            val superPropertiesLocalDataSource = SuperPropertiesLocalDataSource()
            EventLocalDatasource()
                .track(
                    EventModel(
                        value = PayloadCreator.buildTrackEventPayload(
                            eventName = eventName,
                            distinctId = userLocalDatasource.getIdentity(),
                            superProperties = superPropertiesLocalDataSource.getAll(),
                            userProperties = propertiesJO
                        ),
                        id = uuid()
                    )
                )
        } catch (e: Exception) {
            Logger.e(TAG, "", e)
        }
    }

    fun getUser(): UserApiInternalContract {
        return userImpl
    }

    fun flush() {
        if (flushing) {
            Logger.i(TAG, "Flush request is ignored as flush is already in progress")
            return
        }

        Logger.i(TAG, "Trying to flush events")

        flushing = true

        GlobalScope.launch(ioDispatcher() + CoroutineExceptionHandler { _, throwable ->
            Logger.e(TAG, "Exception", throwable)
            flushing = false
        }) {
            Logger.i(TAG, "Flush event started")
            val eventFlushHandler = EventFlushHandler()
            eventFlushHandler.flushUserEvents()
            eventFlushHandler.flushEvents()
            flushing = false
            Logger.i(TAG, "Flush event completed")
        }
    }

    fun reset() {
        GlobalScope.launch(ioDispatcher() + coroutineExceptionHandler) {
            val newID = uuid()
            val userLocalDatasource = UserLocalDatasource()
            val userId = userLocalDatasource.getIdentity()
            Logger.i(TAG, "reset : Current : $userId New : $newID")
            track(SSConstants.S_EVENT_USER_LOGOUT, buildJsonObject { })
            userLocalDatasource.identify(newID)
            flush()
        }
    }

    fun isAppInstalled(): Boolean {
        return ConfigHelper.getBoolean(SSConstants.CONFIG_IS_APP_LAUNCHED) ?: false
    }

    /**
     * Is required to invalidated previous push notification token for same device id
     */
    fun setDeviceId(deviceId: String) {
        ConfigHelper.addOrUpdate(SSConstants.CONFIG_DEVICE_ID, deviceId)
    }

    fun getDeviceID(): String {
        return ConfigHelper.get(SSConstants.CONFIG_DEVICE_ID) ?: ""
    }

    fun setFcmToken(deviceId: String) {
        ConfigHelper.addOrUpdate(SSConstants.CONFIG_FCM_PUSH_TOKEN, deviceId)
    }

    fun getFcmToken(): String {
        return ConfigHelper.get(SSConstants.CONFIG_FCM_PUSH_TOKEN) ?: ""
    }

    fun setAppLaunched() {
        ConfigHelper.addOrUpdate(SSConstants.CONFIG_IS_APP_LAUNCHED, true)
    }

    fun initialize(databaseDriverFactory: DatabaseDriverFactory) {
        try {
            initializeDatabase(databaseDriverFactory)
            initializeNetworking()
        } catch (e: Exception) {
            Logger.e(TAG, "", e)
        }
    }

    private fun initializeNetworking() {
        val httpClient = HttpClient(engine = httpClientEngine)
        globalNetwork.set(httpClient)
    }

    private fun initializeDatabase(databaseDriverFactory: DatabaseDriverFactory) {
        val database = SSDatabaseWrapper(databaseDriverFactory)
        GLOBAL_SUPR_SEND_DATABASE_WRAPPER.set(database)
    }

    // Not included in contract

    fun getCachedApiKey(): String? {
        return ConfigHelper.get(SSConstants.CONFIG_API_KEY)
    }

    private const val TAG = "ssint"
}

internal val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
    Logger.e("exception", "", exception)
}
