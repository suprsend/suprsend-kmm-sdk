package app.suprsend.android

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object Creator {
    val gson: Gson by lazy {
        GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .setLenient()
            .create()
    }
}