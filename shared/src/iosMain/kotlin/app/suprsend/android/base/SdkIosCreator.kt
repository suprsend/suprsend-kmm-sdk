package app.suprsend.android.base

@ThreadLocal
internal object SdkIosCreator {
    var flushing: Boolean = false
}