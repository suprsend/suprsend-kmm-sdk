package app.suprsend.android.event

import app.suprsend.android.SuprSendApiInternal
import app.suprsend.android.base.addUpdateJsoObject
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

object PayloadCreator {

    fun buildIdentityEventPayload(
        identifiedId: String,
        anonymousId: String,
        apiKey: String = SuprSendApiInternal.apiKey
    ): JsonObject {
        return buildJsonObject {
            put("event", JsonPrimitive("\$identify"))
            put("ENV", JsonPrimitive(apiKey))
            put(
                "properties",
                buildJsonObject {
                    put("\$identified_id", JsonPrimitive(identifiedId))
                    put("\$anon_id", JsonPrimitive(anonymousId))
                }
            )
        }
    }

    fun buildTrackEventPayload(
        eventName: String,
        distinctId: String,
        superProperties: JsonObject,
        userProperties: JsonObject?,
        apiKey: String = SuprSendApiInternal.apiKey
    ): JsonObject {

        // Add super properties
        val finalPropertiesJsonObject = (userProperties ?: JsonObject(mutableMapOf())).addUpdateJsoObject(superProperties)

        return buildJsonObject {
            put("event", JsonPrimitive(eventName))
            put("distinct_id", JsonPrimitive(distinctId))
            put("ENV", JsonPrimitive(apiKey))
            put("properties", finalPropertiesJsonObject)
        }
    }

    /**
     * Supported operator are
     * set,unset,set_once,add,append,remove
     */
    fun buildUserOperatorPayload(
        distinctId: String,
        setProperties: JsonElement,
        operator: String,
        apiKey: String = SuprSendApiInternal.apiKey,
    ): JsonObject {
        return buildJsonObject {
            put("distinct_id", JsonPrimitive(distinctId))
            put("ENV", JsonPrimitive(apiKey))
            put(
                operator,
                setProperties
            )
        }
    }


}