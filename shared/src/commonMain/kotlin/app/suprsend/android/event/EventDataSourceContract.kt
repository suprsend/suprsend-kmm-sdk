package app.suprsend.android.event

import kotlinx.serialization.json.JsonElement

internal interface EventDataSourceContract {
    fun track(value: JsonElement?, isDirty: Boolean = true)
    fun getEvents(limit: Long, isDirty: Boolean = true): List<EventModel>
    fun delete(ids:List<Long>)
}