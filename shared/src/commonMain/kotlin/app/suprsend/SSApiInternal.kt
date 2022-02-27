package app.suprsend

import app.suprsend.base.Logger
import app.suprsend.base.SSConstants
import app.suprsend.base.SdkCreator
import app.suprsend.base.executorScope
import app.suprsend.base.filterSSReservedKeys
// import app.suprsend.base.flushExecutorScope
import app.suprsend.base.ioDispatcher
import app.suprsend.base.isInValidKey
import app.suprsend.base.toJsonObject
import app.suprsend.base.toKotlinJsonObject
import app.suprsend.base.uuid
import app.suprsend.config.ConfigHelper
import app.suprsend.event.EventFlushHandler
import app.suprsend.event.EventModel
import app.suprsend.event.PayloadCreator
import app.suprsend.sprop.SuperPropertiesLocalDataSource
import app.suprsend.user.SSInternalUser
import app.suprsend.user.UserLocalDatasource
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

internal object SSApiInternal {

    val userLocalDatasource = UserLocalDatasource()

    fun identify(uniqueId: String) {
        if (userLocalDatasource.getIdentity() == uniqueId) {
            return
        }
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
        appendNotificationToken()
        saveTrackEventPayload(SSConstants.S_EVENT_USER_LOGIN)
    }

    fun setSuperProperty(key: String, value: Any) {
        Logger.i(TAG, "Setting super properties $key $value")
        val superPropertiesRepository = SuperPropertiesLocalDataSource()
        superPropertiesRepository.add(key, value)
    }

    fun setSuperProperties(properties: Map<String, Any>) {
        Logger.i(TAG, "Setting super properties $properties")
        val superPropertiesRepository = SuperPropertiesLocalDataSource()
        superPropertiesRepository.add(properties.toJsonObject().filterSSReservedKeys())
    }

    fun removeSuperProperty(key: String) {
        Logger.i(TAG, "Remove super properties $key")
        val superPropertiesRepository = SuperPropertiesLocalDataSource()
        superPropertiesRepository.remove(key)
    }

    fun track(eventName: String, propertiesMap: Map<String, Any>?) {
        val properties = propertiesMap?.toJsonObject() ?: buildJsonObject {}
        if (eventName.isInValidKey()) {
            Logger.e("validation", "Event name should not contain $ & ss_ : $eventName")
            return
        }
        val filteredJson = properties.filterSSReservedKeys()

        saveTrackEventPayload(eventName, filteredJson)
    }

    fun purchaseMade(propertiesMap: Map<String, Any>) {
        saveTrackEventPayload(eventName = SSConstants.S_EVENT_PURCHASE_MADE, propertiesJO = propertiesMap.toJsonObject())
    }

    fun notificationSubscribed() {
        saveTrackEventPayload(eventName = SSConstants.S_EVENT_NOTIFICATION_SUBSCRIBED)
    }

    fun notificationUnSubscribed() {
        saveTrackEventPayload(eventName = SSConstants.S_EVENT_NOTIFICATION_UNSUBSCRIBED)
    }

    fun pageVisited() {
        saveTrackEventPayload(eventName = SSConstants.S_EVENT_PAGE_VISITED)
    }

    fun flush(mutationHandler: MutationHandler) {
        if (mutationHandler.isFlushing()) {
            Logger.i(EventFlushHandler.TAG, "Flush request is ignored as flush is already in progress")
            return
        }

        Logger.i(EventFlushHandler.TAG, "Trying to flush events")

        mutationHandler.setFlushing(true)

        executorScope.launch(ioDispatcher() + CoroutineExceptionHandler { _, throwable ->
            Logger.e(EventFlushHandler.TAG, "Exception", throwable)
            mutationHandler.setFlushing(false)
        }) {
            Logger.i(EventFlushHandler.TAG, "Flush event started")
            EventFlushHandler.flushEvents()
            mutationHandler.setFlushing(false)
            Logger.i(EventFlushHandler.TAG, "Flush event completed")
        }
    }

    fun reset() {
        val newID = uuid()
        val userId = userLocalDatasource.getIdentity()
        Logger.i(TAG, "reset : Current : $userId New : $newID")
        saveTrackEventPayload(SSConstants.S_EVENT_USER_LOGOUT)
        userLocalDatasource.identify(newID)
        appendNotificationToken()
    }

    fun saveTrackEventPayload(eventName: String, propertiesJO: JsonObject? = null) {
        if (eventName.isBlank()) {
            Logger.i(TAG, "event name cannot be blank")
            return
        }

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
    }

    private fun appendNotificationToken() {
        val iosPushToken = getIOSToken()
        if (iosPushToken.isNotBlank()) {
            SSInternalUser.storeOperatorPayload(
                properties = buildJsonObject {
                    put(SSConstants.PUSH_IOS_TOKEN, JsonPrimitive(iosPushToken))
                    put(SSConstants.PUSH_VENDOR, JsonPrimitive(SSConstants.PUSH_VENDOR_APNS))
                    put(SSConstants.DEVICE_ID, JsonPrimitive(getDeviceID()))
                },
                operator = SSConstants.APPEND
            )
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

    fun setIOSToken(token: String) {
        ConfigHelper.addOrUpdate(SSConstants.CONFIG_IOS_PUSH_TOKEN, token)
    }

    fun getIOSToken(): String {
        return ConfigHelper.get(SSConstants.CONFIG_IOS_PUSH_TOKEN) ?: ""
    }

    fun setAppLaunched() {
        ConfigHelper.addOrUpdate(SSConstants.CONFIG_IS_APP_LAUNCHED, true)
    }

    fun getCachedApiKey(): String {
        return ConfigHelper.get(SSConstants.CONFIG_API_KEY) ?: ""
    }

    fun startPeriodicFlush(mutationHandler: MutationHandler) {
        flow {
            while (true) {
                emit(true)
                delay(SSConstants.PERIODIC_FLUSH_EVENT_IN_SEC * 1000L)
            }
        }
            .onEach {
                Logger.i("flush", "Periodic flush")
                flush(mutationHandler)
            }
            .launchIn(executorScope)
    }

    const val TAG = "ssinternal"
}
