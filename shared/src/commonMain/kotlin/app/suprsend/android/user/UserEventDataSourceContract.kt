package app.suprsend.android.user

import app.suprsend.android.event.EventModel

interface UserEventDataSourceContract {
    fun track(eventModel: EventModel, isDirty: Boolean = true)
    fun getEvents(limit: Long, isDirty: Boolean = true): List<EventModel>
    fun delete(ids: List<String>)
}
