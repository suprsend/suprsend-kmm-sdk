package app.suprsend

import app.suprsend.base.BasicDetails
import app.suprsend.base.LogLevel
import app.suprsend.base.Logger
import app.suprsend.base.SSConstants
import app.suprsend.base.SdkIosCreator
import app.suprsend.base.logLevel
import app.suprsend.base.toJsonObject
import app.suprsend.base.uuid
import app.suprsend.config.ConfigHelper
import app.suprsend.database.DatabaseDriverFactory
import app.suprsend.user.UserLocalDatasource
import kotlinx.serialization.json.buildJsonObject

class IOSSSApi
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
        SSApiInternal.setDeviceId(SdkIosCreator.deviceInfo.getDeviceId())

        if (!SSApiInternal.isAppInstalled()) {
            // App Launched
            SSApiInternal.trackOp(eventName = SSConstants.S_EVENT_APP_INSTALLED, propertiesJO = buildJsonObject { })
            SSApiInternal.setAppLaunched()
        }

        SSApiInternal.trackOp(eventName = SSConstants.S_EVENT_APP_LAUNCHED, propertiesJO = buildJsonObject { })

        SSApiInternal.startPeriodicFlush(mutationHandler)

        // Flush on lifecycle

        // Flush on Exception
    }

    fun identify(uniqueId: String) {
        try {
            SSApiInternal.identify(uniqueId, mutationHandler)
        } catch (exception: Exception) {
            Logger.e(TAG_EXCEPTION, "", exception)
        }
    }

    fun setSuperProperty(key: String, value: Any) {
        try {
            SSApiInternal.setSuperProperty(key, value)
        } catch (exception: Exception) {
            Logger.e(TAG_EXCEPTION, "", exception)
        }
    }

    fun setSuperProperties(properties: Map<String, Any>) {
        try {
            SSApiInternal.setSuperProperties(propertiesJsonObject = properties.toJsonObject().toString())
        } catch (exception: Exception) {
            Logger.e(TAG_EXCEPTION, "", exception)
        }
    }

    fun unSetSuperProperty(key: String) {
        try {
            SSApiInternal.removeSuperProperty(key)
        } catch (exception: Exception) {
            Logger.e(TAG_EXCEPTION, "", exception)
        }
    }

    fun track(eventName: String, properties: Map<String, Any>? = null) {
        try {
            SSApiInternal.trackOp(eventName = eventName, propertiesJsonString = properties?.toJsonObject()?.toString())
        } catch (exception: Exception) {
            Logger.e(TAG_EXCEPTION, "", exception)
        }
    }

    fun track(eventName: String) {
        try {
            SSApiInternal.trackOp(eventName = eventName, propertiesJsonString = null)
        } catch (exception: Exception) {
            Logger.e(TAG_EXCEPTION, "", exception)
        }
    }

    fun purchaseMade(properties: Map<String, Any>) {
        try {
            SSApiInternal.purchaseMade(properties = properties.toJsonObject().toString())
        } catch (exception: Exception) {
            Logger.e(TAG_EXCEPTION, "", exception)
        }
    }

    fun getUser(): SSUserApi {
        return ssUserApi
    }

    fun flush() {
        try {
            SSApiInternal.flush(mutationHandler)
        } catch (exception: Exception) {
            Logger.e(TAG_EXCEPTION, "", exception)
        }
    }

    fun reset(unSubscribeNotification: Boolean) {
        try {
            SSApiInternal.reset(mutationHandler, unSubscribeNotification)
        } catch (exception: Exception) {
            Logger.e(TAG_EXCEPTION, "", exception)
        }
    }

    companion object {
        /**
         * Should be called before Application super.onCreate()
         */
        fun initialize() {

            // Initialize nw and db
            try {
                SdkInitializer.initialize(databaseDriverFactory = DatabaseDriverFactory())
            } catch (exception: Exception) {
                Logger.e(TAG_EXCEPTION, "", exception)
            }
        }

        fun enableLogging() {
            try {
                logLevel.set(LogLevel.VERBOSE)
            } catch (exception: Exception) {
                Logger.e(TAG_EXCEPTION, "", exception)
            }
        }

        @Throws(Exception::class)
        fun getInstance(apiKey: String, apiSecret: String, apiBaseUrl: String? = null, mutationHandler: MutationHandler): IOSSSApi {
            return IOSSSApi(apiKey, apiSecret, apiBaseUrl, mutationHandler)
        }

        @Throws(Exception::class)
        fun getInstanceFromCachedApiKey(mutationHandler: MutationHandler): IOSSSApi? {
            val apiKey = ConfigHelper.get(SSConstants.CONFIG_API_KEY) ?: return null
            val secret = ConfigHelper.get(SSConstants.CONFIG_API_SECRET) ?: return null
            val apiBaseUrl = ConfigHelper.get(SSConstants.CONFIG_API_BASE_URL) ?: return null
            return getInstance(apiKey, secret, apiBaseUrl, mutationHandler)
        }
    }
}
