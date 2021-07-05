package app.suprsend.android.event

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class EventRepository(
    private val eventLocalDatasource: EventLocalDatasource = EventLocalDatasource(),
    private val eventRemoteDatasource: EventRemoteDataSource = EventRemoteDataSource()
) {

    fun trackEvent(eventName: String, value: String) {
        eventLocalDatasource.track(
            name = eventName,
            value = value
        )
    }


    fun getDirtyEvents(limit: Long): Flow<List<EventModel>> {
        return eventLocalDatasource
            .getEvents(limit, true)
            .map { eventList ->
                eventList
                    .filter { it.event != null }
                    .map { it.event!! }
            }
    }
}