package app.suprsend

import app.suprsend.base.SdkIosCreator

actual class Information actual constructor() {
    actual fun getDefaultProperties(): String {
        return SdkIosCreator.deviceInfo.getDeviceInfoProperties()
    }
}
