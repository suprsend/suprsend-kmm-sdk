package app.suprsend.android

import android.annotation.SuppressLint
import android.content.Context

class SuprSendAndroidConfig(
    val context: Context
) {
    companion object {

        @SuppressLint("StaticFieldLeak")
        lateinit var suprSendAndroidConfig: SuprSendAndroidConfig

        fun initialize(context: Context) {
            if (!(Companion::suprSendAndroidConfig.isInitialized)) {
                synchronized(this) {
                    if (!(Companion::suprSendAndroidConfig.isInitialized)) {
                        suprSendAndroidConfig = SuprSendAndroidConfig(context)
                    }
                }
            }
        }
    }
}
