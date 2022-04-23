package app.suprsend

import app.suprsend.base.SdkAndroidCreator

actual class Information actual constructor() {
    actual fun getDefaultProperties(): String {
        return SdkAndroidCreator.deviceInfo.getDeviceInfoProperties().toString()
    }
}
