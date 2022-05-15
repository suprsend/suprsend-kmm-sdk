package app.suprsend.base

//import platform.CoreTelephony.CTRadioAccessTechnologyCDMA1x
//import platform.CoreTelephony.CTRadioAccessTechnologyCDMAEVDORev0
//import platform.CoreTelephony.CTRadioAccessTechnologyCDMAEVDORevA
//import platform.CoreTelephony.CTRadioAccessTechnologyCDMAEVDORevB
//import platform.CoreTelephony.CTRadioAccessTechnologyEdge
//import platform.CoreTelephony.CTRadioAccessTechnologyGPRS
//import platform.CoreTelephony.CTRadioAccessTechnologyHSDPA
//import platform.CoreTelephony.CTRadioAccessTechnologyHSUPA
//import platform.CoreTelephony.CTRadioAccessTechnologyLTE
//import platform.CoreTelephony.CTRadioAccessTechnologyWCDMA
//import platform.CoreTelephony.CTRadioAccessTechnologyeHRPD
//import platform.CoreTelephony.CTTelephonyNetworkInfo
import cocoapods.Reachability.Reachability

internal class NetworkInfo {

    fun getNetworkType(): NetworkType {
        val connection = Reachability.reachabilityForInternetConnection()
        return when {
            connection?.isReachableViaWiFi() == true -> NetworkType.WIFI
            connection?.isReachableViaWWAN() == true -> NetworkType.CELLULAR
            else -> NetworkType.UNKNOWN
        }
//        when (CTTelephonyNetworkInfo().currentRadioAccessTechnology) {
//            CTRadioAccessTechnologyGPRS,
//            CTRadioAccessTechnologyEdge,
//            CTRadioAccessTechnologyCDMA1x -> NetworkType.G2
//            CTRadioAccessTechnologyWCDMA,
//            CTRadioAccessTechnologyHSDPA,
//            CTRadioAccessTechnologyHSUPA,
//            CTRadioAccessTechnologyCDMAEVDORev0,
//            CTRadioAccessTechnologyCDMAEVDORevA,
//            CTRadioAccessTechnologyCDMAEVDORevB,
//            CTRadioAccessTechnologyeHRPD -> NetworkType.G3
//            CTRadioAccessTechnologyLTE -> NetworkType.G4
//        }
//        return NetworkType.UNKNOWN
    }

    fun isConnected(): Boolean {
        return Reachability.reachabilityForInternetConnection()?.isReachable() == true
    }
}

enum class NetworkType(val readableName: String) {
    WIFI("wifi"),
    CELLULAR("cellular"),
    G2("2G"),
    G3("3G"),
    G4("4G"),
    G5("5G"),
    UNKNOWN("-")
}
