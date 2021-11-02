package app.suprsend.android.event

import app.suprsend.android.base.SdkCreator
import app.suprsend.android.base.database
import app.suprsend.android.database.DBConversion
import kotlinx.datetime.Clock

internal class EventLocalDatasource : EventDataSourceContract {

    private val queries = database.get()!!.suprSendDatabase.eventTableQueries

    override fun track(eventModel: EventModel, isDirty: Boolean) {
        queries.track(
            id = eventModel.id,
            model = eventModel,
            isDirty = DBConversion.booleanToLong(isDirty),
            timeStamp = Clock.System.now().toEpochMilliseconds()
        )
    }

    override fun getEvents(limit: Long, isDirty: Boolean): List<EventModel> {
        return queries
            .getTrackedEvents(isDirty = DBConversion.booleanToLong(isDirty), limit = limit)
            .executeAsList()
            .filter { item -> item.model != null }
            .map { item -> item.model!!.copy(id = item.id) }
    }

    override fun delete(ids: List<String>) {
        queries.delete(ids)
    }
}
