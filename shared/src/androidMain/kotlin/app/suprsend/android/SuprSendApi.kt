package app.suprsend.android

import android.app.Application
import android.content.Context
import androidx.annotation.NonNull
import app.suprsend.android.base.ActivityLifecycleCallbackHandler
import app.suprsend.android.base.AndroidCreator
import app.suprsend.android.base.ExceptionHandler
import app.suprsend.android.base.PeriodicFlush
import app.suprsend.android.base.SuprSendConstants
import app.suprsend.android.base.getRandomString
import app.suprsend.android.config.ConfigHelper
import app.suprsend.android.database.DatabaseDriverFactory
import app.suprsend.android.user.UserLocalDatasource
import app.suprsend.android.user.UserRepository
import app.suprsend.android.user.api.UserApi
import org.json.JSONObject

class SuprSendApi
private constructor() {

    fun identify(uniqueId: String) {
        SuprSendApiInternal.identify(uniqueId)
    }

    fun setSuperProperties(jsonObject: JSONObject) {
        SuprSendApiInternal.setSuperProperties(propertiesJsonObject = jsonObject.toString())
    }

    fun track(@NonNull eventName: String, properties: JSONObject? = null) {
        SuprSendApiInternal.track(eventName = eventName, propertiesJsonString = properties?.toString())
    }

    fun getUser(): UserApi {
        return SuprSendApiInternal.getUser()
    }


    fun flush() {
        SuprSendApiInternal.flush()
    }

    fun reset() {
        SuprSendApiInternal.reset()
    }

    companion object {

        private var instance: SuprSendApi? = null

        fun getInstance(
            context: Context,
            apiKey: String
        ): SuprSendApi {

            synchronized(SuprSendApi::class.java) {
                if (instance == null) {

                    if (!AndroidCreator.isContextInitialized()) {
                        AndroidCreator.context = context.applicationContext
                    }

                    SuprSendApiInternal.initialize(
                        databaseDriverFactory = DatabaseDriverFactory(),
                        apiKey = apiKey
                    )

                    val userId = UserLocalDatasource().getIdentity()
                    if (userId.isBlank()) {
                        ConfigHelper.addOrUpdate(UserRepository.USER_ID, getRandomString(SuprSendConstants.ANONYMOUS_USER_ID_LENGTH))
                    }

                    val newInstance = SuprSendApi()
                    instance = newInstance

                    val application = context.applicationContext as Application

                    //To flush periodically
                    PeriodicFlush(newInstance).start()

                    //To flush based on activity lifecycle
                    application.registerActivityLifecycleCallbacks(ActivityLifecycleCallbackHandler(newInstance))

                    ExceptionHandler(newInstance).track()
                }
            }
            return instance!!
        }
    }

}