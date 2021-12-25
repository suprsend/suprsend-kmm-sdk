package app.suprsend.base

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.double
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive

class KotlinExtensionTest {

    @Test
    fun testBlankString() {
        assertEquals("{}", "".toKotlinJsonObject().toString())
    }

    @Test
    fun testJsonProperties() {
        val jsonObject = """
            {
                "title": "Product Title",
                "quantity": 5,
                "price": 340.45
            }
        """.trimIndent().toKotlinJsonObject()

        assertEquals("Product Title", jsonObject["title"]?.jsonPrimitive?.contentOrNull)
        assertEquals(5, jsonObject["quantity"]?.jsonPrimitive?.intOrNull)
        assertEquals(340.45, jsonObject["price"]?.jsonPrimitive?.doubleOrNull)
    }

    @Test
    fun testPropertiesUpdate() {
        val jsonObject = """
            {
                "title": "Product Title",
                "quantity": 5,
                "price": 340.45
            }
        """.trimIndent().toKotlinJsonObject()

        assertEquals("Product Title", jsonObject["title"]?.jsonPrimitive?.contentOrNull)
        assertEquals(5, jsonObject["quantity"]?.jsonPrimitive?.intOrNull)
        assertEquals(340.45, jsonObject["price"]?.jsonPrimitive?.doubleOrNull)

        val updateJsonObject = """
            {
                "title": "Product Title 1",
                "quantity": 51,
                "price": 341.45,
                "discount": 721.45
            }
        """.trimIndent().toKotlinJsonObject()

        val newJsonObject = jsonObject.addUpdateJsoObject(updateJsonObject)

        assertEquals("Product Title 1", newJsonObject["title"]?.jsonPrimitive?.contentOrNull)
        assertEquals(51, newJsonObject["quantity"]?.jsonPrimitive?.intOrNull)
        assertEquals(341.45, newJsonObject["price"]?.jsonPrimitive?.doubleOrNull)
        assertEquals(721.45, newJsonObject["discount"]?.jsonPrimitive?.doubleOrNull)
    }

    @Test
    fun testAddCreateNewJO() {
        val jo = buildJsonObject {
            put("title", JsonPrimitive("Abc"))
        }
        assertEquals(
            jo.addCreateNewJO("price", 340),
            buildJsonObject {
                put("title", JsonPrimitive("Abc"))
                put("price", JsonPrimitive(340))
            }
        )
    }

    @Test
    fun testRandomFunctionLength() {
        assertEquals(1, getRandomString(1).length)
        assertEquals(10, getRandomString(10).length)
        assertEquals(100, getRandomString(100).length)
    }

    @Test
    fun testIfItFiltersDollarKeys() {
        val key = "\$"
        val sampleJson = """
            {
                "someKeyString": "Product Title 1",
                "someKeyInt": 1,
                "someKeyDouble": 1.2,
                "someKeyBoolean": false,
                "jO": { "someKeyString": "Product Title 1" },
                "ja": [{ "someKeyString": "Product Title 1"}],
                "${key}someKey": 51,
                "ss_someKey": 341.45
            }
        """.trimIndent().toKotlinJsonObject().filterSSReservedKeys()
        assertEquals(6, sampleJson.size)
        assertEquals("Product Title 1", sampleJson["someKeyString"]?.jsonPrimitive?.contentOrNull ?: "")
        assertEquals(1, sampleJson["someKeyInt"]?.jsonPrimitive?.int)
        assertEquals(1.2, sampleJson["someKeyDouble"]?.jsonPrimitive?.double)
        assertEquals(false, sampleJson["someKeyBoolean"]?.jsonPrimitive?.boolean)
    }
}
