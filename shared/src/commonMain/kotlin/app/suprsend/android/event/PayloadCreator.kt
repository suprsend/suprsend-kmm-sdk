package app.suprsend.android.event

import app.suprsend.android.SSApiInternal
import app.suprsend.android.base.SSConstants
import app.suprsend.android.base.addUpdateJsoObject
import app.suprsend.android.base.uuid
import kotlinx.datetime.Clock
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonObjectBuilder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

object PayloadCreator {

    fun buildIdentityEventPayload(
        identifiedId: String,
        anonymousId: String,
        apiKey: String = SSApiInternal.apiKey
    ): JsonObject {
        return buildJsonObject {
            addCommonEventProperties()
            put(SSConstants.EVENT, JsonPrimitive(SSConstants.IDENTIFY))
            put(SSConstants.ENV, JsonPrimitive(apiKey))
            put(
                SSConstants.PROPERTIES,
                buildJsonObject {
                    put(SSConstants.IDENTIFIED_ID, JsonPrimitive(identifiedId))
                    put(SSConstants.ANONYMOUS_ID, JsonPrimitive(anonymousId))
                }
            )
        }
    }

    fun buildTrackEventPayload(
        eventName: String,
        distinctId: String,
        superProperties: JsonObject,
        userProperties: JsonObject?,
        apiKey: String = SSApiInternal.apiKey
    ): JsonObject {

        // Add super properties
        val finalPropertiesJsonObject = (userProperties ?: JsonObject(mutableMapOf())).addUpdateJsoObject(superProperties)

        return buildJsonObject {
            addCommonEventProperties()
            put(SSConstants.EVENT, JsonPrimitive(eventName))
            put(SSConstants.DISTINCT_ID, JsonPrimitive(distinctId))
            put(SSConstants.ENV, JsonPrimitive(apiKey))

            put(SSConstants.PROPERTIES, finalPropertiesJsonObject)
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
        apiKey: String = SSApiInternal.apiKey,
    ): JsonObject {
        return buildJsonObject {
            addCommonEventProperties()
            put(SSConstants.DISTINCT_ID, JsonPrimitive(distinctId))
            put(SSConstants.ENV, JsonPrimitive(apiKey))
            put(
                operator,
                setProperties
            )
        }
    }
}

private fun JsonObjectBuilder.addCommonEventProperties() {
    put(SSConstants.INSERT_ID, JsonPrimitive(uuid()))
    put(SSConstants.TIME, JsonPrimitive(Clock.System.now().toEpochMilliseconds()))
}