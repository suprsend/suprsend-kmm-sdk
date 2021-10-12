package app.suprsend.android

import app.suprsend.android.base.SdkAndroidCreator

actual class Information actual constructor() {
    actual fun getDefaultProperties(): String {
        return SdkAndroidCreator.deviceInfo.getDeviceInfoProperties().toString()
    }
}
