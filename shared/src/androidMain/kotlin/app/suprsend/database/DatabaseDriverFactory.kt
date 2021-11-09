package app.suprsend.database

import app.suprsend.SuprSendDatabase
import app.suprsend.base.SdkAndroidCreator
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

actual class DatabaseDriverFactory {

    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(SuprSendDatabase.Schema, SdkAndroidCreator.context, getDatabaseName())
    }

    actual fun getDatabaseName(): String {
        return "suprsendsdk.db"
    }
}
