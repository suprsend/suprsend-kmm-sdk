package app.suprsend.android

import android.content.Context

class AndroidConfig(
    val context: Context
) {
    companion object {
        lateinit var androidConfig: AndroidConfig
        fun initialize(context: Context) {
            if (!(Companion::androidConfig.isInitialized)) {
                synchronized(this) {
                    if (!(Companion::androidConfig.isInitialized)) {
                        androidConfig = AndroidConfig(context)
                    }
                }
            }
        }
    }
}
