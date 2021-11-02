package app.suprsend.android

import app.suprsend.android.base.Logger
import app.suprsend.android.base.SdkCreator
import app.suprsend.android.database.DatabaseDriverFactory
import app.suprsend.android.database.SSDatabaseWrapper
import app.suprsend.android.network.httpClientEngine
import io.ktor.client.*

internal object SdkInitializer {

    fun initialize(databaseDriverFactory: DatabaseDriverFactory) {
        try {
            initializeDatabase(databaseDriverFactory)
            initializeNetworking()
        } catch (e: Exception) {
            Logger.e(SSApiInternal.TAG, "", e)
        }
    }

    private fun initializeDatabase(databaseDriverFactory: DatabaseDriverFactory) {
        val database = SSDatabaseWrapper(databaseDriverFactory)
        SdkCreator.database.set(database)
    }

    private fun initializeNetworking() {
        val httpClient = HttpClient(engine = httpClientEngine)
        SdkCreator.network.set(httpClient)
    }
}
