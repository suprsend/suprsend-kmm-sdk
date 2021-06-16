package app.suprsend.android

import com.squareup.sqldelight.internal.Atomic
import io.ktor.client.HttpClient
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import kotlin.native.concurrent.SharedImmutable

@SharedImmutable
internal val globalDatabase: Atomic<Database?> = Atomic(null)

@SharedImmutable
internal val globalNetwork: Atomic<HttpClient?> = Atomic(null)

object SuprSendSdk {

    fun initialize(databaseDriverFactory: DatabaseDriverFactory) {
        initializeDatabase(databaseDriverFactory)
        initializeNetworking()
    }

    private fun initializeNetworking() {
        val httpClient = HttpClient(engine = engine) {
            install(Logging) {
                logger = io.ktor.client.features.logging.Logger.DEFAULT
                level = LogLevel.HEADERS
            }
        }
        globalNetwork.set(httpClient)
    }

    private fun initializeDatabase(databaseDriverFactory: DatabaseDriverFactory) {
        val database = Database(databaseDriverFactory)
        globalDatabase.set(database)
    }
}