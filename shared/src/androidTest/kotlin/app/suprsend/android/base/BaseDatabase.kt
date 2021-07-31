package app.suprsend.android.base

import app.suprsend.android.GLOBAL_SUPR_SEND_DATABASE_WRAPPER
import app.suprsend.android.SuprSendDatabase.Companion.Schema
import app.suprsend.android.database.SSDatabaseWrapper
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver

internal open class BaseDatabase {
    init {
        GLOBAL_SUPR_SEND_DATABASE_WRAPPER.set(
            SSDatabaseWrapper(
                JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
                    Schema.create(this)
                }
            )
        )
    }
}
