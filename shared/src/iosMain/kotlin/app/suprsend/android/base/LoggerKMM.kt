package app.suprsend.android.base

actual class LoggerKMM {

    fun v(tag: String, message: String) {
        if (isLogAllowed(LogLevel.VERBOSE.num)) {
            printLog(tag, message)
        }
    }

    fun v(tag: String, message: String, throwable: Throwable) {
        if (isLogAllowed(LogLevel.VERBOSE.num)) {
            printLog(tag, message, throwable)
        }
    }

    fun d(tag: String, message: String) {
        if (isLogAllowed(LogLevel.DEBUG.num)) {
            printLog(tag, message)
        }
    }

    fun d(tag: String, message: String, throwable: Throwable) {
        if (isLogAllowed(LogLevel.DEBUG.num)) {
            printLog(tag, message, throwable)
        }
    }

    actual fun i(tag: String, message: String) {
        if (isLogAllowed(LogLevel.INFO.num)) {
            printLog(tag, message)
        }
    }

    fun i(tag: String, message: String, throwable: Throwable) {
        if (isLogAllowed(LogLevel.INFO.num)) {
            printLog(tag, message, throwable)
        }
    }

    fun e(tag: String, message: String) {
        if (isLogAllowed(LogLevel.ERROR.num)) {
            printLog(tag, message)
        }
    }

    actual fun e(tag: String, message: String, throwable: Throwable?) {
        if (isLogAllowed(LogLevel.ERROR.num)) {
            printLog(tag, message, throwable)
        }
    }

    private fun printLog(tag: String, message: String, exception: Throwable? = null) {
        println("$tag : $message")
        if (exception != null) {
            println(exception)
        }
    }

    private fun isLogAllowed(level: Int): Boolean {
        return logLevel.get()?.num ?: LogLevel.OFF.num <= level
    }
}
