package app.suprsend.base

import kotlinx.coroutines.CoroutineDispatcher

internal expect fun ioDispatcher(): CoroutineDispatcher
internal expect fun singleThreadDispatcher(): CoroutineDispatcher
internal expect fun mainDispatcher(): CoroutineDispatcher
