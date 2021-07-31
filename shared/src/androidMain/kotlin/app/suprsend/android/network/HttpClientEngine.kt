package app.suprsend.android.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient

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
