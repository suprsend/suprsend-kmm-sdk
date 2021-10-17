package app.suprsend.android

import app.suprsend.android.base.LogLevel
import app.suprsend.android.base.Logger
import app.suprsend.android.base.SSConstants
import app.suprsend.android.base.toJsonObject
import app.suprsend.android.base.uuid
import app.suprsend.android.config.ConfigHelper
import app.suprsend.android.database.DatabaseDriverFactory
import app.suprsend.android.user.UserLocalDatasource

public class SSApi constructor() {

    private val ssUserApi = SSUserApi()

    fun identify(uniqueId: String) {
        SSApiInternal.identify(uniqueId)
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
        SSApiInternal.flush()
    }

    fun reset() {
        SSApiInternal.reset()
    }

    fun setLogLevel(logLevel: LogLevel) {
        Logger.logLevel = logLevel
    }
}

public class SSApiHelper {
    fun getInstance(apiKey: String, secret: String): SSApi {
        return getInstanceInternal(apiKey, secret, true)
    }

    private fun getInstanceInternal(apiKey: String, secret: String, isStart: Boolean): SSApi {

        // Todo - Make sure that for multi user multiple instance of DatabaseDriverFactory will get initialize this should not happen
        initializeDBNW()

        ConfigHelper.addOrUpdate(SSConstants.CONFIG_API_KEY, apiKey)
        ConfigHelper.addOrUpdate(SSConstants.CONFIG_SECRET, secret)

        // Anynomous user id generation
        val userLocalDatasource = UserLocalDatasource()
        val userId = userLocalDatasource.getIdentity()
        if (userId.isBlank()) {
            userLocalDatasource.identify(uuid())
        }

        val instance: SSApi = SSApi()

        // Device Properties
//            SSApiInternal.setDeviceId(SdkAndroidCreator.deviceInfo.getDeviceId())

        if (isStart && !SSApiInternal.isAppInstalled()) {
            // App Launched
            instance.track(SSConstants.S_EVENT_APP_INSTALLED)
            SSApiInternal.setAppLaunched()
        }

        if (isStart)
            instance.track(SSConstants.S_EVENT_APP_LAUNCHED)

//            val application = context.applicationContext as Application

        // Flush periodically
//            PeriodicFlush(instance).register()

        // Flush on activity lifecycle
//            application.registerActivityLifecycleCallbacks(ActivityLifecycleCallbackHandler(instance))

        // Flush on Exception
        // ExceptionHandler(instance).track()
        return instance
    }

    fun getInstanceFromCachedApiKey(): SSApi? {
        initializeDBNW()
        val apiKey = ConfigHelper.get(SSConstants.CONFIG_API_KEY) ?: ""
        val secret = ConfigHelper.get(SSConstants.CONFIG_SECRET) ?: ""
        if (apiKey.isNotBlank()) {
            return getInstanceInternal(apiKey, secret, false)
        }
        return null
    }

    companion object {

        private fun initializeDBNW() {
            // Setting android context to user everywhere
//            if (!SdkAndroidCreator.isContextInitialized()) {
//                SdkAndroidCreator.context = context.applicationContext
//            }

            // Initialize nw and db
            SSApiInternal.initialize(databaseDriverFactory = DatabaseDriverFactory())
        }

        fun init(apiBaseUrl: String) {

            initializeDBNW()

            var processedBaseUrl = apiBaseUrl
            if (processedBaseUrl.endsWith("/"))
                processedBaseUrl = processedBaseUrl.removeSuffix("/")

            ConfigHelper.addOrUpdate(SSConstants.CONFIG_API_BASE_URL, processedBaseUrl)
        }

        fun setLogLevel(logLevel: LogLevel) {
            Logger.logLevel = logLevel
        }
    }
}
