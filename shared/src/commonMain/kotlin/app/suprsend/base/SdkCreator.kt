package app.suprsend.base

import app.suprsend.Information
import app.suprsend.database.SSDatabaseWrapper
import app.suprsend.event.EventLocalDatasource
import com.squareup.sqldelight.internal.Atomic
import io.ktor.client.*
import kotlin.native.concurrent.SharedImmutable

@SharedImmutable
internal val database: Atomic<SSDatabaseWrapper?> = Atomic(null)

@SharedImmutable
internal val network: Atomic<HttpClient?> = Atomic(null)

@SharedImmutable
internal val logLevel: Atomic<LogLevel?> = Atomic(null)

internal object SdkCreator {
    val information: Information by lazy { Information() }
    val eventLocalDatasource: EventLocalDatasource by lazy { EventLocalDatasource() }





}
