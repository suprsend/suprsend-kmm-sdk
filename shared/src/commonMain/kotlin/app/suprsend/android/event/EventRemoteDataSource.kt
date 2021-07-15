package app.suprsend.android.event

import kotlinx.serialization.json.JsonElement

internal class EventRemoteDataSource : EventDataSourceContract {
    override fun track(value: JsonElement?, isDirty: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getEvents(limit: Long, isDirty: Boolean): List<EventModel> {
        TODO("Not yet implemented")
    }

    override fun delete(ids: List<Long>) {
        TODO("Not yet implemented")
    }

}