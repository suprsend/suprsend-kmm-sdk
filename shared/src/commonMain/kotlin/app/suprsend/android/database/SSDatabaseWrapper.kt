package app.suprsend.android.database

import app.suprsend.android.ConfigTable
import app.suprsend.android.EventTable
import app.suprsend.android.SuprSendDatabase
import app.suprsend.android.base.SdkCreator
import app.suprsend.android.config.ConfigModel
import app.suprsend.android.event.EventModel
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

internal inline fun <T> databaseScope(block: SuprSendDatabase.() -> T) = block(SdkCreator.database.get()?.suprSendDatabase!!)
