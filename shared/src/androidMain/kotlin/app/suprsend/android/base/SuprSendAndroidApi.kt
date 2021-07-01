package app.suprsend.android.base

import android.content.Context
import androidx.annotation.NonNull
import app.suprsend.android.database.DatabaseDriverFactory

object SuprSendAndroidApi {

    fun initialize(
        context: Context,
        databaseDriverFactory: DatabaseDriverFactory
    ) {
        AndroidCreator.context = context.applicationContext
        SuprSendSdkInternal.initialize(
            databaseDriverFactory = databaseDriverFactory
        )
    }

    fun trackEvent(@NonNull eventName: String) {
        trackEvent(eventName = eventName, properties = null)
    }

    fun trackEvent(@NonNull eventName: String, properties: Map<String, Any>?) {
        SuprSendSdkInternal.trackEvent(eventName = eventName, properties = properties)
    }

    fun flush() {
        SuprSendSdkInternal.flush()
    }

    private const val TAG = "SuprSendSdkAndroid"
}