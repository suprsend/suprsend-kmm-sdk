package app.suprsend.android.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

actual val httpClientEngine: HttpClientEngine by lazy {
    OkHttp.create {
        var okHttpClientInstance: OkHttpClient? = null
        config {
            followRedirects(true)
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(10, TimeUnit.SECONDS)
            okHttpClientInstance = build()
        }
        preconfigured = okHttpClientInstance
    }
}
