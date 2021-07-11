package app.suprsend.android.event

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class EventModel(
    val value: JsonElement? = null
)

//Todo - Export database schema before release