package app.suprsend.base

import app.suprsend.SuprSendDatabase.Companion.Schema
import app.suprsend.config.ConfigHelper
import app.suprsend.database.SSDatabaseWrapper
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import kotlin.test.BeforeTest
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.double
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import org.junit.Assert.assertEquals

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

    @BeforeTest
    fun start() {
        clean()
    }

    protected fun clean() {
        println("clean")
        deleteAllEvents()
        ConfigHelper.deleteAll()
    }

    fun deleteAllEvents() {
        println("deleteAllEvents")
        SdkCreator.eventLocalDatasource.deleteAll()
    }

    // Todo : all datatypes are not verified
    protected fun getTestProductData(): MutableMap<String, Any> {
        return mutableMapOf(
            "Product ID" to "P1",
            "Product Available" to true,
            "Product Price" to 43.1,
            "Product Quantity" to 10,
            "Product Sold" to 9999999999999L
        )
    }

    protected fun verifyProductProperties(propertiesPayload: JsonObject) {
        assertEquals("P1", propertiesPayload.get("Product ID")!!.jsonPrimitive.content)
        assertEquals(true, propertiesPayload.get("Product Available")!!.jsonPrimitive.boolean)
        assertEquals(43.1, propertiesPayload.get("Product Price")!!.jsonPrimitive.double, 0.0)
        assertEquals(10, propertiesPayload.get("Product Quantity")!!.jsonPrimitive.int)
        assertEquals(9999999999999L, propertiesPayload.get("Product Sold")!!.jsonPrimitive.long)
    }
}
