package app.suprsend.android.base

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import app.suprsend.android.BuildConfig
import org.json.JSONObject

internal class DeviceInfo(
    private val context: Context
) {
    fun getDeviceInfoProperties(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("\$app_version_string", versionName())
        jsonObject.put("\$app_build_number", versionCode())
        jsonObject.put("\$brand", brand())
        jsonObject.put("\$manufacturer", manufacturer())
        jsonObject.put("\$model", model())
        jsonObject.put("\$os", os())
        jsonObject.put("\$ss_sdk_version", BuildConfig.SS_SDK_VERSION_NAME)

        val networkInfo = NetworkInfo(context)
        jsonObject.put("\$carrier", networkInfo.getNetworkOperatorName())
        jsonObject.put("\$connected", networkInfo.isConnected().toString())
        return jsonObject
    }

    private fun versionName(): String {
        return try {
            val info: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            info.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            Logger.i("init", "Unable to get app version details")
            UNKNOWN
        }
    }

    private fun versionCode(): String {
        return try {
            val info: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
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