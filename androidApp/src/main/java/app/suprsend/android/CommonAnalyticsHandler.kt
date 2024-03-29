package app.suprsend.android

import android.annotation.SuppressLint
import android.content.Context
import app.suprsend.SSApi
import app.suprsend.base.LogLevel
import com.mixpanel.android.mpmetrics.MixpanelAPI
import org.json.JSONObject

object CommonAnalyticsHandler {

    private lateinit var ssApi: SSApi

    @SuppressLint("StaticFieldLeak")
    private lateinit var mixpanelAPI: MixpanelAPI

    fun initialize(context: Context) {
        if (this::ssApi.isInitialized)
            return
        ssApi = SSApi.getInstance(BuildConfig.SS_TOKEN, BuildConfig.SS_SECRET, BuildConfig.SS_API_BASE_URL)
        ssApi.setLogLevel(LogLevel.VERBOSE)
        mixpanelAPI = MixpanelAPI.getInstance(context, BuildConfig.MX_TOKEN)
    }

    fun identify(identity: String) {
        ssApi.identify(identity)
        ssApi.getUser().setEmail(identity)
        mixpanelAPI.identify(identity)
    }

    fun track(eventName: String) {
        ssApi.track(eventName)
        mixpanelAPI.track(eventName)
    }

    fun track(eventName: String, properties: JSONObject? = null) {
        ssApi.track(eventName, properties)
        mixpanelAPI.track(eventName, properties)
    }

    fun set(key: String, value: String) {
        ssApi.getUser().set(key, value)
        mixpanelAPI.people.set(key, value)
    }

    fun set(properties: JSONObject) {
        ssApi.getUser().set(properties)
        mixpanelAPI.people.set(properties)
    }

    fun increment(key: String, value: Number) {
        ssApi.getUser().increment(key, value)
        mixpanelAPI.people.increment(key, value.toDouble())
    }

    fun increment(properties: Map<String, Number>) {
        ssApi.getUser().increment(properties)
        mixpanelAPI.people.increment(properties)
    }

    fun append(key: String, value: String) {
        ssApi.getUser().append(key, value)
        mixpanelAPI.people.append(key, value)
    }

    fun remove(key: String, value: String) {
        ssApi.getUser().remove(key, value)
        mixpanelAPI.people.remove(key, value)
    }

    fun unset(key: String) {
        ssApi.getUser().unSet(key)
        mixpanelAPI.people.unset(key)
    }

    fun reset() {
        ssApi.reset()
        mixpanelAPI.reset()
    }

    fun setOnce(key: String, value: Any) {
        ssApi.getUser().setOnce(key, value)
        mixpanelAPI.people.setOnce(key, value)
    }

    fun setOnce(properties: JSONObject) {
        ssApi.getUser().setOnce(properties)
        mixpanelAPI.people.setOnce(properties)
    }

    fun setSuperProperties(key: String, value: Any) {
        ssApi.setSuperProperty(key, value)
        mixpanelAPI.registerSuperPropertiesMap(hashMapOf(key to value))
    }

    fun setSuperProperties(jsonObject: JSONObject) {
        ssApi.setSuperProperties(jsonObject)
        mixpanelAPI.registerSuperProperties(jsonObject)
    }

    fun unSetSuperProperties(key: String) {
        ssApi.unSetSuperProperty(key)
        mixpanelAPI.unregisterSuperProperty(key)
    }

    fun purchaseMade(properties: JSONObject) {
        ssApi.purchaseMade(properties)
    }
}
