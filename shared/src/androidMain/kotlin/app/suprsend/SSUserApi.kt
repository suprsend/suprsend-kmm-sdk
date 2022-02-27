package app.suprsend

import app.suprsend.user.SSInternalUser
import org.json.JSONObject

class SSUserApi
constructor(
    private val mutationHandler: MutationHandler
) {

    fun set(key: String, value: Any) {
        SSInternalUser.set(key, value)
    }

    fun set(properties: JSONObject) {
//        SSInternalUser.set(properties.toString())
    }

    fun unSet(key: String) {
        SSInternalUser.unSet(key)
    }

    fun unSet(keys: List<String>) {
        SSInternalUser.unSet(keys)
    }

    fun setOnce(key: String, value: Any) {
        SSInternalUser.setOnce(key, value)
    }

    fun setOnce(properties: JSONObject) {
//        SSInternalUser.setOnce(properties.toString())
    }

    fun increment(key: String, value: Number) {
        SSInternalUser.increment(key, value)
    }

    fun increment(properties: Map<String, Number>) {
//        SSInternalUser.increment(JSONObject(properties).toString())
    }

    fun append(key: String, value: Any) {
        SSInternalUser.append(key, value)
    }

    fun remove(key: String, value: Any) {
        SSInternalUser.remove(key, value)
    }

    fun remove(properties: JSONObject) {
//        SSInternalUser.remove(properties.toString())
    }

    fun setEmail(email: String) {
        SSInternalUser.setEmail(email)
    }

    fun unSetEmail(email: String) {
        SSInternalUser.unSetEmail(email)
    }

    fun setSms(mobile: String) {
        SSInternalUser.setSms(mobile)
    }

    fun unSetSms(mobile: String) {
        SSInternalUser.unSetSms(mobile)
    }

    fun setWhatsApp(mobile: String) {
        SSInternalUser.setWhatsApp(mobile)
    }

    fun unSetWhatsApp(mobile: String) {
        SSInternalUser.unSetWhatsApp(mobile)
    }

    fun setAndroidFcmPush(token: String) {
//        SSInternalUser.setAndroidFcmPush(token)
        SSApiInternal.flush(mutationHandler)
    }

    fun unSetAndroidFcmPush(token: String) {
//        SSInternalUser.unSetAndroidFcmPush(token)
        SSApiInternal.flush(mutationHandler)
    }

    fun setAndroidXiaomiPush(token: String) {
//        SSInternalUser.setAndroidXiaomiPush(token)
        SSApiInternal.flush(mutationHandler)
    }

    fun unSetAndroidXiaomiPush(token: String) {
//        SSInternalUser.unSetAndroidXiaomiPush(token)
        SSApiInternal.flush(mutationHandler)
    }

    fun setIOSPush(token: String) {
        SSInternalUser.setIOSPush(token)
        SSApiInternal.flush(mutationHandler)
    }

    fun unSetIOSPush(token: String) {
        SSInternalUser.unSetIOSPush(token)
        SSApiInternal.flush(mutationHandler)
    }
}
