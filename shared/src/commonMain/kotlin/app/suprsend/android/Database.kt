package app.suprsend.android

import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.db.SqlDriver

internal class Database {

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

internal inline fun <T> databaseScope(block: SuprSendDatabase.() -> T) = block(globalDatabase.get()?.suprSendDatabase!!)
