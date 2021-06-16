package app.suprsend.android

import com.squareup.sqldelight.ColumnAdapter
import com.squareup.sqldelight.db.SqlDriver

internal class Database {
    val database: SuprSend
    lateinit var driver: SqlDriver

    constructor(databaseDriver: SqlDriver) {
        database = createDatabase(databaseDriver)
    }

    constructor(databaseDriverFactory: DatabaseDriverFactory) {
        database = createDatabase(databaseDriverFactory)
    }

    private fun createDatabase(databaseDriverFactory: DatabaseDriverFactory): SuprSend {
        val driver = databaseDriverFactory.createDriver()
        return createDatabase(driver)
    }

    private fun createDatabase(databaseDriver: SqlDriver): SuprSend {
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

        return SuprSend(
            driver,
            UserModel.Adapter(
                companyAdapter = coordinateAdapter
            )
        )
    }
}

internal inline fun <T> databaseScope(block: SuprSend.() -> T) = block(globalDatabase.get()?.database!!)
