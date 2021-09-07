package app.suprsend.android.base

import app.suprsend.android.Information
import app.suprsend.android.event.EventFlushHandler
import app.suprsend.android.event.EventLocalDatasource

internal object SdkCreator {
    val information: Information by lazy { Information() }
    val eventFlushHandler: EventFlushHandler by lazy { EventFlushHandler() }
    val eventLocalDatasource: EventLocalDatasource by lazy { EventLocalDatasource() }
}
