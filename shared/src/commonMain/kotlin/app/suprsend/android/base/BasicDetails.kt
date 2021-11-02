package app.suprsend.android.base

import kotlinx.serialization.Serializable

@Serializable
internal class BasicDetails(
    val apiKey: String,
    val apiSecret: String,
    private val apiBaseUrl: String? = null
) {
    fun getApiBaseUrl(): String {
        if (apiBaseUrl == null) {
            return SSConstants.DEFAULT_BASE_API_URL
        }
        val processedBaseUrl = apiBaseUrl.trim()
        return if (processedBaseUrl.endsWith("/"))
            processedBaseUrl.removeSuffix("/")
        else
            processedBaseUrl
    }
}
