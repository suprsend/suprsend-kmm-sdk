package app.suprsend.android.user

import app.suprsend.android.event.EventModel
import kotlinx.serialization.json.JsonElement

interface UserEventDataSourceContract {
    fun track(value: JsonElement?, isDirty: Boolean = true)
    fun getEvents(limit: Long, isDirty: Boolean = true): List<EventModel>
    fun delete(ids:List<Long>)
}