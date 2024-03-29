package app.suprsend

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.annotation.NonNull
import app.suprsend.base.ActivityLifecycleCallbackHandler
import app.suprsend.base.BasicDetails
import app.suprsend.base.LogLevel
import app.suprsend.base.SSConstants
import app.suprsend.base.SdkAndroidCreator
import app.suprsend.base.logLevel
import app.suprsend.base.uuid
import app.suprsend.config.ConfigHelper
import app.suprsend.database.DatabaseDriverFactory
import app.suprsend.user.UserLocalDatasource
import app.suprsend.xiaomi.SSXiaomiReceiver
import com.xiaomi.channel.commonutils.logger.LoggerInterface
import com.xiaomi.mipush.sdk.Logger
import com.xiaomi.mipush.sdk.MiPushClient
import org.json.JSONObject

class SSApi
private constructor(
    apiKey: String,
    apiSecret: String,
    apiBaseUrl: String? = null, // If null data will be directed to prod server
    isFromCache: Boolean = false
) {

    private val basicDetails: BasicDetails = BasicDetails(apiKey, apiSecret, apiBaseUrl)
    private val mutationHandler: MutationHandler = AndroidMutationHandler()
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
        SSApiInternal.setDeviceId(SdkAndroidCreator.deviceInfo.getDeviceId())

        if (!SSApiInternal.isAppInstalled()) {
            // App Launched
            track(SSConstants.S_EVENT_APP_INSTALLED)
            SSApiInternal.setAppLaunched()
        }

        if (!isFromCache)
            track(SSConstants.S_EVENT_APP_LAUNCHED)

        val application = SdkAndroidCreator.context.applicationContext as Application

        // Flush periodically
        SSApiInternal.startPeriodicFlush(mutationHandler)

        // Flush on activity lifecycle
        application.registerActivityLifecycleCallbacks(ActivityLifecycleCallbackHandler(this))

        // Flush on Exception
        // ExceptionHandler(newInstance).track()
    }

    fun identify(uniqueId: String) {
        SSApiInternal.identify(uniqueId, mutationHandler)
    }

    fun setSuperProperty(key: String, value: Any) {
        SSApiInternal.setSuperProperty(key, value)
    }

    fun setSuperProperties(jsonObject: JSONObject) {
        SSApiInternal.setSuperProperties(propertiesJsonObject = jsonObject.toString())
    }

    fun unSetSuperProperty(key: String) {
        SSApiInternal.removeSuperProperty(key)
    }

    fun track(@NonNull eventName: String, properties: JSONObject? = null) {
        SSApiInternal.trackOp(eventName = eventName, propertiesJsonString = properties?.toString())
    }

    fun purchaseMade(properties: JSONObject) {
        SSApiInternal.purchaseMade(properties = properties.toString())
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

    fun setLogLevel(level: LogLevel) {
        logLevel.set(level)
    }

    companion object {

        private val instancesMap = hashMapOf<String, SSApi>()

        /**
         * Should be called before Application super.onCreate()
         */
        fun init(context: Context) {

            // Setting android context to user everywhere
            if (!SdkAndroidCreator.isContextInitialized()) {
                SdkAndroidCreator.context = context.applicationContext
            }

            // Initialize nw and db
            SdkInitializer.initialize(databaseDriverFactory = DatabaseDriverFactory())
        }

        fun initXiaomi(context: Context, appId: String, apiKey: String) {
            MiPushClient.registerPush(context, appId, apiKey)
            Logger.setLogger(context, object : LoggerInterface {
                override fun setTag(tag: String?) {
                    Log.d(SSXiaomiReceiver.TAG, "set Tag : $tag")
                }

                override fun log(message: String?) {
                    Log.d(SSXiaomiReceiver.TAG, "Log : $message")
                }

                override fun log(message: String?, throwable: Throwable?) {
                    Log.e(SSXiaomiReceiver.TAG, "Log : $message", throwable)
                }
            })
        }

        fun getInstance(apiKey: String, apiSecret: String, apiBaseUrl: String? = null): SSApi {
            return getInstanceInternal(apiKey = apiKey, apiSecret = apiSecret, apiBaseUrl = apiBaseUrl)
        }

        internal fun getInstanceFromCachedApiKey(): SSApi? {
            val apiKey = ConfigHelper.get(SSConstants.CONFIG_API_KEY) ?: return null
            val secret = ConfigHelper.get(SSConstants.CONFIG_API_SECRET) ?: return null
            val apiBaseUrl = ConfigHelper.get(SSConstants.CONFIG_API_BASE_URL) ?: return null
            return getInstanceInternal(apiKey = apiKey, apiSecret = secret, apiBaseUrl = apiBaseUrl, isFromCache = true)
        }

        private fun getInstanceInternal(apiKey: String, apiSecret: String, apiBaseUrl: String? = null, isFromCache: Boolean = false): SSApi {
            val uniqueId = "$apiKey-$apiSecret"
            if (instancesMap.containsKey(uniqueId)) {
                return instancesMap[uniqueId]!!
            }
            val instance = SSApi(apiKey, apiSecret, apiBaseUrl, isFromCache)
            instancesMap[uniqueId] = instance
            return instance
        }
    }
}
