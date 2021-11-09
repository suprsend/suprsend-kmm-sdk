package app.suprsend.config

import app.suprsend.base.SdkCreator
import app.suprsend.base.database

/**
 * This will only save key and value(string)
 */
internal object ConfigHelper {

    private val queries = database.get()!!.suprSendDatabase.configTableQueries

    fun addOrUpdate(key: String, value: String) {
        queries.insert(key, ConfigModel(value))
    }

    fun get(key: String): String? {
        return queries.get(key).executeAsOneOrNull()?.value?.value
    }

    fun addOrUpdate(key: String, value: Boolean) {
        queries.insert(key, ConfigModel(getBooleanToString(value)))
    }

    fun getBoolean(key: String): Boolean? {
        return getStringToBoolean(queries.get(key).executeAsOneOrNull()?.value?.value)
    }

    private fun getStringToBoolean(value: String?): Boolean? {
        if (value == "1")
            return true
        return false
    }

    private fun getBooleanToString(value: Boolean): String {
        return if (value) "1" else "0"
    }
}
