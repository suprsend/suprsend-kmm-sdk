package app.suprsend.android.user.api

import app.suprsend.android.SSApiInternal
import app.suprsend.android.base.Logger
import app.suprsend.android.base.SSConstants
import app.suprsend.android.base.convertToJsonPrimitive
import app.suprsend.android.base.ioDispatcher
import app.suprsend.android.base.toKotlinJsonObject
import app.suprsend.android.base.uuid
import app.suprsend.android.config.ConfigHelper
import app.suprsend.android.event.EventModel
import app.suprsend.android.event.PayloadCreator
import app.suprsend.android.user.UserEventLocalDataSource
import app.suprsend.android.user.UserLocalDatasource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonElement
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
    override fun setAndroidPush(token: String) {
        Logger.i("user", "setAndroidPush : $token")
        append(SSConstants.FCM_TOKEN_PUSH, token)
        ConfigHelper.addOrUpdate(SSConstants.FCM_TOKEN_PUSH, token)
    }

    override fun getSdkFcmToken(): String {
        return ConfigHelper.get(SSConstants.FCM_TOKEN_PUSH) ?: ""
    }

    override fun unSetAndroidPush(token: String) {
        Logger.i("user", "unSetAndroidPush : $token")
        remove(SSConstants.FCM_TOKEN_PUSH, token)
    }

    override fun reset() {
        val newID = uuid()
        Logger.i("user", "reset : $newID")
        UserLocalDatasource().identify(newID)
        SSApiInternal.flush()
    }

    private fun internalOperatorCall(properties: JsonElement, operator: String) {
        GlobalScope.launch(ioDispatcher()) {
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
