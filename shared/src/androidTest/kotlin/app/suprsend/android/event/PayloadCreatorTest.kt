package app.suprsend.android.event

import app.suprsend.android.base.toKotlinJsonObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject

class PayloadCreatorTest {

    @Test
    fun testIdentityEventPayload() {

        val eventName = "\$identify"
        val identifiedId = "\$identified_id"
        val anonId = "\$anon_id"
        val payload =
            """
                {
                  "event": "$eventName",
                  "ENV": "ENV_API_KEY",
                  "properties": {
                    "$identifiedId": "unique_id",
                    "$anonId": "old_anonymous_id"
                  }
                }
            """.trimIndent().toKotlinJsonObject()

        assertEquals(
            payload,
            PayloadCreator
                .buildIdentityEventPayload(
                    identifiedId = "unique_id",
                    anonymousId = "old_anonymous_id",
                    apiKey = "ENV_API_KEY"
                )
        )
    }

    @Test
    fun testTrackEventPayload() {
        val payload =
            """
                {
                  "event": "Product Delete",
                  "distinct_id": "13793",
                  "ENV": "ENV_API_KEY",
                  "properties": {
                    "Product Title": "Book 121",
                    "Quantity": 30,
                    "App Version": "11.3",
                    "App Build Number": "123",
                    "Brand": "New Model"
                  }
                }
            """.trimIndent().toKotlinJsonObject()

        assertEquals(
            payload,
            PayloadCreator
                .buildTrackEventPayload(
                    eventName = "Product Delete",
                    distinctId = "13793",
                    superProperties = buildJsonObject {
                        put("App Version", JsonPrimitive("11.3"))
                        put("App Build Number", JsonPrimitive("123"))
                        put("Brand", JsonPrimitive("New Model"))
                    },
                    userProperties = buildJsonObject {
                        put("Product Title", JsonPrimitive("Book 121"))
                        put("Quantity", JsonPrimitive(30))
                    },
                    apiKey = "ENV_API_KEY"
                )
        )
    }

    @Test
    fun testUserOperatorEventPayload() {
        val set = "\$set"

        val payload =
            """
                {
                  "distinct_id": "13793",
                  "ENV": "ENV_API_KEY",
                  "$set": {
                    "Privileged Customer": true,
                    "Bought Super Coin": 4567.87
                  }
                }
            """.trimIndent().toKotlinJsonObject()

        assertEquals(
            payload,
            PayloadCreator
                .buildUserOperatorPayload(
                    distinctId = "13793",
                    setProperties = buildJsonObject {
                        put("Privileged Customer", JsonPrimitive(true))
                        put("Bought Super Coin", JsonPrimitive(4567.87))
                    },
                    operator = "\$set",
                    apiKey = "ENV_API_KEY"
                )
        )
    }
}
