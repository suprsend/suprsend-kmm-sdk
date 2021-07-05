package app.suprsend.android.event

import app.suprsend.android.EventTable
import app.suprsend.android.base.GLOBAL_SUPR_SEND_DATABASE_WRAPPER
import app.suprsend.android.database.DBConversion
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow

internal class EventLocalDatasource {

    private val queries = GLOBAL_SUPR_SEND_DATABASE_WRAPPER.get()!!.suprSendDatabase.eventModelQueries

    internal fun track(name: String, value: String, isDirty: Boolean = true) {
        queries.track(
            id = null,
            event = EventModel(name, value),
            isDirty = DBConversion.booleanToLong(isDirty)
        )
    }

    internal fun getEvents(limit: Long, isDirty: Boolean = true): Flow<List<EventTable>> =
        queries
            .getTrackedEvents(isDirty = DBConversion.booleanToLong(isDirty), limit = limit)
            .asFlow()
            .mapToList()

}