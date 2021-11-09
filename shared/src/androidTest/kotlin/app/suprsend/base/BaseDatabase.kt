package app.suprsend.base

import app.suprsend.SuprSendDatabase.Companion.Schema
import app.suprsend.database.SSDatabaseWrapper
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver

internal open class BaseDatabase {
    init {
        database.set(
            SSDatabaseWrapper(
                JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
                    Schema.create(this)
                }
            )
        )
    }
}
