package app.suprsend.android.base

import android.util.Log

actual class LoggerKMM {

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
            Log.i(tag, message)
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
            Log.e(tag, throwable?.message ?: message, throwable)
        }
    }

    private fun isLogAllowed(level: Int): Boolean {
        return logLevel.get()?.num ?: LogLevel.OFF.num <= level
    }
}
