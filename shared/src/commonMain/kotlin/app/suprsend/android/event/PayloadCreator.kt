package app.suprsend.android.event

import app.suprsend.android.SSApiInternal
import app.suprsend.android.base.Logger
import app.suprsend.android.base.SSConstants
import app.suprsend.android.base.addUpdateJsoObject
import app.suprsend.android.base.uuid
import app.suprsend.android.user.api.UserApiInternalImpl
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
        val identifyPayload = buildJsonObject {
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
        Logger.i(SSApiInternal.TAG, "identity : $identifiedId $identifyPayload")
        return identifyPayload
    }

    fun buildTrackEventPayload(
        eventName: String,
        distinctId: String,
        superProperties: JsonObject,
        defaultProperties: JsonObject,
        userProperties: JsonObject?,
        apiKey: String = SSApiInternal.apiKey
    ): JsonObject {

        var finalProperties = defaultProperties

        // Add super properties
        if (superProperties.isNotEmpty())
            finalProperties = finalProperties.addUpdateJsoObject(superProperties)

        // Add user properties
        if (userProperties?.isNotEmpty() == true)
            finalProperties = finalProperties.addUpdateJsoObject(userProperties)

        val eventPayload = buildJsonObject {
            addCommonEventProperties()
            put(SSConstants.EVENT, JsonPrimitive(eventName))
            put(SSConstants.DISTINCT_ID, JsonPrimitive(distinctId))
            put(SSConstants.ENV, JsonPrimitive(apiKey))

            put(SSConstants.PROPERTIES, finalProperties)
        }
        Logger.i(SSApiInternal.TAG, "Event Payload : $eventName $userProperties $eventPayload")
        return eventPayload
    }

    /**
     * Supported operator are
     * set,unset,set_once,add,append,remove
     */
    fun buildUserOperatorPayload(
        distinctId: String,
        setProperties: JsonElement,
        operator: String,
        apiKey: String = SSApiInternal.apiKey
    ): JsonObject {
        val operatorPayload = buildJsonObject {
            addCommonEventProperties()
            put(SSConstants.DISTINCT_ID, JsonPrimitive(distinctId))
            put(SSConstants.ENV, JsonPrimitive(apiKey))
            put(
                operator,
                setProperties
            )
        }
        Logger.i(UserApiInternalImpl.TAG, "Operator Payload : $operator $setProperties $operatorPayload")
        return operatorPayload
    }
}

private fun JsonObjectBuilder.addCommonEventProperties() {
    put(SSConstants.INSERT_ID, JsonPrimitive(uuid()))
    put(SSConstants.TIME, JsonPrimitive(Clock.System.now().toEpochMilliseconds()))
}
