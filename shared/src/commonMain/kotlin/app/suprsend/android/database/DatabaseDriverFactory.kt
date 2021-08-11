package app.suprsend.android.database

import com.squareup.sqldelight.db.SqlDriver
import kotlin.native.concurrent.SharedImmutable
import kotlinx.serialization.json.Json

expect class DatabaseDriverFactory {
    internal fun createDriver(): SqlDriver
    internal fun getDatabaseName(): String
}

@SharedImmutable
internal val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    prettyPrint = false
    allowStructuredMapKeys = true
}
