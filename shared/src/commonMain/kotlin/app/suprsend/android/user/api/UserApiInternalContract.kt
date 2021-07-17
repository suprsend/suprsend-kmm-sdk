package app.suprsend.android.user.api

interface UserApiInternalContract {

    fun set(key: String, value: Any)
    fun set(propertiesJson: String)
    fun unSet(key: String)

    fun setOnce(key: String, value: Any)
    fun setOnce(propertiesJson: String)

    fun increment(key: String, value: Any)
    fun increment(propertiesJson: String)

    fun append(key: String, value: Any)

    fun remove(key: String, value: Any)

    fun setEmail(email: String)
    fun unSetEmail(email: String)

    fun setSms(mobile: String)
    fun unSetSms(mobile: String)

    fun setWhatsApp(mobile: String)
    fun unSetWhatsApp(mobile: String)

    fun setAndroidPush(token: String)
    fun unSetAndroidPush(token: String)
    fun refreshAndroidPush(token: String)
    fun reset()
}