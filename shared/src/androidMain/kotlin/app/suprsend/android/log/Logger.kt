package app.suprsend.android.log

import android.util.Log

object Logger {

    const val VERBOSE = 101
    const val DEBUG = 102
    const val INFO = 103
    const val ERROR = 104
    const val OFF = Int.MAX_VALUE

    var logLevel: Int = OFF

    fun v(tag: String?, message: String?) {
        if (isLogAllowed(VERBOSE)) {
            Log.v(tag, message!!)
        }
    }

    fun v(tag: String?, message: String?, throwable: Throwable?) {
        if (isLogAllowed(VERBOSE)) {
            Log.v(tag, message, throwable)
        }
    }

    fun d(tag: String?, message: String?) {
        if (isLogAllowed(DEBUG)) {
            Log.d(tag, message!!)
        }
    }

    fun d(tag: String?, message: String?, throwable: Throwable?) {
        if (isLogAllowed(DEBUG)) {
            Log.d(tag, message, throwable)
        }
    }

    fun i(tag: String?, message: String?) {
        if (isLogAllowed(INFO)) {
            Log.i(tag, message!!)
        }
    }

    fun i(tag: String?, message: String?, throwable: Throwable?) {
        if (isLogAllowed(INFO)) {
            Log.i(tag, message, throwable)
        }
    }


    fun e(tag: String?, message: String?) {
        if (isLogAllowed(ERROR)) {
            Log.e(tag, message!!)
        }
    }

    fun e(tag: String?, message: String?, throwable: Throwable?) {
        if (isLogAllowed(ERROR)) {
            Log.e(tag, message, throwable)
        }
    }

    private fun isLogAllowed(level: Int): Boolean {
        return logLevel <= level
    }

}