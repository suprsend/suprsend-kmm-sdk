package app.suprsend.android.event

import kotlinx.serialization.Serializable

@Serializable
data class EventModel(
    val name: String,
    val value: String
)

//Todo - Export database schema before release