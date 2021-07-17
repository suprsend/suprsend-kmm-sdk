package app.suprsend.android.base

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject

fun String?.toKotlinJsonObject(): JsonObject {
    return try {
        if (this.isNullOrBlank())
            return JsonObject(mutableMapOf())
        Json.parseToJsonElement(this).jsonObject
    } catch (e: Exception) {
        JsonObject(mutableMapOf())
    }
}

fun JsonElement?.addUpdateJsoObject(updateJsonObject: JsonObject): JsonElement? {
    return if (this is JsonObject)
        this.addUpdateJsoObject(updateJsonObject)
    else this
}

fun JsonObject?.addUpdateJsoObject(updateJsonObject: JsonObject): JsonObject {
    this ?: return updateJsonObject
    return JsonObject(
        toMutableMap()
            .apply {
                updateJsonObject.forEach { (key, je) ->
                    put(key, je)
                }
            }
    )
}

fun Map<String, Any>.toJsonObject(): JsonObject {
    return buildJsonObject {
        forEach { (key, value) ->
            value.convertToJsonPrimitive(key)?.let { jp ->
                put(key, jp)
            }
        }
    }
}

fun JsonObject.addCreateNewJO(key: String, value: Any): JsonObject {
    val valueJP = value.convertToJsonPrimitive(key)
    valueJP ?: return this
    return JsonObject(this + (key to valueJP))
}

fun Any.convertToJsonPrimitive(key: String): JsonPrimitive? {
    val valuePrimitive = when (this) {
        is String -> JsonPrimitive(this)
        is Number -> JsonPrimitive(this)
        is Boolean -> JsonPrimitive(this)
        else -> null
    }

    if (valuePrimitive == null) {
        Logger.e("user", "$key is ignored : only primitive values are accepted")
    }
    return valuePrimitive
}


fun getRandomString(length: Int): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}

