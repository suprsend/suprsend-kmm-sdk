package app.suprsend.android.config

import kotlinx.serialization.Serializable

@Serializable
data class ConfigModel(
    val key: String,
    val value: String
)