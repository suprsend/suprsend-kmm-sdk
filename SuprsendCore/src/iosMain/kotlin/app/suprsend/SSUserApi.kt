package app.suprsend

import app.suprsend.base.Logger
import app.suprsend.base.toJsonObject

class SSUserApi
constructor(
    private val mutationHandler: MutationHandler
) {

    private val user = SSApiInternal.getUser()

    fun set(key: String, value: Any) {
        try {
            user.set(key, value)
        } catch (exception: Exception) {
            Logger.e(TAG_EXCEPTION, "", exception)
        }
    }

    fun set(properties: Map<String, Any>) {
        try {
            user.set(properties.toJsonObject().toString())
        } catch (exception: Exception) {
            Logger.e(TAG_EXCEPTION, "", exception)
        }
    }

    fun unSet(key: String) {
        try {
            user.unSet(key)
        } catch (exception: Exception) {
            Logger.e(TAG_EXCEPTION, "", exception)
        }
    }

    fun unSet(keys: List<String>) {
        try {
            user.unSet(keys)
        } catch (exception: Exception) {
            Logger.e(TAG_EXCEPTION, "", exception)
        }
    }

    fun setOnce(key: String, value: Any) {
        try {
            user.setOnce(key, value)
        } catch (exception: Exception) {
            Logger.e(TAG_EXCEPTION, "", exception)
        }
    }

    fun setOnce(properties: Map<String, Any>) {
        try {
            user.setOnce(properties.toJsonObject().toString())
        } catch (exception: Exception) {
            Logger.e(TAG_EXCEPTION, "", exception)
        }
    }

    fun increment(key: String, value: Number) {
        try {
            user.increment(key, value)
        } catch (exception: Exception) {
            Logger.e(TAG_EXCEPTION, "", exception)
        }
    }

    fun increment(properties: Map<String, Number>) {
        try {
            user.increment(properties.toJsonObject().toString())
        } catch (exception: Exception) {
            Logger.e(TAG_EXCEPTION, "", exception)
        }
    }

    fun append(key: String, value: Any) {
        try {
            user.append(key, value)
        } catch (exception: Exception) {
            Logger.e(TAG_EXCEPTION, "", exception)
        }
    }

    fun remove(key: String, value: Any) {
        try {
            user.remove(key, value)
        } catch (exception: Exception) {
            Logger.e(TAG_EXCEPTION, "", exception)
        }
    }

    fun setEmail(email: String) {
        try {
            user.setEmail(email)
        } catch (exception: Exception) {
            Logger.e(TAG_EXCEPTION, "", exception)
        }
    }

    fun unSetEmail(email: String) {
        try {
            user.unSetEmail(email)
        } catch (exception: Exception) {
            Logger.e(TAG_EXCEPTION, "", exception)
        }
    }

    fun setSms(mobile: String) {
        try {
            user.setSms(mobile)
        } catch (exception: Exception) {
            Logger.e(TAG_EXCEPTION, "", exception)
        }
    }

    fun unSetSms(mobile: String) {
        try {
            user.unSetSms(mobile)
        } catch (exception: Exception) {
            Logger.e(TAG_EXCEPTION, "", exception)
        }
    }

    fun setWhatsApp(mobile: String) {
        try {
            user.setWhatsApp(mobile)
        } catch (exception: Exception) {
            Logger.e(TAG_EXCEPTION, "", exception)
        }
    }

    fun unSetWhatsApp(mobile: String) {
        try {
            user.unSetWhatsApp(mobile)
        } catch (exception: Exception) {
            Logger.e(TAG_EXCEPTION, "", exception)
        }
    }

    fun setIOSPush(token: String) {
        try {
            user.setIOSPush(token)
            SSApiInternal.flush(mutationHandler)
        } catch (exception: Exception) {
            Logger.e(TAG_EXCEPTION, "", exception)
        }
    }

    fun unSetIOSPush(token: String) {
        try {
            user.unSetIOSPush(token)
            SSApiInternal.flush(mutationHandler)
        } catch (exception: Exception) {
            Logger.e(TAG_EXCEPTION, "", exception)
        }
    }
}
