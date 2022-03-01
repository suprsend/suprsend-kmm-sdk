package app.suprsend.sprop

import app.suprsend.base.addCreateNewJO
import app.suprsend.base.addUpdateJsoObject
import app.suprsend.base.convertToJsonPrimitive
import app.suprsend.base.toKotlinJsonObject
import app.suprsend.config.ConfigHelper
import kotlinx.serialization.json.JsonObject

internal class SuperPropertiesLocalDataSource : SuperPropertiesRepositoryContract {

    override fun add(key: String, value: Any) {
        val valueJP = value.convertToJsonPrimitive(key)
        valueJP ?: return
        val updatedJsonObject = getAll().addCreateNewJO(key, value)
        saveValues(updatedJsonObject)
    }

    override fun add(properties: JsonObject) {
        if (properties.isEmpty()) {
            return
        }
        val updatedProperties = getAll().addUpdateJsoObject(properties)
        saveValues(updatedProperties)
    }

    override fun remove(key: String) {
        val result = getAll().filter { it.key != key }
        saveValues(JsonObject(result))
    }

    override fun getAll(): JsonObject {
        val superPropertiesJsonString = ConfigHelper.get(CONFIG_KEY)
        return superPropertiesJsonString.toKotlinJsonObject()
    }

    private fun saveValues(updatedJsonObject: JsonObject) {
        ConfigHelper.addOrUpdate(CONFIG_KEY, updatedJsonObject.toString())
    }

    companion object {
        const val CONFIG_KEY = "super_properties"
    }
}
