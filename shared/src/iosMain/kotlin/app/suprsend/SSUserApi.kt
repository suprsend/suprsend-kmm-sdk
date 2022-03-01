package app.suprsend

import app.suprsend.base.coroutineExceptionHandler
import app.suprsend.base.executorScope
import app.suprsend.base.singleThreadDispatcher
import app.suprsend.user.SSInternalUser
import kotlinx.coroutines.launch

class SSUserApi
constructor() {

    fun set(key: String, value: Any) {
        executorScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            SSInternalUser.set(key, value)
        }
    }

    fun set(properties: Map<String, Any>) {
        executorScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            SSInternalUser.set(properties)
        }
    }

    fun unSet(key: String) {
        executorScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            SSInternalUser.unSet(key)
        }
    }

    fun unSet(keys: List<String>) {
        executorScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            SSInternalUser.unSet(keys)
        }
    }

    fun setOnce(key: String, value: Any) {
        executorScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            SSInternalUser.setOnce(key, value)
        }
    }

    fun setOnce(properties: Map<String, Any>) {
        executorScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            SSInternalUser.setOnce(properties)
        }
    }

    fun increment(key: String, value: Number) {
        executorScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            SSInternalUser.increment(key, value)
        }
    }

    fun increment(properties: Map<String, Number>) {
        executorScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            SSInternalUser.increment(properties)
        }
    }

    fun append(key: String, value: Any) {
        executorScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            SSInternalUser.append(key, value)
        }
    }

    fun remove(key: String, value: Any) {
        executorScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            SSInternalUser.remove(key, value)
        }
    }

    fun setEmail(email: String) {
        executorScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            SSInternalUser.setEmail(email)
        }
    }

    fun unSetEmail(email: String) {
        executorScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            SSInternalUser.unSetEmail(email)
        }
    }

    fun setSms(mobile: String) {
        executorScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            SSInternalUser.setSms(mobile)
        }
    }

    fun unSetSms(mobile: String) {
        executorScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            SSInternalUser.unSetSms(mobile)
        }
    }

    fun setWhatsApp(mobile: String) {
        executorScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            SSInternalUser.setWhatsApp(mobile)
        }
    }

    fun unSetWhatsApp(mobile: String) {
        executorScope.launch(singleThreadDispatcher() + coroutineExceptionHandler) {
            SSInternalUser.unSetWhatsApp(mobile)
        }
    }
}
