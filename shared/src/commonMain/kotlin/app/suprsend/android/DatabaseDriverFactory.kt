package app.suprsend.android

import com.squareup.sqldelight.db.SqlDriver

internal const val DATABASE_NAME = "suprsend.db"

public expect class DatabaseDriverFactory {
    internal fun createDriver(): SqlDriver
}
