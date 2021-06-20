package app.suprsend.android.database

import app.suprsend.android.user.Company
import app.suprsend.android.SuprSendDatabase
import app.suprsend.android.UserModel
import app.suprsend.android.GLOBAL_SUPR_SEND_DATABASE_WRAPPER
import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.db.SqlDriver

internal class SuprSendDatabaseWrapper {

    val suprSendDatabase: SuprSendDatabase

    lateinit var driver: SqlDriver

    constructor(databaseDriver: SqlDriver) {
        suprSendDatabase = createDatabase(databaseDriver)
    }

    constructor(databaseDriverFactory: DatabaseDriverFactory) {
        suprSendDatabase = createDatabase(databaseDriverFactory)
    }

    private fun createDatabase(databaseDriverFactory: DatabaseDriverFactory): SuprSendDatabase {
        val driver = databaseDriverFactory.createDriver()
        return createDatabase(driver)
    }

    private fun createDatabase(databaseDriver: SqlDriver): SuprSendDatabase {
        driver = databaseDriver
        val coordinateAdapter = object : ColumnAdapter<Company, String> {
            override fun decode(databaseValue: String): Company {
                val split = databaseValue.split(":")
                return Company(split[0], split[1])
            }

            override fun encode(value: Company): String {
                return "${value.id}:${value.email}"
            }
        }

        return SuprSendDatabase(
            driver,
            UserModel.Adapter(
                companyAdapter = coordinateAdapter
            )
        )
    }
}

internal inline fun <T> databaseScope(block: SuprSendDatabase.() -> T) = block(GLOBAL_SUPR_SEND_DATABASE_WRAPPER.get()?.suprSendDatabase!!)
