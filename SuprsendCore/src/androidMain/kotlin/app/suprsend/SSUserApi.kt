package app.suprsend

import org.json.JSONObject

class SSUserApi
constructor(
    private val mutationHandler: MutationHandler
) {

    private val user = SSApiInternal.getUser()

    fun set(key: String, value: Any) {
        user.set(key, value)
    }

    fun set(properties: JSONObject) {
        user.set(properties.toString())
    }

    fun unSet(key: String) {
        user.unSet(key)
    }

    fun unSet(keys: List<String>) {
        user.unSet(keys)
    }

    fun setOnce(key: String, value: Any) {
        user.setOnce(key, value)
    }

    fun setOnce(properties: JSONObject) {
        user.setOnce(properties.toString())
    }

    fun increment(key: String, value: Number) {
        user.increment(key, value)
    }

    fun increment(properties: Map<String, Number>) {
        user.increment(JSONObject(properties).toString())
    }

    fun append(key: String, value: Any) {
        user.append(key, value)
    }

    fun remove(key: String, value: Any) {
        user.remove(key, value)
    }

    fun remove(properties: JSONObject) {
        user.remove(properties.toString())
    }

    fun setEmail(email: String) {
        user.setEmail(email)
    }

    fun unSetEmail(email: String) {
        user.unSetEmail(email)
    }

    fun setSms(mobile: String) {
        user.setSms(mobile)
    }

    fun unSetSms(mobile: String) {
        user.unSetSms(mobile)
    }

    fun setWhatsApp(mobile: String) {
        user.setWhatsApp(mobile)
    }

    fun unSetWhatsApp(mobile: String) {
        user.unSetWhatsApp(mobile)
    }

    fun setAndroidFcmPush(token: String) {
        user.setAndroidFcmPush(token)
        SSApiInternal.flush(mutationHandler)
    }

    fun unSetAndroidFcmPush(token: String) {
        user.unSetAndroidFcmPush(token)
        SSApiInternal.flush(mutationHandler)
    }

    fun setAndroidXiaomiPush(token: String) {
        user.setAndroidXiaomiPush(token)
        SSApiInternal.flush(mutationHandler)
    }

    fun unSetAndroidXiaomiPush(token: String) {
        user.unSetAndroidXiaomiPush(token)
        SSApiInternal.flush(mutationHandler)
    }

    fun setIOSPush(token: String) {
        user.setIOSPush(token)
        SSApiInternal.flush(mutationHandler)
    }

    fun unSetIOSPush(token: String) {
        user.unSetIOSPush(token)
        SSApiInternal.flush(mutationHandler)
    }
}
