package app.suprsend.user.api

import app.suprsend.SSApiInternal
import app.suprsend.base.BaseDatabase
import app.suprsend.base.SSConstants
import app.suprsend.base.SdkCreator
import app.suprsend.user.SSInternalUser
import kotlinx.serialization.json.double
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

internal class SSInternalUserTest : BaseDatabase() {

    @Test
    fun testSetOperator() {

        // Todo : Cover all cases
        SSInternalUser.set("Abc", "Def")

        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        val payload = eventsList.first().value.jsonObject
        val propertiesPayload = payload.get(SSConstants.SET)!!.jsonObject

        assertEquals(1, eventsList.size)
        assertTrue(payload.containsKey(SSConstants.SET))
        assertEquals(1, propertiesPayload.size)
        assertEquals("Def", propertiesPayload["Abc"]!!.jsonPrimitive.content)
    }

    @Test
    fun testSetReservedOperator() {
        SSInternalUser.set("\$abc", "123")
        var eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        assertEquals(0, eventsList.size)

        SSInternalUser.set("ss_abc", "123")
        eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        assertEquals(0, eventsList.size)
    }

    @Test
    fun testSetJsonOperator() {

        val operator = SSConstants.SET
        SSInternalUser.set(getTestProductData())

        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        val payload = eventsList.first().value.jsonObject
        val propertiesPayload = payload.get(operator)!!.jsonObject

        assertEquals(1, eventsList.size)
        assertTrue(payload.containsKey(operator))
        assertEquals(5, propertiesPayload.size)
        verifyProductProperties(propertiesPayload)
    }

    @Test
    fun testSetJsonReservedOperator() {
        SSInternalUser.set(
            mapOf(
                "\$abc" to "123",
                "ss_abc" to "123"
            )
        )
        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        assertEquals(0, eventsList.size)
    }

    @Test
    fun testSetOnceOperator() {

        val operator = SSConstants.SET_ONCE
        SSInternalUser.setOnce("Abc", "Def")

        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        val payload = eventsList.first().value.jsonObject
        val propertiesPayload = payload.get(operator)!!.jsonObject

        assertEquals(1, eventsList.size)
        assertTrue(payload.containsKey(operator))
        assertEquals(1, propertiesPayload.size)
        assertEquals("Def", propertiesPayload.get("Abc")!!.jsonPrimitive.content)
    }

    @Test
    fun testSetOnceReservedOperator() {
        SSInternalUser.setOnce("\$abc", "123")
        var eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        assertEquals(0, eventsList.size)

        SSInternalUser.setOnce("ss_abc", "123")
        eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        assertEquals(0, eventsList.size)
    }

    @Test
    fun testSetOnceJsonOperator() {

        val operator = SSConstants.SET_ONCE
        SSInternalUser.setOnce(
            mapOf(
                "Abc" to "Def"
            )
        )

        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        val payload = eventsList.first().value.jsonObject
        val propertiesPayload = payload.get(operator)!!.jsonObject

        assertEquals(1, eventsList.size)
        assertTrue(payload.containsKey(operator))
        assertEquals(1, propertiesPayload.size)
        assertEquals("Def", propertiesPayload.get("Abc")!!.jsonPrimitive.content)
    }

    @Test
    fun testSetOnceJsonReservedOperator() {
        SSInternalUser.setOnce(mapOf(
            "\$abc" to "123",
            "ss_abc" to "123"
        ))
        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        assertEquals(0, eventsList.size)
    }

    @Test
    fun testIncrementOperator() {

        val operator = SSConstants.ADD

        // Todo : Increment float, double
        SSInternalUser.increment("abc", 1)

        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        val payload = eventsList.first().value.jsonObject
        val propertiesPayload = payload.get(operator)!!.jsonObject

        assertEquals(1, eventsList.size)
        assertTrue(payload.containsKey(operator))
        assertEquals(1, propertiesPayload.size)
        assertEquals(1, propertiesPayload.get("abc")!!.jsonPrimitive.int)
    }

    @Test
    fun testIncrementReservedOperator() {
        SSInternalUser.increment("\$abc", 123)
        var eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        assertEquals(0, eventsList.size)

        SSInternalUser.increment("ss_abc", 123)
        eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        assertEquals(0, eventsList.size)
    }

    @Test
    fun testIncrementJsonOperator() {

        val operator = SSConstants.ADD

        val testData: Map<String, Number> = mutableMapOf(
            "Product Price" to 43.1,
            "Product Quantity" to 10,
            "Product Sold" to 9999999999999L
        )

        SSInternalUser.increment(testData)

        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        val payload = eventsList.first().value.jsonObject
        val propertiesPayload = payload.get(operator)!!.jsonObject

        assertEquals(1, eventsList.size)
        assertTrue(payload.containsKey(operator))
        assertEquals(3, propertiesPayload.size)
        assertEquals(43.1, propertiesPayload.get("Product Price")!!.jsonPrimitive.double, 0.0)
        assertEquals(10, propertiesPayload.get("Product Quantity")!!.jsonPrimitive.int)
        assertEquals(9999999999999L, propertiesPayload.get("Product Sold")!!.jsonPrimitive.long)
    }

    @Test
    fun testIncrementJsonReservedOperator() {
        SSInternalUser.increment(
            mapOf(
                "\$abc" to 123,
                "ss_abc" to 123
            )
        )
        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        assertEquals(0, eventsList.size)
    }

    @Test
    fun testAppendOperator() {

        SSInternalUser.append("Abc", "Def")

        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        val payload = eventsList.first().value.jsonObject
        val propertiesPayload = payload.get(SSConstants.APPEND)!!.jsonObject

        assertEquals(1, eventsList.size)
        assertTrue(payload.containsKey(SSConstants.APPEND))
        assertEquals(1, propertiesPayload.size)
        assertEquals("Def", propertiesPayload.get("Abc")!!.jsonPrimitive.content)
    }

    @Test
    fun testAppendReservedOperator() {
        SSInternalUser.append("\$abc", "123")
        var eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        assertEquals(0, eventsList.size)

        SSInternalUser.append("ss_abc", "123")
        eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        assertEquals(0, eventsList.size)
    }

    @Test
    fun testAppendJsonOperator() {

        val operator = SSConstants.APPEND

        SSInternalUser.append(getTestProductData())

        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        val payload = eventsList.first().value.jsonObject
        val propertiesPayload = payload.get(operator)!!.jsonObject

        assertEquals(1, eventsList.size)
        assertTrue(payload.containsKey(operator))
        assertEquals(5, propertiesPayload.size)
        verifyProductProperties(propertiesPayload)
    }

    @Test
    fun testAppendJsonReservedOperator() {
        SSInternalUser.append(mapOf(
            "\$abc" to "123",
            "ss_abc" to "123"
        ))
        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        assertEquals(0, eventsList.size)
    }

    @Test
    fun testRemoveOperator() {

        val operator = SSConstants.REMOVE

        SSInternalUser.remove("Abc", "Def")

        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        val payload = eventsList.first().value.jsonObject
        val propertiesPayload = payload.get(operator)!!.jsonObject

        assertEquals(1, eventsList.size)
        assertTrue(payload.containsKey(operator))
        assertEquals(1, propertiesPayload.size)
        assertEquals("Def", propertiesPayload.get("Abc")!!.jsonPrimitive.content)
    }

    @Test
    fun testRemoveReservedOperator() {
        SSInternalUser.remove("\$abc", "123")
        var eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        assertEquals(0, eventsList.size)

        SSInternalUser.remove("ss_abc", "123")
        eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        assertEquals(0, eventsList.size)
    }

    @Test
    fun testRemoveJsonOperator() {

        val operator = SSConstants.REMOVE

        SSInternalUser.remove(getTestProductData())

        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        val payload = eventsList.first().value.jsonObject
        val propertiesPayload = payload.get(operator)!!.jsonObject

        assertEquals(1, eventsList.size)
        assertTrue(payload.containsKey(operator))
        assertEquals(5, propertiesPayload.size)
        verifyProductProperties(propertiesPayload)
    }

    @Test
    fun testRemoveJsonReservedOperator() {
        SSInternalUser.remove(mapOf(
            "\$abc" to "123",
            "ss_abc" to "123"
        ))
        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        assertEquals(0, eventsList.size)
    }

    @Test
    fun testUnSetOperator() {

        val operator = SSConstants.UNSET

        SSInternalUser.unSet("Product Name")

        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        val payload = eventsList.first().value.jsonObject
        val propertiesPayload = payload.get(operator)!!.jsonArray

        assertEquals(1, eventsList.size)
        assertTrue(payload.containsKey(operator))
        assertEquals(1, propertiesPayload.size)
        assertEquals("Product Name", propertiesPayload.get(0)!!.jsonPrimitive.content)
    }

    @Test
    fun testUnSetListOperator() {

        val operator = SSConstants.UNSET

        SSInternalUser.unSet(listOf("P1", "P2"))

        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        val payload = eventsList.first().value.jsonObject
        val propertiesPayload = payload.get(operator)!!.jsonArray

        assertEquals(1, eventsList.size)
        assertTrue(payload.containsKey(operator))
        assertEquals(2, propertiesPayload.size)
        assertEquals("P1", propertiesPayload.get(0).jsonPrimitive.content)
        assertEquals("P2", propertiesPayload.get(1).jsonPrimitive.content)
    }

    @Test
    fun testSetEmailOperator() {

        val testValue = "abc@abc.com"
        val operator = SSConstants.APPEND

        SSInternalUser.setEmail(testValue)

        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        val payload = eventsList.first().value.jsonObject
        val propertiesPayload = payload.get(operator)!!.jsonObject

        assertEquals(1, eventsList.size)
        assertTrue(payload.containsKey(operator))
        assertEquals(1, propertiesPayload.size)
        assertEquals(testValue, propertiesPayload.get(SSConstants.EMAIL)!!.jsonPrimitive.content)
    }

    @Test
    fun testUnSetEmailOperator() {

        val testValue = "abc@abc.com"
        val operator = SSConstants.REMOVE

        SSInternalUser.unSetEmail(testValue)

        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        val payload = eventsList.first().value.jsonObject
        val propertiesPayload = payload.get(operator)!!.jsonObject

        assertEquals(1, eventsList.size)
        assertTrue(payload.containsKey(operator))
        assertEquals(1, propertiesPayload.size)
        assertEquals(testValue, propertiesPayload.get(SSConstants.EMAIL)!!.jsonPrimitive.content)
    }

    @Test
    fun testSetSmsOperator() {

        val testValue = "+919999999999"
        val operator = SSConstants.APPEND

        SSInternalUser.setSms(testValue)

        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        val payload = eventsList.first().value.jsonObject
        val propertiesPayload = payload.get(operator)!!.jsonObject

        assertEquals(1, eventsList.size)
        assertTrue(payload.containsKey(operator))
        assertEquals(1, propertiesPayload.size)
        assertEquals(testValue, propertiesPayload.get(SSConstants.SMS)!!.jsonPrimitive.content)
    }

    @Test
    fun testUnSetSmsOperator() {

        val testValue = "+919999999999"
        val operator = SSConstants.REMOVE

        SSInternalUser.unSetSms(testValue)

        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        val payload = eventsList.first().value.jsonObject
        val propertiesPayload = payload.get(operator)!!.jsonObject

        assertEquals(1, eventsList.size)
        assertTrue(payload.containsKey(operator))
        assertEquals(1, propertiesPayload.size)
        assertEquals(testValue, propertiesPayload.get(SSConstants.SMS)!!.jsonPrimitive.content)
    }

    @Test
    fun testSetWhatsAppOperator() {

        val testValue = "+919999999999"
        val operator = SSConstants.APPEND

        SSInternalUser.setWhatsApp(testValue)

        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        val payload = eventsList.first().value.jsonObject
        val propertiesPayload = payload.get(operator)!!.jsonObject

        assertEquals(1, eventsList.size)
        assertTrue(payload.containsKey(operator))
        assertEquals(1, propertiesPayload.size)
        assertEquals(testValue, propertiesPayload.get(SSConstants.WHATS_APP)!!.jsonPrimitive.content)
    }

    @Test
    fun testUnSetWhatsAppOperator() {

        val testValue = "+919999999999"
        val operator = SSConstants.REMOVE

        SSInternalUser.unSetWhatsApp(testValue)

        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        val payload = eventsList.first().value.jsonObject
        val propertiesPayload = payload.get(operator)!!.jsonObject

        assertEquals(1, eventsList.size)
        assertTrue(payload.containsKey(operator))
        assertEquals(1, propertiesPayload.size)
        assertEquals(testValue, propertiesPayload.get(SSConstants.WHATS_APP)!!.jsonPrimitive.content)
    }

    @Test
    fun testSetIOSPushOperator() {

        SSApiInternal.setDeviceId("D1")
        val testValue = "Token1"
        val operator = SSConstants.APPEND

        SSInternalUser.setIOSPush(testValue)

        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        val payload = eventsList.first().value.jsonObject
        val propertiesPayload = payload.get(operator)!!.jsonObject

        assertEquals(1, eventsList.size)
        assertTrue(payload.containsKey(operator))
        assertEquals(3, propertiesPayload.size)
        assertEquals(testValue, propertiesPayload.get(SSConstants.PUSH_IOS_TOKEN)!!.jsonPrimitive.content)
        assertEquals(SSConstants.PUSH_VENDOR_APNS, propertiesPayload.get(SSConstants.PUSH_VENDOR)!!.jsonPrimitive.content)
        assertEquals("D1", propertiesPayload.get(SSConstants.DEVICE_ID)!!.jsonPrimitive.content)
    }

    @Test
    fun testUnsetIOSPushOperator() {

        SSApiInternal.setDeviceId("D1")
        val testValue = "Token1"
        val operator = SSConstants.REMOVE

        SSInternalUser.unSetIOSPush(testValue)

        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        val payload = eventsList.first().value.jsonObject
        val propertiesPayload = payload.get(operator)!!.jsonObject

        assertEquals(1, eventsList.size)
        assertTrue(payload.containsKey(operator))
        assertEquals(3, propertiesPayload.size)
        assertEquals(testValue, propertiesPayload.get(SSConstants.PUSH_IOS_TOKEN)!!.jsonPrimitive.content)
        assertEquals(SSConstants.PUSH_VENDOR_APNS, propertiesPayload.get(SSConstants.PUSH_VENDOR)!!.jsonPrimitive.content)
        assertEquals("D1", propertiesPayload.get(SSConstants.DEVICE_ID)!!.jsonPrimitive.content)
    }
}
