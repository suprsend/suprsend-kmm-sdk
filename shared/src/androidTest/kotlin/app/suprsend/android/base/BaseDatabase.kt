package app.suprsend.android.base

import app.suprsend.android.SuprSendDatabase.Companion.Schema
import app.suprsend.android.database.SSDatabaseWrapper
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver

internal open class BaseDatabase {
    init {
        SdkCreator.database.set(
            SSDatabaseWrapper(
                JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
                    Schema.create(this)
                }
            )
        )
    }
}
