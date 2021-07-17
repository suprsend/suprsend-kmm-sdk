package app.suprsend.android.base

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.telephony.TelephonyManager

internal class NetworkInfo(
    private val context: Context
) {

    fun getNetworkOperatorName(): String {
        return (context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager)?.networkOperator ?: UNKNOWN
    }

    @SuppressLint("MissingPermission")
    fun isConnected(): Boolean {
        return if (PackageManager.PERMISSION_GRANTED == context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE)) {
            val networkInfo = (context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)?.activeNetworkInfo
            networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected
        } else false
    }

    companion object {
        const val UNKNOWN = "UNKNOWN"
    }
}