package app.suprsend.android.config

import app.suprsend.android.GLOBAL_SUPR_SEND_DATABASE_WRAPPER

/**
 * This will only save key and value(string)
 */
internal object ConfigHelper {

    private val queries = GLOBAL_SUPR_SEND_DATABASE_WRAPPER.get()!!.suprSendDatabase.configTableQueries

    fun addOrUpdate(key: String, value: String) {
        queries.insert(key, ConfigModel(key, value))
    }

    fun get(key: String): String? {
        return queries.get(key).executeAsOneOrNull()?.value?.value
    }
}
