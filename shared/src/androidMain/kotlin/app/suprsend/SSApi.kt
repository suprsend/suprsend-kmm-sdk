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
    isFromCache: Boolean = false
) {

    private val mutationHandler: MutationHandler = AndroidMutationHandler()
    private val ssUserApi: SSUserApi = SSUserApi(mutationHandler)

    init {

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
        SSApiInternal.identify(uniqueId)
    }

    fun setSuperProperty(key: String, value: Any) {
        SSApiInternal.setSuperProperty(key, value)
    }

    fun setSuperProperties(jsonObject: JSONObject) {
//        SSApiInternal.setSuperProperties(propertiesJsonObject = jsonObject.toString())
    }

    fun unSetSuperProperty(key: String) {
        SSApiInternal.removeSuperProperty(key)
    }

    fun track(@NonNull eventName: String, properties: JSONObject? = null) {
        SSApiInternal.track(eventName = eventName, null)
    }

    fun purchaseMade(properties: JSONObject) {
//        SSApiInternal.purchaseMade(properties = properties.toString())
    }

    fun getUser(): SSUserApi {
        return ssUserApi
    }

    fun flush() {
        SSApiInternal.flush(mutationHandler)
    }

    fun reset() {
        SSApiInternal.reset()
    }

    fun setLogLevel(level: LogLevel) {
        logLevel.set(level)
    }

    companion object {

        private val instancesMap = hashMapOf<String, SSApi>()

        /**
         * Should be called before Application super.onCreate()
         */
        fun init(context: Context, apiKey: String, apiSecret: String, apiBaseUrl: String? = null) {

            // Setting android context to user everywhere
            if (!SdkAndroidCreator.isContextInitialized()) {
                SdkAndroidCreator.context = context.applicationContext
            }

            // Initialize nw and db
            SdkInitializer.initialize(databaseDriverFactory = DatabaseDriverFactory())

            val basicDetails = BasicDetails(apiKey, apiSecret, apiBaseUrl)
            ConfigHelper.addOrUpdate(SSConstants.CONFIG_API_BASE_URL, basicDetails.getApiBaseUrl())
            ConfigHelper.addOrUpdate(SSConstants.CONFIG_API_KEY, basicDetails.apiKey)
            ConfigHelper.addOrUpdate(SSConstants.CONFIG_API_SECRET, basicDetails.apiSecret)
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

        fun getInstance(): SSApi {
            return getInstanceInternal()
        }

        internal fun getInstanceFromCachedApiKey(): SSApi? {
            return getInstanceInternal(isFromCache = true)
        }

        private fun getInstanceInternal(isFromCache: Boolean = false): SSApi {
            val uniqueId = "only_one_instance_support"
            if (instancesMap.containsKey(uniqueId)) {
                return instancesMap[uniqueId]!!
            }
            val instance = SSApi(isFromCache)
            instancesMap[uniqueId] = instance
            return instance
        }
    }
}
