package app.suprsend.base

internal object SdkIosCreator {

    val deviceInfo: DeviceInfo by lazy { DeviceInfo() }
    val networkInfo: NetworkInfo by lazy { NetworkInfo() }
}
