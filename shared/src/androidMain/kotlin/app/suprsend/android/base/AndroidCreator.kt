package app.suprsend.android.base

import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder


@SuppressLint("StaticFieldLeak")
internal object AndroidCreator {

    //Keeping only application context here
    lateinit var context: Context

    val gson: Gson by lazy {
        GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .setLenient()
            .create()
    }

}