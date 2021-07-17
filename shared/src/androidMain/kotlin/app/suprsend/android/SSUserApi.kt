package app.suprsend.android

import org.json.JSONObject

class SSUserApi {

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

    fun setOnce(key: String, value: Any) {
        user.setOnce(key, value)
    }

    fun setOnce(properties: JSONObject) {
        user.setOnce(properties.toString())
    }

    fun increment(key: String, value: Any) {
        user.increment(key, value)
    }

    fun increment(properties: JSONObject) {
        user.increment(properties.toString())
    }

    fun append(key: String, value: Any) {
        user.append(key, value)
    }

    fun remove(key: String, value: Any) {
        user.remove(key, value)
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

    fun setAndroidPush(token: String) {
        user.setAndroidPush(token)
    }

    fun unSetAndroidPush(token: String) {
        user.unSetAndroidPush(token)
    }

    fun refreshAndroidPush(token: String) {
        user.refreshAndroidPush(token)
    }

    fun reset() {
        user.reset()
    }
}