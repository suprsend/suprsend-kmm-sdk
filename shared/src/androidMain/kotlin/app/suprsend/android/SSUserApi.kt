package app.suprsend.android

import app.suprsend.android.user.UserLocalDatasource
import org.json.JSONObject

class SSUserApi {

    private val user = SSApiInternal.getUser()

    private fun set(key: String, value: Any) {
        user.set(key, value)
    }

    internal fun set(properties: JSONObject) {
        user.set(properties.toString())
    }

    private fun unSet(key: String) {
        user.unSet(key)
    }

    private fun unSet(keys: List<String>) {
        user.unSet(keys)
    }

    private fun setOnce(key: String, value: Any) {
        user.setOnce(key, value)
    }

    private fun setOnce(properties: JSONObject) {
        user.setOnce(properties.toString())
    }

    private fun increment(key: String, value: Any) {
        user.increment(key, value)
    }

    private fun increment(properties: JSONObject) {
        user.increment(properties.toString())
    }

    private fun append(key: String, value: Any) {
        user.append(key, value)
    }

    private fun remove(key: String, value: Any) {
        user.remove(key, value)
    }

    private fun setEmail(email: String) {
        user.setEmail(email)
    }

    private fun unSetEmail(email: String) {
        user.unSetEmail(email)
    }

    private fun setSms(mobile: String) {
        user.setSms(mobile)
    }

    private fun unSetSms(mobile: String) {
        user.unSetSms(mobile)
    }

    private fun setWhatsApp(mobile: String) {
        user.setWhatsApp(mobile)
    }

    private fun unSetWhatsApp(mobile: String) {
        user.unSetWhatsApp(mobile)
    }

    fun setAndroidPush(token: String) {
        user.setAndroidPush(token)
    }

    fun unSetAndroidPush(token: String) {
        user.unSetAndroidPush(token)
    }

    // Not included in contract
    private fun getUserIdentity(): String {
        return UserLocalDatasource().getIdentity()
    }

    /**
     * If SSFirebaseMessagingService is registered in your app manifest
     * then this will return fcm token else this will return blank string
     */
    fun getFcmToken(): String {
        return user.getSdkFcmToken()
    }
}
