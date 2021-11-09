package app.suprsend.event

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonElement

@Serializable
data class EventModel(
    val value: JsonElement,
    @Transient
    val id: String = ""
)

// Todo - Export database schema before release
