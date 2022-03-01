package app.suprsend.base

import app.suprsend.database.json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.jsonObject

internal fun String?.toKotlinJsonObject(): JsonObject {
    return try {
        if (this.isNullOrBlank())
            return JsonObject(mutableMapOf())
        json.parseToJsonElement(this).jsonObject
    } catch (e: Exception) {
        JsonObject(mutableMapOf())
    }
}

internal fun JsonObject.filterSSReservedKeys(): JsonObject {
    val receivedJo = this
    if (size == 0)
        return this
    return buildJsonObject {
        receivedJo.forEach { (key, jsonElement) ->
            when {
                key.isInValidKey() -> {
                    Logger.i(SSConstants.TAG_VALIDATION, "Key should not start with $ or ss_ this events property will not be sent to suprsend - Key - $key")
                }
                else -> {
                    put(key, jsonElement)
                }
            }
        }
    }
}

internal fun String.isInValidKey(): Boolean {
    return contains("$") || startsWith("ss_")
}

internal fun JsonElement?.addUpdateJsoObject(updateJsonObject: JsonObject): JsonElement? {
    return if (this is JsonObject)
        this.addUpdateJsoObject(updateJsonObject)
    else this
}

internal fun JsonObject?.addUpdateJsoObject(updateJsonObject: JsonObject): JsonObject {
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

internal fun Map<String, Any>.toJsonObject(): JsonObject {
    return buildJsonObject {
        forEach { (key, value) ->
            value.convertToJsonPrimitive(key)?.let { jp ->
                put(key, jp)
            }
        }
    }
}

internal fun JsonObject.addCreateNewJO(key: String, value: Any): JsonObject {
    val valueJP = value.convertToJsonPrimitive(key)
    valueJP ?: return this
    return JsonObject(this + (key to valueJP))
}

internal fun Any.convertToJsonPrimitive(key: String): JsonPrimitive? {
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

internal fun getRandomString(length: Int): String {
    val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length)
        .map { allowedChars.random() }
        .joinToString("")
}

fun CharSequence?.isValidEmail() = !isNullOrEmpty() && "^[^@]+@[^@]+\\.[^@]+\$".toRegex().matches(this.toString())
