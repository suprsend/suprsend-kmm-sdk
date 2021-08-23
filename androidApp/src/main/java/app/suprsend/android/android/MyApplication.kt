package app.suprsend.android.android

import android.app.Application

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeSdk()
    }

    private fun initializeSdk() {
        CommonAnalyticsHandler.initialize(applicationContext)
    }
}
