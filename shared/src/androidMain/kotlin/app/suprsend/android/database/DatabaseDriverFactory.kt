package app.suprsend.android.database

import app.suprsend.android.SuprSendAndroidConfig
import app.suprsend.android.SuprSendDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory {

    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(SuprSendDatabase.Schema, SuprSendAndroidConfig.suprSendAndroidConfig.context, getDatabaseName())
    }

    actual fun getDatabaseName(): String {
        return "suprsend.db"
    }

}