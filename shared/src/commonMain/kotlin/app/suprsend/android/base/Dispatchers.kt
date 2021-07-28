package app.suprsend.android.base

import kotlinx.coroutines.CoroutineDispatcher

internal expect fun ioDispatcher(): CoroutineDispatcher
internal expect fun mainDispatcher(): CoroutineDispatcher
