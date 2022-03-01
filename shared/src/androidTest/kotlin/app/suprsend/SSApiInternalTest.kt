package app.suprsend

import app.suprsend.base.BaseDatabase
import app.suprsend.base.SSConstants
import app.suprsend.base.SdkCreator
import app.suprsend.event.EventLocalDatasource
import app.suprsend.user.UserLocalDatasource
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.double
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Assert
import org.junit.Test

internal class SSApiInternalTest : BaseDatabase() {

    @Test
    fun testIdentityWithoutPushRegistered() {
        SSApiInternal.identify("U1")
        Assert.assertEquals("U1", UserLocalDatasource().getIdentity())
        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        Assert.assertEquals(2, eventsList.size)
        Assert.assertEquals(SSConstants.IDENTIFY, eventsList[0].value.jsonObject.get(SSConstants.EVENT)!!.jsonPrimitive.content)
        Assert.assertEquals(SSConstants.S_EVENT_USER_LOGIN, eventsList[1].value.jsonObject.get(SSConstants.EVENT)!!.jsonPrimitive.content)
    }

    @Test
    fun testIgnoreIdentifyIfAlreadyIdentified() {
        //Initially identify got called
        SSApiInternal.identify("U1")
        Assert.assertEquals("U1", UserLocalDatasource().getIdentity())
        val eventLocalDatasource = SdkCreator.eventLocalDatasource

        //Cleaning just to fake like event got flush
        deleteAllEvents()

        //Again identify is called with same user id
        SSApiInternal.identify("U1")
        Assert.assertEquals("U1", UserLocalDatasource().getIdentity())
        val eventsList = eventLocalDatasource.getEvents(10)
        //Verify no events are generated as identify is ignored
        Assert.assertEquals(0, eventsList.size)
    }

    @Test
    fun testSetSuperPropertyKeyValue() {


        SSApiInternal.setSuperProperty("Product Name", "Cycle 123")
        SSApiInternal.track("ABC", null)

        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        val eventPayload = eventsList[0].value.jsonObject
        Assert.assertEquals("ABC", eventPayload.get(SSConstants.EVENT)!!.jsonPrimitive.content)
        Assert.assertEquals("Cycle 123", eventPayload.get(SSConstants.PROPERTIES)!!.jsonObject.get("Product Name")!!.jsonPrimitive.content)
    }

    @Test
    fun testSetSuperPropertiesJson() {


        SSApiInternal.setSuperProperties(mapOf(
            "Product Name 1" to "Cycle 1",
            "Product Name 2" to "Cycle 2"
        ))

        SSApiInternal.track("ABC", null)

        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        val eventPayload = eventsList[0].value.jsonObject
        Assert.assertEquals("ABC", eventPayload.get(SSConstants.EVENT)!!.jsonPrimitive.content)
        Assert.assertEquals("Cycle 1", eventPayload.get(SSConstants.PROPERTIES)!!.jsonObject["Product Name 1"]!!.jsonPrimitive.content)
        Assert.assertEquals("Cycle 2", eventPayload.get(SSConstants.PROPERTIES)!!.jsonObject["Product Name 2"]!!.jsonPrimitive.content)
    }


    @Test
    fun testRemoveSuperProperty() {


        SSApiInternal.setSuperProperties(
            mapOf(
                "Product Name 1" to "Cycle 1",
                "Product Name 2" to "Cycle 2"
            )
        )

        SSApiInternal.removeSuperProperty("Product Name 1")

        SSApiInternal.track("ABC", null)

        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        val eventPayload = eventsList[0].value.jsonObject
        Assert.assertEquals("ABC", eventPayload.get(SSConstants.EVENT)!!.jsonPrimitive.content)
        Assert.assertEquals(false, eventPayload.get(SSConstants.PROPERTIES)!!.jsonObject.containsKey("Product Name 1"))

    }

    @Test
    fun testTrackEvent() {


        SSApiInternal.track("ABC", null)

        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        val eventPayload = eventsList[0].value.jsonObject
        Assert.assertEquals("ABC", eventPayload.get(SSConstants.EVENT)!!.jsonPrimitive.content)
    }

    @Test
    fun testTrackEventWithDollar() {
        SSApiInternal.track("\$some_event_name", null)

        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        Assert.assertEquals(0, eventsList.size)
    }

    @Test
    fun testTrackEventWithPrefixSS() {
        SSApiInternal.track("ss_some_event_name", null)

        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        Assert.assertEquals(0, eventsList.size)
    }

    @Test
    fun testTrackEventWithPrefixSSInBetweenText() {
        SSApiInternal.track("business_event", null)

        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        Assert.assertEquals(1, eventsList.size)
    }

    @Test
    fun testTrackEventWithReservedKeys() {

        SSApiInternal.track(
            "ABC", mapOf(
                "Product ID" to 1,
                "Product Name" to "Abc Title",
                "Product Quantity" to 10,
                "Product Price" to 43.1,
                "\$abc" to 43.1,
                "\$Product Price" to 43.1,
            )
        )

        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)

        val eventPayload = eventsList[0].value.jsonObject

        val propertiesPayload = eventPayload.get(SSConstants.PROPERTIES)!!.jsonObject
        Assert.assertEquals("ABC", eventPayload.get(SSConstants.EVENT)!!.jsonPrimitive.content)
        Assert.assertEquals(4, propertiesPayload.size )
        Assert.assertEquals(1, propertiesPayload.get("Product ID")!!.jsonPrimitive.int)
        Assert.assertEquals("Abc Title", propertiesPayload.get("Product Name")!!.jsonPrimitive.content)
        Assert.assertEquals(10, propertiesPayload.get("Product Quantity")!!.jsonPrimitive.int)
        Assert.assertEquals(43.1, propertiesPayload.get("Product Price")!!.jsonPrimitive.double, 0.0)
    }

    @Test
    fun testTrackEventWithProperties() {

        SSApiInternal.track("ABC", propertiesMap =getTestProductData())

        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        val eventPayload = eventsList[0].value.jsonObject
        Assert.assertEquals("ABC", eventPayload.get(SSConstants.EVENT)!!.jsonPrimitive.content)

        val propertiesPayload = eventPayload.get(SSConstants.PROPERTIES)!!.jsonObject

        verifyProductProperties(propertiesPayload)
    }

    @Test
    fun testPurchaseMadeProperties() {

        SSApiInternal.purchaseMade(propertiesMap = getTestProductData())

        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        val eventPayload = eventsList[0].value.jsonObject

        Assert.assertEquals(1, eventsList.size)
        Assert.assertEquals(SSConstants.S_EVENT_PURCHASE_MADE, eventPayload.get(SSConstants.EVENT)!!.jsonPrimitive.content)

        val propertiesPayload = eventPayload.get(SSConstants.PROPERTIES)!!.jsonObject

        verifyProductProperties(propertiesPayload)
    }

    @Test
    fun testReset() {
        SSApiInternal.identify("U1")
        Assert.assertEquals("U1", UserLocalDatasource().getIdentity())

        SSApiInternal.reset()
        Assert.assertTrue("U1" != UserLocalDatasource().getIdentity())

        //Todo try using EvenLocalDatasourceDirectly here
        val eventsList = SdkCreator.eventLocalDatasource.getEvents(10)
        Assert.assertEquals(3, eventsList.size)
        Assert.assertEquals(SSConstants.S_EVENT_USER_LOGOUT, eventsList[2].value.jsonObject.get(SSConstants.EVENT)!!.jsonPrimitive.content)
    }
}