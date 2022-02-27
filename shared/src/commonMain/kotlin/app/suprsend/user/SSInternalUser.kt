package app.suprsend.user

import app.suprsend.SSApiInternal
import app.suprsend.base.Logger
import app.suprsend.base.SSConstants
import app.suprsend.base.SdkCreator
import app.suprsend.base.convertToJsonPrimitive
import app.suprsend.base.filterSSReservedKeys
import app.suprsend.base.isInValidKey
import app.suprsend.base.isMobileNumberValid
import app.suprsend.base.isValidEmail
import app.suprsend.base.toJsonObject
import app.suprsend.base.uuid
import app.suprsend.event.EventModel
import app.suprsend.event.PayloadCreator
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject

internal object SSInternalUser {

    fun set(key: String, value: Any) {
        val valuePrimitive = value.convertToJsonPrimitive(key)
        valuePrimitive ?: return
        filterAndStoreOperatorPayload(
            properties = buildJsonObject {
                put(key, valuePrimitive)
            },
            operator = SSConstants.SET
        )
    }

    fun set(properties: Map<String, Any>) {
        filterAndStoreOperatorPayload(
            properties = JsonObject(properties.toJsonObject()),
            operator = SSConstants.SET
        )
    }

    fun setOnce(key: String, value: Any) {
        val valuePrimitive = value.convertToJsonPrimitive(key)
        valuePrimitive ?: return
        filterAndStoreOperatorPayload(
            properties = buildJsonObject {
                put(key, valuePrimitive)
            },
            operator = SSConstants.SET_ONCE
        )
    }

    fun setOnce(properties: Map<String, Any>) {
        filterAndStoreOperatorPayload(
            properties = JsonObject(properties.toJsonObject()),
            operator = SSConstants.SET_ONCE
        )
    }

    fun increment(key: String, value: Number) {
        val valuePrimitive = value.convertToJsonPrimitive(key)
        valuePrimitive ?: return
        filterAndStoreOperatorPayload(
            properties = buildJsonObject {
                put(key, valuePrimitive)
            },
            operator = SSConstants.ADD
        )
    }

    fun increment(properties: Map<String, Any>) {
        filterAndStoreOperatorPayload(
            properties = properties.toJsonObject(),
            operator = SSConstants.ADD
        )
    }

    fun append(key: String, value: Any) {
        val valuePrimitive = value.convertToJsonPrimitive(key)
        valuePrimitive ?: return
        filterAndStoreOperatorPayload(
            properties = buildJsonObject {
                put(key, valuePrimitive)
            },
            operator = SSConstants.APPEND
        )
    }

    fun append(properties: Map<String, Any>) {
        filterAndStoreOperatorPayload(
            properties = properties.toJsonObject(),
            operator = SSConstants.APPEND
        )
    }

    fun remove(key: String, value: Any) {
        val valuePrimitive = value.convertToJsonPrimitive(key)
        valuePrimitive ?: return
        filterAndStoreOperatorPayload(
            properties = buildJsonObject {
                put(key, valuePrimitive)
            },
            operator = SSConstants.REMOVE
        )
    }

    fun remove(properties: Map<String, Any>) {
        filterAndStoreOperatorPayload(
            properties = properties.toJsonObject(),
            operator = SSConstants.REMOVE
        )
    }

    fun unSet(key: String) {
        unSet(listOf(key))
    }

    fun unSet(keys: List<String>) {
        val filteredValidKeys = keys.filter { key -> !key.isInValidKey() }
        if (filteredValidKeys.isNotEmpty()) {
            storeOperatorPayload(
                setPropertiesArray = buildJsonArray {
                        keys.forEach { key ->
                            add(key)
                        }
                    },
                operator = SSConstants.UNSET
            )
        } else {
            Logger.i(SSConstants.TAG_VALIDATION, "Payload ignored as none keys are valid after filtering reserved keys for unset operator $keys")
        }
    }

    fun setEmail(email: String) {
        val valuePrimitive = email.convertToJsonPrimitive(SSConstants.EMAIL)
        valuePrimitive ?: return
        if (email.isValidEmail()) {
            storeOperatorPayload(
                properties = buildJsonObject { put(SSConstants.EMAIL, valuePrimitive) },
                operator = SSConstants.APPEND
            )
        } else {
            Logger.e(SSConstants.TAG_VALIDATION, "Email is not valid : $email")
        }
        append(SSConstants.EMAIL, email)
    }

    fun unSetEmail(email: String) {
        val valuePrimitive = email.convertToJsonPrimitive(SSConstants.EMAIL)
        valuePrimitive ?: return
        if (email.isValidEmail()) {
            storeOperatorPayload(
                properties = buildJsonObject { put(SSConstants.EMAIL, valuePrimitive) },
                operator = SSConstants.REMOVE
            )
        } else {
            Logger.e(SSConstants.TAG_VALIDATION, "Email is not valid : $email")
        }
    }

    fun setSms(mobile: String) {
        val valuePrimitive = mobile.convertToJsonPrimitive(SSConstants.SMS)
        valuePrimitive ?: return
        if (isMobileNumberValid(mobile)) {
            storeOperatorPayload(
                properties = buildJsonObject { put(SSConstants.SMS, valuePrimitive) },
                operator = SSConstants.APPEND
            )
        } else {
            Logger.e("serror", "Mobile number is not valid : $mobile")
        }
    }

    fun unSetSms(mobile: String) {
        val valuePrimitive = mobile.convertToJsonPrimitive(SSConstants.SMS)
        valuePrimitive ?: return
        if (isMobileNumberValid(mobile)) {
            storeOperatorPayload(
                properties = buildJsonObject { put(SSConstants.SMS, valuePrimitive) },
                operator = SSConstants.REMOVE
            )
        } else {
            Logger.e("serror", "Mobile number is not valid : $mobile")
        }
    }

    fun setWhatsApp(mobile: String) {
        val valuePrimitive = mobile.convertToJsonPrimitive(SSConstants.SMS)
        valuePrimitive ?: return
        if (isMobileNumberValid(mobile)) {
            storeOperatorPayload(
                properties = buildJsonObject { put(SSConstants.WHATS_APP, valuePrimitive) },
                operator = SSConstants.APPEND
            )
        } else {
            Logger.e("serror", "Mobile number is not valid : $mobile")
        }
    }

    fun unSetWhatsApp(mobile: String) {
        val valuePrimitive = mobile.convertToJsonPrimitive(SSConstants.SMS)
        valuePrimitive ?: return
        if (isMobileNumberValid(mobile)) {
            storeOperatorPayload(
                properties = buildJsonObject { put(SSConstants.WHATS_APP, valuePrimitive) },
                operator = SSConstants.REMOVE
            )
        } else {
            Logger.e("serror", "Mobile number is not valid : $mobile")
        }
    }

    fun setIOSPush(newToken: String) {
        val oldToken = SSApiInternal.getIOSToken()
        if (oldToken != newToken) {
            SSApiInternal.setIOSToken(newToken)
        }
        storeOperatorPayload(
            properties = buildJsonObject {
                put(SSConstants.PUSH_IOS_TOKEN, JsonPrimitive(newToken))
                put(SSConstants.PUSH_VENDOR, JsonPrimitive(SSConstants.PUSH_VENDOR_APNS))
                put(SSConstants.DEVICE_ID, JsonPrimitive(SSApiInternal.getDeviceID()))
            },
            operator = SSConstants.APPEND
        )
    }

    fun unSetIOSPush(token: String) {
        storeOperatorPayload(
            properties = buildJsonObject {
                put(SSConstants.PUSH_IOS_TOKEN, JsonPrimitive(token))
                put(SSConstants.PUSH_VENDOR, JsonPrimitive(SSConstants.PUSH_VENDOR_APNS))
                put(SSConstants.DEVICE_ID, JsonPrimitive(SSApiInternal.getDeviceID()))
            },
            operator = SSConstants.REMOVE
        )
    }

    fun storeOperatorPayload(properties: JsonObject? = null, operator: String, setPropertiesArray: JsonArray? = null) {

        val userLocalDatasource = UserLocalDatasource()
        SdkCreator
            .eventLocalDatasource
            .track(
                EventModel(
                    value = PayloadCreator
                        .buildUserOperatorPayload(
                            distinctId = userLocalDatasource.getIdentity(),
                            setProperties = properties,
                            setPropertiesArray = setPropertiesArray,
                            operator = operator
                        ),
                    id = uuid()
                )
            )
    }

    private fun filterAndStoreOperatorPayload(properties: JsonObject, operator: String) {

        val filteredProperties = properties.filterSSReservedKeys()

        if (filteredProperties.isEmpty()) {
            return
        }
        storeOperatorPayload(properties = filteredProperties, operator = operator)
    }
}
