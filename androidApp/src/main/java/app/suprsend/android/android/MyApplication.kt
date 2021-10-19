package app.suprsend.android.android

import android.app.Application
import app.suprsend.android.SSApi

class MyApplication : Application() {

    override fun onCreate() {
        SSApi.init(this, BuildConfig.SS_API_BASE_URL)
        super.onCreate()
        initializeSdk()
    }

    private fun initializeSdk() {
        CommonAnalyticsHandler.initialize(applicationContext)
    }
}
