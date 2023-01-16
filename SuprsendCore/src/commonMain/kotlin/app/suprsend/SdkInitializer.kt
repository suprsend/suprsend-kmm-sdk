package app.suprsend

import app.suprsend.base.Logger
import app.suprsend.base.database
import app.suprsend.base.network
import app.suprsend.database.DatabaseDriverFactory
import app.suprsend.database.SSDatabaseWrapper
import app.suprsend.network.httpClientEngine
/* ktlint-disable no-wildcard-imports */
import io.ktor.client.*
/* ktlint-enable no-wildcard-imports */

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
        val db = SSDatabaseWrapper(databaseDriverFactory)
        database.set(db)
    }

    private fun initializeNetworking() {
        val httpClient = HttpClient(engine = httpClientEngine)
        network.set(httpClient)
    }
}
