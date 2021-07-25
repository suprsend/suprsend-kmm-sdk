package app.suprsend.android.database

import app.suprsend.android.base.AndroidCreator
import app.suprsend.android.SuprSendDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory {

    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(SuprSendDatabase.Schema, AndroidCreator.context, getDatabaseName())
    }

    actual fun getDatabaseName(): String {
        return "suprsendsdk.db"
    }

}