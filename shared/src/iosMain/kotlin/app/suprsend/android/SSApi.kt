package app.suprsend.android

import app.suprsend.android.base.BasicDetails
import app.suprsend.android.base.LogLevel
import app.suprsend.android.base.SSConstants
import app.suprsend.android.base.SdkCreator
import app.suprsend.android.base.toJsonObject
import app.suprsend.android.base.uuid
import app.suprsend.android.config.ConfigHelper
import app.suprsend.android.database.DatabaseDriverFactory
import app.suprsend.android.user.UserLocalDatasource

class SSApi
private constructor(
    private val apiKey: String,
    private val apiSecret: String,
    private val apiBaseUrl: String? = null, // If null data will be directed to prod server
    private val mutationHandler: MutationHandler
) {
    private val basicDetails: BasicDetails = BasicDetails(apiKey, apiSecret, apiBaseUrl)
    private val ssUserApi: SSUserApi = SSUserApi(mutationHandler)

    init {

        ConfigHelper.addOrUpdate(SSConstants.CONFIG_API_BASE_URL, basicDetails.getApiBaseUrl())
        ConfigHelper.addOrUpdate(SSConstants.CONFIG_API_KEY, basicDetails.apiKey)
        ConfigHelper.addOrUpdate(SSConstants.CONFIG_API_SECRET, basicDetails.apiSecret)

        // Anonymous user id generation
        val userLocalDatasource = UserLocalDatasource()
        val userId = userLocalDatasource.getIdentity()
        if (userId.isBlank()) {
            userLocalDatasource.identify(uuid())
        }

        // Device Properties
//            SSApiInternal.setDeviceId(SdkAndroidCreator.deviceInfo.getDeviceId())
        if (!SSApiInternal.isAppInstalled()) {
            // App Launched
            track(SSConstants.S_EVENT_APP_INSTALLED)
            SSApiInternal.setAppLaunched()
        }

        track(SSConstants.S_EVENT_APP_LAUNCHED)
    }

    fun identify(uniqueId: String) {
        SSApiInternal.identify(uniqueId, mutationHandler)
    }

    fun setSuperProperty(key: String, value: Any) {
        SSApiInternal.setSuperProperty(key, value)
    }

    fun setSuperProperties(properties: Map<String, Any>) {
        SSApiInternal.setSuperProperties(propertiesJsonObject = properties.toJsonObject().toString())
    }

    fun unSetSuperProperty(key: String) {
        SSApiInternal.removeSuperProperty(key)
    }

    fun track(eventName: String, properties: Map<String, Any>? = null) {
        SSApiInternal.trackOp(eventName = eventName, propertiesJsonString = properties?.toJsonObject()?.toString())
    }

    fun purchaseMade(properties: Map<String, Any>) {
        SSApiInternal.purchaseMade(properties = properties.toJsonObject().toString())
    }

    fun getUser(): SSUserApi {
        return ssUserApi
    }

    fun flush() {
        SSApiInternal.flush(mutationHandler)
    }

    fun reset() {
        SSApiInternal.reset(mutationHandler)
    }

    fun setLogLevel(logLevel: LogLevel) {
        SdkCreator.logLevel.set(logLevel)
    }

    companion object {
        /**
         * Should be called before Application super.onCreate()
         */
        fun init() {

            // Initialize nw and db
            SdkInitializer.initialize(databaseDriverFactory = DatabaseDriverFactory())
        }

        fun getInstance(apiKey: String, apiSecret: String, apiBaseUrl: String? = null, mutationHandler: MutationHandler): SSApi {
            return SSApi(apiKey, apiSecret, apiBaseUrl, mutationHandler)
        }

        fun getInstanceFromCachedApiKey(mutationHandler: MutationHandler): SSApi? {
            val apiKey = ConfigHelper.get(SSConstants.CONFIG_API_KEY) ?: return null
            val secret = ConfigHelper.get(SSConstants.CONFIG_API_SECRET) ?: return null
            val apiBaseUrl = ConfigHelper.get(SSConstants.CONFIG_API_BASE_URL) ?: return null
            return getInstance(apiKey, secret, apiBaseUrl, mutationHandler)
        }
    }
}
