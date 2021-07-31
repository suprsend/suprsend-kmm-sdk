package app.suprsend.android.base

internal object Logger {
    // Todo : Also pick value from network
    private val loggerKMM = LoggerKMM()

    var logLevel
        get() = loggerKMM.logLevel
        set(value) {
            loggerKMM.logLevel = value
        }

    fun i(tag: String, message: String) {
        loggerKMM.i(tag, message)
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        loggerKMM.e(tag, message, throwable)
    }
}
