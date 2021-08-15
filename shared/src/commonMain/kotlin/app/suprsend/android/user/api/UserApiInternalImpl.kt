package app.suprsend.android.user.api

import app.suprsend.android.SSApiInternal
import app.suprsend.android.base.Logger
import app.suprsend.android.base.SSConstants
import app.suprsend.android.base.convertToJsonPrimitive
import app.suprsend.android.base.ioDispatcher
import app.suprsend.android.base.toKotlinJsonObject
import app.suprsend.android.base.uuid
import app.suprsend.android.config.ConfigHelper
import app.suprsend.android.coroutineExceptionHandler
import app.suprsend.android.event.EventModel
import app.suprsend.android.event.PayloadCreator
import app.suprsend.android.user.UserEventLocalDataSource
import app.suprsend.android.user.UserLocalDatasource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject

internal class UserApiInternalImpl : UserApiInternalContract {

    override fun set(key: String, value: Any) {
        Logger.i("user", "set $key ")
        val valuePrimitive = value.convertToJsonPrimitive(key)
        valuePrimitive ?: return
        internalOperatorCall(
            buildJsonObject {
                put(key, valuePrimitive)
            },
            operator = "\$set"
        )
    }

    override fun set(propertiesJson: String) {
        Logger.i("user", "set properties")
        internalOperatorCall(
            propertiesJson.toKotlinJsonObject(),
            operator = "\$set"
        )
    }

    override fun setOnce(key: String, value: Any) {
        Logger.i("user", "setOnce $key")
        val valuePrimitive = value.convertToJsonPrimitive(key)
        valuePrimitive ?: return
        internalOperatorCall(
            buildJsonObject {
                put(key, valuePrimitive)
            },
            operator = "\$set_once"
        )
    }

    override fun setOnce(propertiesJson: String) {
        Logger.i("user", "setOnce properties")
        internalOperatorCall(
            propertiesJson.toKotlinJsonObject(),
            operator = "\$set_once"
        )
    }

    override fun increment(key: String, value: Any) {
        Logger.i("user", "increment $key")
        val valuePrimitive = value.convertToJsonPrimitive(key)
        valuePrimitive ?: return
        internalOperatorCall(
            buildJsonObject {
                put(key, valuePrimitive)
            },
            operator = "\$add"
        )
    }

    override fun increment(propertiesJson: String) {
        Logger.i("user", "increment properties")
        internalOperatorCall(
            propertiesJson.toKotlinJsonObject(),
            operator = "\$add"
        )
    }

    override fun append(key: String, value: Any) {
        Logger.i("user", "append $key")
        val valuePrimitive = value.convertToJsonPrimitive(key)
        valuePrimitive ?: return
        internalOperatorCall(
            buildJsonObject {
                put(key, valuePrimitive)
            },
            operator = "\$append"
        )
    }

    override fun append(propertiesJson: String) {
        Logger.i("user", "append properties")
        internalOperatorCall(
            propertiesJson.toKotlinJsonObject(),
            operator = "\$append"
        )
    }

    override fun remove(key: String, value: Any) {
        Logger.i("user", "remove $key")
        val valuePrimitive = value.convertToJsonPrimitive(key)
        valuePrimitive ?: return
        internalOperatorCall(
            buildJsonObject {
                put(key, valuePrimitive)
            },
            operator = "\$remove"
        )
    }

    override fun unSet(key: String) {
        unSet(listOf(key))
    }

    override fun unSet(keys: List<String>) {
        Logger.i("user", "unSet $keys")
        internalOperatorCall(
            buildJsonArray {
                keys.forEach { key ->
                    add(key)
                }
            },
            operator = "\$unset"
        )
    }

    override fun setEmail(email: String) {
        Logger.i("user", "setEmail : $email")
        append("\$email", email)
    }

    override fun unSetEmail(email: String) {
        Logger.i("user", "UnSetEmail : $email")
        remove("\$email", email)
    }

    override fun setSms(mobile: String) {
        Logger.i("user", "setSms : $mobile")
        append("\$sms", mobile)
    }

    override fun unSetSms(mobile: String) {
        Logger.i("user", "unSetSms : $mobile")
        remove("\$sms", mobile)
    }

    override fun setWhatsApp(mobile: String) {
        Logger.i("user", "setWhatsApp : $mobile")
        append("\$whatsapp", mobile)
    }

    override fun unSetWhatsApp(mobile: String) {
        Logger.i("user", "unSetWhatsApp : $mobile")
        remove("\$whatsapp", mobile)
    }

    // TODO - Create constant
    override fun setAndroidPush(newToken: String) {
        val oldToken = getSdkFcmToken()
        Logger.i("user", "Old token : $oldToken")
        if (oldToken == newToken) {
            Logger.i("user", "Ignored token : $newToken as this is already pushed for this device")
            return
        }
        ConfigHelper.addOrUpdate(SSConstants.CONFIG_FCM_PUSH_TOKEN, newToken)

        Logger.i("user", "setAndroidPush : $newToken")
        append(buildJsonObject {
            put(SSConstants.FCM_TOKEN_PUSH, JsonPrimitive(newToken))
            put(SSConstants.DEVICE_ID, JsonPrimitive(SSApiInternal.getDeviceID()))
        }.toString())
    }

    override fun getSdkFcmToken(): String {
        return ConfigHelper.get(SSConstants.CONFIG_FCM_PUSH_TOKEN) ?: ""
    }

    override fun unSetAndroidPush(token: String) {
        Logger.i("user", "unSetAndroidPush : $token")
        remove(SSConstants.FCM_TOKEN_PUSH, token)
    }

    private fun internalOperatorCall(properties: JsonElement, operator: String) {
        GlobalScope.launch(ioDispatcher() + coroutineExceptionHandler) {
            val userLocalDatasource = UserLocalDatasource()
            UserEventLocalDataSource()
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
    }
}
