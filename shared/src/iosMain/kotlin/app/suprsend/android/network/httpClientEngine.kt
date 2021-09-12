package app.suprsend.android.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.ios.Ios

actual val httpClientEngine: HttpClientEngine by lazy { Ios.create() }
