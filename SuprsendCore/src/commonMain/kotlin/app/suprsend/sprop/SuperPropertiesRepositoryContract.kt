package app.suprsend.sprop

import kotlinx.serialization.json.JsonObject

internal interface SuperPropertiesRepositoryContract {
    fun add(key: String, value: Any)
    fun add(properties: JsonObject)
    fun getAll(): JsonObject
    fun remove(key: String)
    fun removeAll()
}
