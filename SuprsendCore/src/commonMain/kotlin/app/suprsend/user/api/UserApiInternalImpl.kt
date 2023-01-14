package app.suprsend.user.api

import app.suprsend.SSApiInternal
import app.suprsend.base.Logger
import app.suprsend.base.PreferredLanguage
import app.suprsend.base.SSConstants
import app.suprsend.base.SdkCreator
import app.suprsend.base.convertToJsonPrimitive
import app.suprsend.base.filterSSReservedKeys
import app.suprsend.base.isInValidKey
import app.suprsend.base.isMobileNumberValid
import app.suprsend.base.singleThreadDispatcher
import app.suprsend.base.toKotlinJsonObject
import app.suprsend.base.uuid
import app.suprsend.coroutineExceptionHandler
import app.suprsend.event.EventModel
import app.suprsend.event.PayloadCreator
import app.suprsend.user.UserLocalDatasource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject

internal class UserApiInternalImpl : UserApiInternalContract {

    private val coroutineScope = CoroutineScope(SupervisorJob())

    override fun set(key: String, value: Any) {
        val valuePrimitive = value.convertToJsonPrimitive(key)
        valuePrimitive ?: return
        internalOperatorCall(
            buildJsonObject {
                put(key, valuePrimitive)
            },
            operator = SSConstants.SET
        )
    }

    override fun set(propertiesJson: String) {
        internalOperatorCall(
            propertiesJson.toKotlinJsonObject(),
            operator = SSConstants.SET
        )
    }

    override fun setOnce(key: String, value: Any) {
        val valuePrimitive = value.convertToJsonPrimitive(key)
        valuePrimitive ?: return
        internalOperatorCall(
            buildJsonObject {
                put(key, valuePrimitive)
            },
            operator = SSConstants.SET_ONCE
        )
    }

    override fun setOnce(propertiesJson: String) {
        internalOperatorCall(
            propertiesJson.toKotlinJsonObject(),
            operator = SSConstants.SET_ONCE
        )
    }

    override fun increment(key: String, value: Number) {
        val valuePrimitive = value.convertToJsonPrimitive(key)
        valuePrimitive ?: return
        internalOperatorCall(
            buildJsonObject {
                put(key, valuePrimitive)
            },
            operator = SSConstants.ADD
        )
    }

    override fun increment(propertiesJson: String) {
        internalOperatorCall(
            propertiesJson.toKotlinJsonObject(),
            operator = SSConstants.ADD
        )
    }

    override fun append(key: String, value: Any) {
        val valuePrimitive = value.convertToJsonPrimitive(key)
        valuePrimitive ?: return
        internalOperatorCall(
            buildJsonObject {
                put(key, valuePrimitive)
            },
            operator = SSConstants.APPEND
        )
    }

    override fun append(propertiesJson: String) {
        internalOperatorCall(
            propertiesJson.toKotlinJsonObject(),
            operator = SSConstants.APPEND
        )
    }

    override fun remove(key: String, value: Any) {
        val valuePrimitive = value.convertToJsonPrimitive(key)
        valuePrimitive ?: return
        internalOperatorCall(
            buildJsonObject {
                put(key, valuePrimitive)
            },
            operator = SSConstants.REMOVE
        )
    }

    override fun remove(propertiesJson: String) {
        internalOperatorCall(
            propertiesJson.toKotlinJsonObject(),
            operator = SSConstants.REMOVE
        )
    }

    override fun unSet(key: String) {
        unSet(listOf(key))
    }

    override fun unSet(keys: List<String>) {
        coroutineScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            val filteredValidKeys = keys.filter { key -> !key.isInValidKey() }
            if (filteredValidKeys.isNotEmpty()) {
                internalOperatorCallOp(
                    properties = buildJsonArray {
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
    }

    override fun setEmail(email: String) {
        coroutineScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            internalOperatorCallOp(
                properties = buildJsonObject {
                    put(SSConstants.EMAIL, JsonPrimitive(email))
                },
                operator = SSConstants.APPEND
            )
        }
    }

    override fun unSetEmail(email: String) {
        coroutineScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            internalOperatorCallOp(
                properties = buildJsonObject {
                    put(SSConstants.EMAIL, JsonPrimitive(email))
                },
                operator = SSConstants.REMOVE
            )
        }
    }

    override fun setSms(mobile: String) {
        if (isMobileNumberValid(mobile)) {
            coroutineScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
                internalOperatorCallOp(
                    properties = buildJsonObject {
                        put(SSConstants.SMS, JsonPrimitive(mobile))
                    },
                    operator = SSConstants.APPEND
                )
            }
        } else {
            Logger.e("serror", "Mobile number is not valid : $mobile")
        }
    }

    override fun unSetSms(mobile: String) {
        if (isMobileNumberValid(mobile)) {
            coroutineScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
                internalOperatorCallOp(
                    properties = buildJsonObject {
                        put(SSConstants.SMS, JsonPrimitive(mobile))
                    },
                    operator = SSConstants.REMOVE
                )
            }
        } else {
            Logger.e("serror", "Mobile number is not valid : $mobile")
        }
    }

    override fun setWhatsApp(mobile: String) {
        if (isMobileNumberValid(mobile)) {
            coroutineScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
                internalOperatorCallOp(
                    properties = buildJsonObject {
                        put(SSConstants.WHATS_APP, JsonPrimitive(mobile))
                    },
                    operator = SSConstants.APPEND
                )
            }
        } else {
            Logger.e("serror", "Mobile number is not valid : $mobile")
        }
    }

    override fun unSetWhatsApp(mobile: String) {
        if (isMobileNumberValid(mobile)) {
            coroutineScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
                internalOperatorCallOp(
                    properties = buildJsonObject {
                        put(SSConstants.WHATS_APP, JsonPrimitive(mobile))
                    },
                    operator = SSConstants.REMOVE
                )
            }
        } else {
            Logger.e("serror", "Mobile number is not valid : $mobile")
        }
    }

    // TODO - Create constant

    override fun setIOSPush(newToken: String) {
        coroutineScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            val oldToken = SSApiInternal.getIOSToken()
            if (newToken != oldToken) {
                SSApiInternal.setIOSToken(newToken)
                internalOperatorCallOp(properties = buildJsonObject {
                    put(SSConstants.PUSH_IOS_TOKEN, JsonPrimitive(newToken))
                    put(SSConstants.PUSH_VENDOR, JsonPrimitive(SSConstants.PUSH_VENDOR_APNS))
                    put(SSConstants.DEVICE_ID, JsonPrimitive(SSApiInternal.getDeviceID()))
                }, operator = SSConstants.APPEND)
            }
        }
    }

    override fun unSetIOSPush(token: String) {
        remove(
            buildJsonObject {
                put(SSConstants.PUSH_IOS_TOKEN, JsonPrimitive(token))
                put(SSConstants.PUSH_VENDOR, JsonPrimitive(SSConstants.PUSH_VENDOR_APNS))
            }.toString()
        )
    }

    override fun setPreferredLanguage(languageCode: String) {
        val processedLanguageCode = languageCode.lowercase()
        val isValid = PreferredLanguage.values[processedLanguageCode] != null
        if (!isValid) {
            Logger.i(SSConstants.TAG_SUPRSEND, "invalid value $languageCode")
            return
        }
        internalOperatorCall(
            properties = buildJsonObject {
                put(SSConstants.PREFERRED_LANGUAGE, JsonPrimitive(processedLanguageCode))
            },
            operator = SSConstants.SET
        )
    }

    private fun internalOperatorCall(properties: JsonObject, operator: String) {
        coroutineScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            val filteredProperties = properties.filterSSReservedKeys()
            if (filteredProperties.isNotEmpty()) {
                internalOperatorCallOp(filteredProperties, operator)
            } else {
                Logger.i(SSConstants.TAG_VALIDATION, "Payload ignored as none keys are valid after filtering reserved keys for $operator $properties")
            }
        }
    }

    fun internalOperatorCallOp(properties: JsonElement, operator: String) {
        val userLocalDatasource = UserLocalDatasource()
        SdkCreator
            .eventLocalDatasource
            .track(
                EventModel(
                    value = PayloadCreator
                        .buildUserOperatorPayload(
                            distinctId = userLocalDatasource.getIdentity(),
                            setProperties = properties,
                            operator = operator
                        ),
                    id = uuid()
                )
            )
    }

    companion object {
        const val TAG = SSApiInternal.TAG
    }
}
