package app.suprsend.event

import app.suprsend.base.BaseDatabase
import kotlin.test.assertEquals
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.junit.Test

internal class EventLocalDatasourceTest : BaseDatabase() {

    private val eventLocalDatasource = EventLocalDatasource()

    @Test
    fun testInsertEvent() {
        eventLocalDatasource.track(
            eventModel = EventModel(
                value = buildJsonObject {
                    put("Id", JsonPrimitive("123"))
                },
                id = "1"
            ),
            isDirty = true
        )
        val events = eventLocalDatasource.getEvents(10, true)
        assertEquals(1, events.size)
        assertEquals("1", events.first().id)
        assertEquals("123", events.first().value.jsonObject.get("Id")!!.jsonPrimitive.content)
    }
}
