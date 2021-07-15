package app.suprsend.android.base

import app.suprsend.android.SuprSendApi
import kotlin.system.exitProcess
import android.os.Process

class ExceptionHandler(
    private val suprSendApi: SuprSendApi
) {
    fun track() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, exc ->

            Logger.i(TAG,"Exception handler flush")
            suprSendApi.flush()

            if (defaultHandler != null) {
                defaultHandler.uncaughtException(thread, exc)
            } else
                killProcessAndExit()
        }
    }

    private fun killProcessAndExit() {
        try {
            Thread.sleep(400)
        } catch (e: InterruptedException) {
            Logger.e(TAG, "", e)
        }
        Process.killProcess(Process.myPid())
        exitProcess(10);
    }
    companion object{
        const val TAG = "exc"
    }
}