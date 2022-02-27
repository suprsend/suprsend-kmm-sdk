package app.suprsend.base

import app.suprsend.Information
import app.suprsend.database.SSDatabaseWrapper
import app.suprsend.event.EventLocalDatasource
import com.squareup.sqldelight.internal.Atomic
/* ktlint-disable no-wildcard-imports */
import io.ktor.client.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
/* ktlint-enable no-wildcard-imports */
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
val executorScope = CoroutineScope(SupervisorJob())
// val flushExecutorScope = CoroutineScope(SupervisorJob())

internal val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
    Logger.e("exception", "", exception)
}
