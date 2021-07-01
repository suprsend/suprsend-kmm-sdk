package app.suprsend.android.database

import com.squareup.sqldelight.db.SqlDriver
import kotlinx.serialization.json.Json
import kotlin.native.concurrent.SharedImmutable

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
