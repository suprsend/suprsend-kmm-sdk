package app.suprsend.android.base

import android.util.Log

internal actual class LoggerKMM {

    actual var logLevel: LogLevel = LogLevel.OFF


    fun v(tag: String?, message: String?) {
        if (isLogAllowed(LogLevel.VERBOSE.num)) {
            Log.v(tag, message!!)
        }
    }

    fun v(tag: String?, message: String?, throwable: Throwable?) {
        if (isLogAllowed(LogLevel.VERBOSE.num)) {
            Log.v(tag, message, throwable)
        }
    }

    fun d(tag: String?, message: String?) {
        if (isLogAllowed(LogLevel.DEBUG.num)) {
            Log.d(tag, message!!)
        }
    }

    fun d(tag: String?, message: String?, throwable: Throwable?) {
        if (isLogAllowed(LogLevel.DEBUG.num)) {
            Log.d(tag, message, throwable)
        }
    }

    actual fun i(tag: String, message: String) {
        if (isLogAllowed(LogLevel.INFO.num)) {
            Log.i(tag, message!!)
        }
    }

    fun i(tag: String?, message: String?, throwable: Throwable?) {
        if (isLogAllowed(LogLevel.INFO.num)) {
            Log.i(tag, message, throwable)
        }
    }


    fun e(tag: String?, message: String?) {
        if (isLogAllowed(LogLevel.ERROR.num)) {
            Log.e(tag, message!!)
        }
    }

    actual fun e(tag: String, message: String, throwable: Throwable?) {
        if (isLogAllowed(LogLevel.ERROR.num)) {
            Log.e(tag, message, throwable)
        }
    }

    private fun isLogAllowed(level: Int): Boolean {
        return logLevel.num <= level
    }
}