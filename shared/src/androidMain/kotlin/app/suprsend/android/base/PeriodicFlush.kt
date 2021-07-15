package app.suprsend.android.base

import android.os.Handler
import android.os.Looper
import app.suprsend.android.SuprSendApi

class PeriodicFlush(
    private val suprSendApi: SuprSendApi
) {
    // For evey delay seconds it will flush events
    private val delay = 60 * 1000L
    var handler: Handler = Handler(Looper.getMainLooper())
    var runnable: Runnable? = null

    fun start() {
        handler.postDelayed(Runnable {
            Logger.i(TAG, "Periodic flush")
            suprSendApi.flush()
            handler.postDelayed(runnable!!, delay)
        }.also { runnable = it }, delay)
    }

    companion object {
        const val TAG = "per"
    }
}