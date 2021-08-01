package app.suprsend.android

import app.suprsend.android.user.UserLocalDatasource
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

    fun unSet(keys: List<String>) {
        user.unSet(keys)
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

    fun reset() {
        user.reset()
    }

    // Not included in contract
    fun getUserIdentity(): String {
        return UserLocalDatasource().getIdentity()
    }

    fun getFcmToken(): String {
        return user.getSdkFcmToken()
    }
}
