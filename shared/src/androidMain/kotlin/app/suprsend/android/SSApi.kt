package app.suprsend.android

import android.app.Application
import android.content.Context
import androidx.annotation.NonNull
import app.suprsend.android.base.ActivityLifecycleCallbackHandler
import app.suprsend.android.base.AndroidCreator
import app.suprsend.android.base.DeviceInfo
import app.suprsend.android.base.PeriodicFlush
import app.suprsend.android.base.uuid
import app.suprsend.android.database.DatabaseDriverFactory
import app.suprsend.android.user.UserLocalDatasource
import org.json.JSONObject

class SSApi
private constructor() {

    private val ssUserApi = SSUserApi()
    fun identify(uniqueId: String) {
        SSApiInternal.identify(uniqueId)
    }

    fun setSuperProperties(jsonObject: JSONObject) {
        SSApiInternal.setSuperProperties(propertiesJsonObject = jsonObject.toString())
    }

    fun track(@NonNull eventName: String, properties: JSONObject? = null) {
        SSApiInternal.track(eventName = eventName, propertiesJsonString = properties?.toString())
    }

    fun getUser(): SSUserApi {
        return ssUserApi
    }

    fun flush() {
        SSApiInternal.flush()
    }

    companion object {

        private var instance: SSApi? = null

        fun getInstance(
            context: Context,
            apiKey: String
        ): SSApi {

            synchronized(SSApi::class.java) {
                if (instance == null) {

                    // Setting android context to user everywhere
                    if (!AndroidCreator.isContextInitialized()) {
                        AndroidCreator.context = context.applicationContext
                    }

                    // Initialize nw and db
                    SSApiInternal.initialize(
                        databaseDriverFactory = DatabaseDriverFactory(),
                        apiKey = apiKey
                    )

                    // Anynomous user id generation
                    val userLocalDatasource = UserLocalDatasource()
                    val userId = userLocalDatasource.getIdentity()
                    if (userId.isBlank()) {
                        userLocalDatasource.identify(uuid())
                    }

                    val newInstance = SSApi()
                    instance = newInstance

                    // Device Properties
                    newInstance.getUser().set(DeviceInfo(context).getDeviceInfoProperties())

                    val application = context.applicationContext as Application

                    // Flush periodically
                    PeriodicFlush(newInstance).start()

                    // Flush on activity lifecycle
                    application.registerActivityLifecycleCallbacks(ActivityLifecycleCallbackHandler(newInstance))

                    // Flush on Exception
                    // ExceptionHandler(newInstance).track()
                }
            }
            return instance!!
        }
    }
}
