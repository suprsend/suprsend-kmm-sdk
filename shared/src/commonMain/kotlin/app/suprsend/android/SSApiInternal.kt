package app.suprsend.android

import app.suprsend.android.base.Logger
import app.suprsend.android.base.SSConstants
import app.suprsend.android.base.SdkCreator
import app.suprsend.android.base.ioDispatcher
import app.suprsend.android.base.singleThreadDispatcher
import app.suprsend.android.base.toKotlinJsonObject
import app.suprsend.android.base.uuid
import app.suprsend.android.config.ConfigHelper
import app.suprsend.android.event.EventFlushHandler
import app.suprsend.android.event.EventModel
import app.suprsend.android.event.PayloadCreator
import app.suprsend.android.sprop.SuperPropertiesLocalDataSource
import app.suprsend.android.user.UserLocalDatasource
import app.suprsend.android.user.api.UserApiInternalContract
import app.suprsend.android.user.api.UserApiInternalImpl
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

internal object SSApiInternal {

    private val userImpl = UserApiInternalImpl()
    private val coroutineScope = CoroutineScope(SupervisorJob())

    fun purchaseMade(properties: String) {
        coroutineScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            trackOp(eventName = SSConstants.S_EVENT_PURCHASE_MADE, propertiesJO = properties.toKotlinJsonObject())
        }
    }

    fun notificationSubscribed() {
        coroutineScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            trackOp(eventName = SSConstants.S_EVENT_NOTIFICATION_SUBSCRIBED, propertiesJO = buildJsonObject { })
        }
    }

    fun notificationUnSubscribed() {
        coroutineScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            trackOp(eventName = SSConstants.S_EVENT_NOTIFICATION_UNSUBSCRIBED, propertiesJO = buildJsonObject { })
        }
    }

    fun pageVisited() {
        coroutineScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            trackOp(eventName = SSConstants.S_EVENT_PAGE_VISITED, propertiesJO = buildJsonObject { })
        }
    }

    fun identify(uniqueId: String, mutationHandler: MutationHandler) {
        coroutineScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            val userLocalDatasource = UserLocalDatasource()
            SdkCreator
                .eventLocalDatasource
                .track(
                    EventModel(
                        value = PayloadCreator
                            .buildIdentityEventPayload(
                                identifiedId = uniqueId,
                                anonymousId = userLocalDatasource.getIdentity()
                            ),
                        id = uuid()
                    )
                )
            userLocalDatasource.identify(uniqueId)
            userImpl.internalOperatorCallOp(buildJsonObject {
                put(SSConstants.FCM_TOKEN_PUSH, JsonPrimitive(getFcmToken()))
                put(SSConstants.DEVICE_ID, JsonPrimitive(getDeviceID()))
            }, SSConstants.APPEND)
            trackOp(SSConstants.S_EVENT_USER_LOGIN, buildJsonObject { })
            flush(mutationHandler)
        }
    }

    fun setSuperProperty(key: String, value: Any) {
        coroutineScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            Logger.i(TAG, "Setting super properties $key $value")
            val superPropertiesRepository = SuperPropertiesLocalDataSource()
            superPropertiesRepository.add(key, value)
        }
    }

    fun setSuperProperties(propertiesJsonObject: String?) {
        coroutineScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            Logger.i(TAG, "Setting super properties $propertiesJsonObject")
            val superPropertiesRepository = SuperPropertiesLocalDataSource()
            superPropertiesRepository.add(propertiesJsonObject.toKotlinJsonObject())
        }
    }

    fun removeSuperProperty(key: String) {
        coroutineScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            Logger.i(TAG, "Remove super properties $key")
            val superPropertiesRepository = SuperPropertiesLocalDataSource()
            superPropertiesRepository.remove(key)
        }
    }

    fun trackOp(eventName: String, propertiesJsonString: String?) {
        coroutineScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            trackOp(eventName, propertiesJsonString.toKotlinJsonObject())
        }
    }

    private fun trackOp(eventName: String, propertiesJO: JsonObject?) {
        try {
            if (eventName.isBlank()) {
                Logger.i(TAG, "event name cannot be blank")
                return
            }

            val userLocalDatasource = UserLocalDatasource()
            val superPropertiesLocalDataSource = SuperPropertiesLocalDataSource()
            SdkCreator
                .eventLocalDatasource
                .track(
                    EventModel(
                        value = PayloadCreator.buildTrackEventPayload(
                            eventName = eventName,
                            distinctId = userLocalDatasource.getIdentity(),
                            superProperties = superPropertiesLocalDataSource.getAll(),
                            defaultProperties = SdkCreator.information.getDefaultProperties().toKotlinJsonObject(),
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

    fun flush(mutationHandler: MutationHandler) {
        if (mutationHandler.isFlushing()) {
            Logger.i(EventFlushHandler.TAG, "Flush request is ignored as flush is already in progress")
            return
        }

        Logger.i(EventFlushHandler.TAG, "Trying to flush events")

        mutationHandler.setFlushing(true)

        coroutineScope.launch(ioDispatcher() + CoroutineExceptionHandler { _, throwable ->
            Logger.e(EventFlushHandler.TAG, "Exception", throwable)
            mutationHandler.setFlushing(false)
        }) {
            Logger.i(EventFlushHandler.TAG, "Flush event started")
            EventFlushHandler.flushEvents()
            mutationHandler.setFlushing(false)
            Logger.i(EventFlushHandler.TAG, "Flush event completed")
        }
    }

    fun reset(mutationHandler: MutationHandler) {
        coroutineScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            val newID = uuid()
            val userLocalDatasource = UserLocalDatasource()
            val userId = userLocalDatasource.getIdentity()
            Logger.i(TAG, "reset : Current : $userId New : $newID")
            trackOp(SSConstants.S_EVENT_USER_LOGOUT, buildJsonObject { })
            userLocalDatasource.identify(newID)
            userImpl.internalOperatorCallOp(buildJsonObject {
                put(SSConstants.FCM_TOKEN_PUSH, JsonPrimitive(getFcmToken()))
                put(SSConstants.DEVICE_ID, JsonPrimitive(getDeviceID()))
            }, SSConstants.APPEND)
            flush(mutationHandler)
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

    fun getCachedApiKey(): String {
        return ConfigHelper.get(SSConstants.CONFIG_API_KEY) ?: ""
    }

    const val TAG = "ssinternal"
}

internal val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
    Logger.e("exception", "", exception)
}
