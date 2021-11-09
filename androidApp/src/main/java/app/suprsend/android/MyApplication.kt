package app.suprsend.android

import android.app.Application
import app.suprsend.SSApi

class MyApplication : Application() {

    override fun onCreate() {
        SSApi.init(this)
        super.onCreate()
        initializeSdk()
    }

    private fun initializeSdk() {
        CommonAnalyticsHandler.initialize(applicationContext)
    }
}
