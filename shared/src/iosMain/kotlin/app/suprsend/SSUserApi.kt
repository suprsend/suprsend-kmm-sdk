package app.suprsend

import app.suprsend.base.toJsonObject

class SSUserApi
constructor(
    private val mutationHandler: MutationHandler
) {

    private val user = SSApiInternal.getUser()

    fun set(key: String, value: Any) {
        user.set(key, value)
    }

    fun set(properties: Map<String, Any>) {
        user.set(properties.toJsonObject().toString())
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

    fun setOnce(properties: Map<String, Any>) {
        user.setOnce(properties.toJsonObject().toString())
    }

    fun increment(key: String, value: Number) {
        user.increment(key, value)
    }

    fun increment(properties: Map<String, Number>) {
        user.increment(properties.toJsonObject().toString())
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

}
