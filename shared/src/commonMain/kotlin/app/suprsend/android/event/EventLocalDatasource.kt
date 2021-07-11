package app.suprsend.android.event

import app.suprsend.android.GLOBAL_SUPR_SEND_DATABASE_WRAPPER
import app.suprsend.android.database.DBConversion
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.JsonElement

internal class EventLocalDatasource : EventDataSourceContract {

    private val queries = GLOBAL_SUPR_SEND_DATABASE_WRAPPER.get()!!.suprSendDatabase.eventTableQueries

    override fun track(value: JsonElement?, isDirty: Boolean) {
        queries.track(
            id = null,
            model = EventModel(value),
            isDirty = DBConversion.booleanToLong(isDirty)
        )
    }

    override fun getEvents(limit: Long, isDirty: Boolean): Flow<List<EventModel>> {
        return queries
            .getTrackedEvents(isDirty = DBConversion.booleanToLong(isDirty), limit = limit)
            .asFlow()
            .mapToList()
            .map { it.filter { item -> item.model != null }.map { item -> item.model!! } }
    }

}