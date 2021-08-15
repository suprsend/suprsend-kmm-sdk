package app.suprsend.android.base

import android.annotation.SuppressLint
import android.content.Context

@SuppressLint("StaticFieldLeak")
internal object AndroidCreator {

    // Keeping only application context here
    lateinit var context: Context

    fun isContextInitialized(): Boolean {
        return this::context.isInitialized
    }

    val deviceInfo: DeviceInfo by lazy { DeviceInfo() }

    val networkInfo: NetworkInfo by lazy { NetworkInfo() }
}
