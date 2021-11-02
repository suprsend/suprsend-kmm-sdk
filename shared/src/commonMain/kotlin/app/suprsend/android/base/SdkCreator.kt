package app.suprsend.android.base

import app.suprsend.android.Information
import app.suprsend.android.database.SSDatabaseWrapper
import app.suprsend.android.event.EventLocalDatasource
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
