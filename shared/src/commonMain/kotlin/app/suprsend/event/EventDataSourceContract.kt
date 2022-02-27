package app.suprsend.event

internal interface EventDataSourceContract {
    fun track(eventModel: EventModel, isDirty: Boolean = true)
    fun getEvents(limit: Long, isDirty: Boolean = true): List<EventModel>
    fun delete(ids: List<String>)
    fun deleteAll()
}
