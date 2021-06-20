package app.suprsend.android.database

import com.squareup.sqldelight.db.SqlDriver

public expect class DatabaseDriverFactory {
    internal fun createDriver(): SqlDriver
    internal fun getDatabaseName():String
}