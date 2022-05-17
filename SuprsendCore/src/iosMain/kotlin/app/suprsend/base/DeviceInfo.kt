package app.suprsend.base

// import app.suprsend.BuildConfig
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import platform.Foundation.NSBundle
import platform.UIKit.UIDevice

internal class DeviceInfo {
    fun getDeviceInfoProperties(): String {
        return buildJsonObject {

            put("\$app_version_string", JsonPrimitive(versionName()))
            put("\$app_build_number", JsonPrimitive(versionCode()))
            put("\$os", JsonPrimitive(os()))
            put("\$os_version", JsonPrimitive(osVersion()))
            put("\$model", JsonPrimitive(model()))
            put(SSConstants.DEVICE_ID, JsonPrimitive(getDeviceId()))

            put("\$ss_sdk_version", JsonPrimitive("ios-rn/0.1.0Beta1"))

//            val networkInfo = SdkIosCreator.networkInfo
//            put("\$network", JsonPrimitive(networkInfo.getNetworkType().readableName))
//            put("\$connected", JsonPrimitive(networkInfo.isConnected().toString()))
        }.toString()
    }

//    fun getDeviceWidthPixel(): Int {
//        return SdkAndroidCreator.context.resources.displayMetrics.widthPixels
//    }
//
//    fun getDeviceHeightPixels(): Int {
//        return SdkAndroidCreator.context.resources.displayMetrics.heightPixels
//    }

    fun getDeviceId(): String {
        return UIDevice.currentDevice.identifierForVendor?.UUIDString ?: ""
    }

    private fun versionName(): String {
        return NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString")?.toString() ?: UNKNOWN
    }

    private fun versionCode(): String {
        return NSBundle.mainBundle.infoDictionary?.get("CFBundleVersion")?.toString() ?: UNKNOWN
    }

    private fun model(): String {
        return UIDevice.currentDevice.model
    }

    private fun os(): String {
        return UIDevice.currentDevice.systemName
    }

    private fun osVersion(): String {
        return UIDevice.currentDevice.systemVersion
    }

    companion object {
        const val UNKNOWN = "UNKNOWN"
    }
}
