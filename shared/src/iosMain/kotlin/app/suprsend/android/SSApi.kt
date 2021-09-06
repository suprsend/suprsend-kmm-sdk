package app.suprsend.android

import app.suprsend.android.base.LogLevel
import app.suprsend.android.base.Logger
import app.suprsend.android.base.SSConstants
import app.suprsend.android.base.toJsonObject
import app.suprsend.android.base.uuid
import app.suprsend.android.config.ConfigHelper
import app.suprsend.android.database.DatabaseDriverFactory
import app.suprsend.android.user.UserLocalDatasource

class SSApi
private constructor() {

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

    companion object {


        fun getInstance(apiKey: String): SSApi {
            return getInstanceInternal(apiKey, true)
        }

        private fun initializeDBNW() {
            // Setting android context to user everywhere
//            if (!SdkAndroidCreator.isContextInitialized()) {
//                SdkAndroidCreator.context = context.applicationContext
//            }

            // Initialize nw and db
            SSApiInternal.initialize(databaseDriverFactory = DatabaseDriverFactory())
        }

        private fun getInstanceInternal(apiKey: String, isStart: Boolean): SSApi {


            // Todo - Make sure that for multi user multiple instance of DatabaseDriverFactory will get initialize this should not happen
            initializeDBNW()

            SSApiInternal.apiKey = apiKey
            ConfigHelper.addOrUpdate(SSConstants.CONFIG_API_KEY, SSApiInternal.apiKey)

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
            val apiKey = SSApiInternal.getCachedApiKey()
            if (apiKey != null) {
                return getInstanceInternal(apiKey, false)
            }
            return null
        }
    }
}
