package app.suprsend.android.event

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.JsonElement

internal class EventRepository(
    private val eventLocalDatasource: EventLocalDatasource = EventLocalDatasource(),
    private val eventRemoteDatasource: EventRemoteDataSource = EventRemoteDataSource()
) : EventDataSourceContract {

    override fun track(value: JsonElement?, isDirty: Boolean) {
        eventLocalDatasource.track(
            value = value,
            isDirty = isDirty
        )
    }


    override fun getEvents(limit: Long, isDirty: Boolean): Flow<List<EventModel>> {
        return eventLocalDatasource
            .getEvents(limit, isDirty)
    }
}