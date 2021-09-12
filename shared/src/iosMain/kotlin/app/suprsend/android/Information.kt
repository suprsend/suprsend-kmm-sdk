package app.suprsend.android

import app.suprsend.android.base.SdkIosCreator

actual class Information actual constructor() {
    actual fun getDefaultProperties(): String = "{}"
    actual fun isFlushing(): Boolean = SdkIosCreator.flushing
    actual fun setFlushing(value: Boolean) {
        SdkIosCreator.flushing = value
    }
}