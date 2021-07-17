package app.suprsend.android.base

import app.suprsend.android.GLOBAL_SUPR_SEND_DATABASE_WRAPPER
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import app.suprsend.android.SuprSendDatabase.Companion.Schema
import app.suprsend.android.database.SSDatabaseWrapper

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