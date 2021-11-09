package app.suprsend.base

import android.annotation.SuppressLint
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import app.suprsend.BuildConfig
import org.json.JSONObject

internal class DeviceInfo {
    fun getDeviceInfoProperties(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("\$app_version_string", versionName())
        jsonObject.put("\$app_build_number", versionCode())
        jsonObject.put("\$os", os())
        jsonObject.put("\$manufacturer", manufacturer())
        jsonObject.put("\$brand", brand())
        jsonObject.put("\$model", model())
        jsonObject.put("\$deviceId", getDeviceId())
        jsonObject.put("\$ss_sdk_version", BuildConfig.SS_SDK_VERSION_NAME)

        val networkInfo = SdkAndroidCreator.networkInfo
        jsonObject.put("\$network", networkInfo.getNetworkType().readableName)
        jsonObject.put("\$connected", networkInfo.isConnected().toString())
        return jsonObject
    }

    fun getDeviceWidthPixel(): Int {
        return SdkAndroidCreator.context.resources.displayMetrics.widthPixels
    }

    fun getDeviceHeightPixels(): Int {
        return SdkAndroidCreator.context.resources.displayMetrics.heightPixels
    }

    @SuppressLint("HardwareIds")
    fun getDeviceId(): String {
        return Settings.Secure.getString(SdkAndroidCreator.context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    private fun versionName(): String {
        return try {
            val info: PackageInfo = SdkAndroidCreator.context.packageManager.getPackageInfo(SdkAndroidCreator.context.packageName, 0)
            info.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            Logger.i("init", "Unable to get app version details")
            UNKNOWN
        }
    }

    private fun versionCode(): String {
        return try {
            val info: PackageInfo = SdkAndroidCreator.context.packageManager.getPackageInfo(SdkAndroidCreator.context.packageName, 0)
            info.versionCode.toString()
        } catch (e: PackageManager.NameNotFoundException) {
            Logger.i("init", "Unable to get app version details")
            UNKNOWN
        }
    }

    private fun brand(): String {
        return if (Build.BRAND == null) UNKNOWN else Build.BRAND
    }

    private fun manufacturer(): String {
        return if (Build.MANUFACTURER == null) UNKNOWN else Build.MANUFACTURER
    }

    private fun model(): String {
        return if (Build.MODEL == null) UNKNOWN else Build.MODEL
    }

    private fun os(): String {
        return "android"
    }

    companion object {
        const val UNKNOWN = "UNKNOWN"
    }
}
