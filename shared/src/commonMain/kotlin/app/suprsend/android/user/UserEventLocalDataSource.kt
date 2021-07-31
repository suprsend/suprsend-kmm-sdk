package app.suprsend.android.user

import app.suprsend.android.GLOBAL_SUPR_SEND_DATABASE_WRAPPER
import app.suprsend.android.database.DBConversion
import app.suprsend.android.event.EventModel

internal class UserEventLocalDataSource : UserEventDataSourceContract {

    private val queries = GLOBAL_SUPR_SEND_DATABASE_WRAPPER.get()!!.suprSendDatabase.userEventTableQueries

    override fun track(eventModel: EventModel, isDirty: Boolean) {
        queries.track(
            id = eventModel.id,
            model = eventModel,
            isDirty = DBConversion.booleanToLong(isDirty)
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
