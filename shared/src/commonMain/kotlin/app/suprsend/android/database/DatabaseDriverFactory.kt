package app.suprsend.android.database

import com.squareup.sqldelight.db.SqlDriver
import kotlin.native.concurrent.SharedImmutable
import kotlinx.serialization.json.Json

expect class DatabaseDriverFactory {
    internal fun createDriver(): SqlDriver
    internal fun getDatabaseName(): String
}

@SharedImmutable
internal val format = Json {
    ignoreUnknownKeys = true
    isLenient = true
    prettyPrint = true
    allowStructuredMapKeys = true
}
