package app.suprsend.android.base

expect class LoggerKMM constructor() {
    fun i(tag: String, message: String)
    fun e(tag: String, message: String, throwable: Throwable?)
}

enum class LogLevel(val num: Int) {
    VERBOSE(101),
    DEBUG(102),
    INFO(103),
    ERROR(104),
    OFF(Int.MAX_VALUE)
}
