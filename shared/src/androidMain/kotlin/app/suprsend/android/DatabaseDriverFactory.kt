package app.suprsend.android

import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(SuprSendDatabase.Schema, SuprSendAndroidConfig.suprSendAndroidConfig.context, DATABASE_NAME)
    }
}
