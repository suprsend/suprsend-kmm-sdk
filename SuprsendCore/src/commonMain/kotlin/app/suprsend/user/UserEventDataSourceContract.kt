package app.suprsend.user

import app.suprsend.event.EventModel

interface UserEventDataSourceContract {
    fun track(eventModel: EventModel, isDirty: Boolean = true)
    fun getEvents(limit: Long, isDirty: Boolean = true): List<EventModel>
    fun delete(ids: List<String>)
}
