package app.suprsend.database

import app.suprsend.SuprSendDatabase
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver

public actual class DatabaseDriverFactory {
    public actual fun createDriver(): SqlDriver = NativeSqliteDriver(SuprSendDatabase.Schema, getDatabaseName())

    actual fun getDatabaseName(): String {
        return "suprsendsdk.db"
    }
}
