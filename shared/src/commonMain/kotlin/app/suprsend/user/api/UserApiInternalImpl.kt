package app.suprsend.user.api

import app.suprsend.SSApiInternal
import app.suprsend.base.SSConstants
import app.suprsend.base.SdkCreator
import app.suprsend.base.convertToJsonPrimitive
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

    override fun unSet(key: String) {
        unSet(listOf(key))
    }

    override fun unSet(keys: List<String>) {
        internalOperatorCall(
            buildJsonArray {
                keys.forEach { key ->
                    add(key)
                }
            },
            operator = SSConstants.UNSET
        )
    }

    override fun setEmail(email: String) {
        append(SSConstants.EMAIL, email)
    }

    override fun unSetEmail(email: String) {
        remove(SSConstants.EMAIL, email)
    }

    override fun setSms(mobile: String) {
        append(SSConstants.SMS, mobile)
    }

    override fun unSetSms(mobile: String) {
        remove(SSConstants.SMS, mobile)
    }

    override fun setWhatsApp(mobile: String) {
        append(SSConstants.WHATS_APP, mobile)
    }

    override fun unSetWhatsApp(mobile: String) {
        remove(SSConstants.WHATS_APP, mobile)
    }

    // TODO - Create constant
    override fun setAndroidPush(newToken: String) {
        val oldToken = SSApiInternal.getFcmToken()
        if (oldToken != newToken) {
            SSApiInternal.setFcmToken(newToken)
        }
        append(buildJsonObject {
            put(SSConstants.FCM_TOKEN_PUSH, JsonPrimitive(newToken))
            put(SSConstants.DEVICE_ID, JsonPrimitive(SSApiInternal.getDeviceID()))
        }.toString())
    }

    override fun unSetAndroidPush(token: String) {
        remove(SSConstants.FCM_TOKEN_PUSH, token)
    }

    private fun internalOperatorCall(properties: JsonElement, operator: String) {
        coroutineScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            internalOperatorCallOp(properties, operator)
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
