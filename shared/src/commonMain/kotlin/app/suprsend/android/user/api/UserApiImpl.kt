package app.suprsend.android.user.api

import app.suprsend.android.base.convertToJsonPrimitive
import app.suprsend.android.base.ioDispatcher
import app.suprsend.android.base.toKotlinJsonObject
import app.suprsend.android.config.ConfigHelper
import app.suprsend.android.event.EventLocalDatasource
import app.suprsend.android.event.PayloadCreator
import app.suprsend.android.user.UserLocalDatasource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject

internal class UserApiImpl : UserApi {

    override fun set(key: String, value: Any) {
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
        internalOperatorCall(
            propertiesJson.toKotlinJsonObject(),
            operator = "\$set"
        )
    }

    override fun setOnce(key: String, value: Any) {
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
        internalOperatorCall(
            propertiesJson.toKotlinJsonObject(),
            operator = "\$set_once"
        )
    }

    override fun increment(key: String, value: Any) {
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
        internalOperatorCall(
            propertiesJson.toKotlinJsonObject(),
            operator = "\$add"
        )
    }

    override fun append(key: String, value: Any) {
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
        internalOperatorCall(
            buildJsonArray {
                add(key)
            },
            operator = "\$unset"
        )
    }

    override fun setEmail(email: String) {
        append("\$email", email)
    }

    override fun unSetEmail(email: String) {
        remove("\$email", email)
    }

    override fun setSms(mobile: String) {
        append("\$sms", mobile)
    }

    override fun unSetSms(mobile: String) {
        remove("\$sms", mobile)
    }

    override fun setWhatsApp(mobile: String) {
        append("\$whatsapp", mobile)
    }

    override fun unSetWhatsApp(mobile: String) {
        remove("\$whatsapp", mobile)
    }

    override fun setAndroidPush(token: String) {
        append("\$androidpush", token)
        ConfigHelper.addOrUpdate("\$androidpush", token)
    }

    override fun unSetAndroidPush(token: String) {
        remove("\$androidpush", token)
    }

    override fun refreshAndroidPush(token: String) {
        remove("\$androidpush", ConfigHelper.get("\$androidpush") ?: "")
        append("\$androidpush", token)
    }

    private fun internalOperatorCall(properties: JsonElement, operator: String) {
        GlobalScope.launch(ioDispatcher()) {
            val userLocalDatasource = UserLocalDatasource()
            EventLocalDatasource()
                .track(
                    PayloadCreator
                        .buildUserOperatorPayload(
                            distinctId = userLocalDatasource.getIdentity(),
                            setProperties = properties,
                            operator = operator
                        )
                )
        }
    }
}