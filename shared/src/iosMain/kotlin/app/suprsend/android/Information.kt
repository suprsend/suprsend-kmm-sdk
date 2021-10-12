package app.suprsend.android

import app.suprsend.android.base.SdkIosCreator

actual class Information actual constructor() {
    actual fun getDefaultProperties(): String {
        return SdkIosCreator.deviceInfo.getDeviceInfoProperties()
    }
}