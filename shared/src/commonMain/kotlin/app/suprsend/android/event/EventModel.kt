package app.suprsend.android.event

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonElement

@Serializable
data class EventModel(
    val value: JsonElement? = null,
    @Transient
    val id: Long? = null
)

//Todo - Export database schema before release