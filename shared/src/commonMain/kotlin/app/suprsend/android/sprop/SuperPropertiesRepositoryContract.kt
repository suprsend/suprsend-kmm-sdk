package app.suprsend.android.sprop

import kotlinx.serialization.json.JsonObject

internal interface SuperPropertiesRepositoryContract {
    fun add(key: String, value: Any)
    fun add(properties: JsonObject)
    fun remove(key: String)
    fun getAll(): JsonObject
}