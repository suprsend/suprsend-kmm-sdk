package app.suprsend.android

import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(SuprSend.Schema, AndroidConfig.androidConfig.context, DATABASE_NAME)
    }
}
