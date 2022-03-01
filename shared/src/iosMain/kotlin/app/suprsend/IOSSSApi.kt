package app.suprsend

import app.suprsend.base.BasicDetails
import app.suprsend.base.LogLevel
import app.suprsend.base.SSConstants
import app.suprsend.base.SdkIosCreator
import app.suprsend.base.coroutineExceptionHandler
import app.suprsend.base.executorScope
import app.suprsend.base.logLevel
import app.suprsend.base.singleThreadDispatcher
import app.suprsend.base.toJsonObject
import app.suprsend.base.uuid
import app.suprsend.config.ConfigHelper
import app.suprsend.database.DatabaseDriverFactory
import app.suprsend.user.UserLocalDatasource
import kotlinx.coroutines.launch

class IOSSSApi
private constructor(
    private val isFromCache: Boolean = false,
    private val mutationHandler: MutationHandler
) {

    private val ssUserApi: SSUserApi = SSUserApi()

    init {

        // Anonymous user id generation
        val userLocalDatasource = UserLocalDatasource()
        val userId = userLocalDatasource.getIdentity()
        if (userId.isBlank()) {
            userLocalDatasource.identify(uuid())
        }

        // Device Properties
        SSApiInternal.setDeviceId(SdkIosCreator.deviceInfo.getDeviceId())

        if (!SSApiInternal.isAppInstalled()) {
            // App Launched
            SSApiInternal.saveTrackEventPayload(SSConstants.S_EVENT_APP_INSTALLED)
            SSApiInternal.setAppLaunched()
        }

        if (!isFromCache)
            SSApiInternal.saveTrackEventPayload(SSConstants.S_EVENT_APP_LAUNCHED)

        SSApiInternal.startPeriodicFlush(mutationHandler)
    }

    fun identify(uniqueId: String) {
        executorScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            SSApiInternal.identify(uniqueId)
            SSApiInternal.flush(mutationHandler)
        }
    }

    fun setSuperProperty(key: String, value: Any) {
        executorScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            SSApiInternal.setSuperProperty(key, value)
        }
    }

    fun setSuperProperties(properties: Map<String, Any>) {
        executorScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            SSApiInternal.setSuperProperties(properties = properties.toJsonObject())
        }
    }

    fun unSetSuperProperty(key: String) {
        executorScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            SSApiInternal.removeSuperProperty(key)
        }
    }

    fun track(eventName: String, properties: Map<String, Any>? = null) {
        executorScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            SSApiInternal.track(eventName = eventName, propertiesMap = properties)
        }
    }

    fun track(eventName: String) {
        executorScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            SSApiInternal.track(eventName = eventName, propertiesMap = null)
        }
    }

    fun purchaseMade(properties: Map<String, Any>) {
        executorScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            SSApiInternal.purchaseMade(propertiesMap = properties)
        }
    }

    fun getUser(): SSUserApi {
        return ssUserApi
    }

    fun flush() {
        executorScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            SSApiInternal.flush(mutationHandler)
        }
    }

    fun reset() {
        executorScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            SSApiInternal.reset()
            SSApiInternal.flush(mutationHandler)
        }
    }

    companion object {

        private val instancesMap = hashMapOf<String, IOSSSApi>()

        /**
         * Should be called before Application super.onCreate()
         */
        fun initialize(apiKey: String, apiSecret: String, apiBaseUrl: String? = null) {
            // Initialize nw and db
            SdkInitializer.initialize(databaseDriverFactory = DatabaseDriverFactory())

            val basicDetails = BasicDetails(apiKey, apiSecret, apiBaseUrl)
            ConfigHelper.addOrUpdate(SSConstants.CONFIG_API_BASE_URL, basicDetails.getApiBaseUrl())
            ConfigHelper.addOrUpdate(SSConstants.CONFIG_API_KEY, basicDetails.apiKey)
            ConfigHelper.addOrUpdate(SSConstants.CONFIG_API_SECRET, basicDetails.apiSecret)
        }

        fun enableLogging() {
            logLevel.set(LogLevel.VERBOSE)
        }

        fun getInstance(isFromCache: Boolean, mutationHandler: MutationHandler): IOSSSApi {
            return IOSSSApi(isFromCache = isFromCache, mutationHandler = mutationHandler)
        }
    }
}
