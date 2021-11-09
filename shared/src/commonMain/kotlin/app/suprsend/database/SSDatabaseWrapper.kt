package app.suprsend.database

import app.suprsend.ConfigTable
import app.suprsend.EventTable
import app.suprsend.SuprSendDatabase
import app.suprsend.base.database
import app.suprsend.config.ConfigModel
import app.suprsend.event.EventModel
import com.squareup.sqldelight.db.SqlDriver

internal class SSDatabaseWrapper {

    val suprSendDatabase: SuprSendDatabase

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
        return SuprSendDatabase(
            driver = databaseDriver,
            EventTableAdapter = EventTable.Adapter(
                modelAdapter = DataModelColumnAdapter(serializer = EventModel.serializer())
            ),
            ConfigTableAdapter = ConfigTable.Adapter(
                valueAdapter = DataModelColumnAdapter(serializer = ConfigModel.serializer())
            )
        )
    }
}

internal inline fun <T> databaseScope(block: SuprSendDatabase.() -> T) = block(database.get()?.suprSendDatabase!!)
