package app.suprsend.android.android

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser

object Creator {
    val gson: Gson by lazy {
        GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .setLenient()
//            .setExclusionStrategies(AnnotationExclusionStrategy())
            .create()
    }
}

// Gson
fun String?.toGsonJsonObject(): JsonObject {
    if (isNullOrBlank())
        return JsonObject()
    return JsonParser.parseString(this).asJsonObject
}

fun String?.toGsonJsonArray(): JsonArray {
    if (isNullOrBlank()) {
        return JsonArray()
    }
    return JsonParser.parseString(this).asJsonArray
}

fun JsonObject?.getString(key: String, defaultValue: String = ""): String {
    val je = this?.get(key)
    return if (je?.isJsonNull == false && je.isJsonPrimitive) {
        je.asString
    } else {
        defaultValue
    }
}

fun JsonObject?.getLong(key: String, defaultValue: Long = 0): Long {
    val je = this?.get(key)
    return if (je?.isJsonNull == false && je.isJsonPrimitive) {
        je.asLong
    } else {
        defaultValue
    }
}

fun JsonObject?.getInt(key: String, defaultValue: Int = 0): Int {
    val je = this?.get(key)
    return if (je?.isJsonNull == false && je.isJsonPrimitive) {
        je.asInt
    } else {
        defaultValue
    }
}

fun JsonObject?.getDouble(key: String, defaultValue: Double = 0.0): Double {
    val je = this?.get(key)
    return if (je?.isJsonNull == false && je.isJsonPrimitive) {
        je.asDouble
    } else {
        defaultValue
    }
}

fun JsonObject?.getBoolean(key: String, defaultValue: Boolean = false): Boolean {
    val je = this?.get(key)
    return if (je?.isJsonNull == false && je.isJsonPrimitive) {
        je.asBoolean
    } else {
        defaultValue
    }
}
