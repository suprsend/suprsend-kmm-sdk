package app.suprsend.android.database

import app.suprsend.android.ConfigTable
import app.suprsend.android.EventTable
import app.suprsend.android.GLOBAL_SUPR_SEND_DATABASE_WRAPPER
import app.suprsend.android.SuprSendDatabase
import app.suprsend.android.config.ConfigModel
import app.suprsend.android.event.EventModel
import com.squareup.sqldelight.db.SqlDriver

internal class SSDatabaseWrapper {

    val suprSendDatabase: SuprSendDatabase

    lateinit var driver: SqlDriver

    constructor(databaseDriver: SqlDriver) {
        suprSendDatabase = createDatabase(databaseDriver = databaseDriver)
    }

    constructor(databaseDriverFactory: DatabaseDriverFactory) {
        suprSendDatabase = createDatabase(databaseDriverFactory = databaseDriverFactory)
    }

    private fun createDatabase(databaseDriverFactory: DatabaseDriverFactory): SuprSendDatabase {
        val driver = databaseDriverFactory.createDriver()
        return createDatabase(driver)
    }

    private fun createDatabase(databaseDriver: SqlDriver): SuprSendDatabase {
        driver = databaseDriver

        return SuprSendDatabase(
            driver = driver,
            EventTableAdapter = EventTable.Adapter(
                modelAdapter = DataModelColumnAdapter(serializer = EventModel.serializer())
            ),
            ConfigTableAdapter = ConfigTable.Adapter(
                valueAdapter = DataModelColumnAdapter(serializer = ConfigModel.serializer())
            )
        )
    }
}

internal inline fun <T> databaseScope(block: SuprSendDatabase.() -> T) = block(GLOBAL_SUPR_SEND_DATABASE_WRAPPER.get()?.suprSendDatabase!!)
