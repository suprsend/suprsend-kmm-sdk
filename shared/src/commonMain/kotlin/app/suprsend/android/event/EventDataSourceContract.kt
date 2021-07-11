package app.suprsend.android.event

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement

internal interface EventDataSourceContract {
    fun track(value: JsonElement?, isDirty: Boolean = true)
    fun getEvents(limit: Long, isDirty: Boolean = true): Flow<List<EventModel>>
}