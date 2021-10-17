package app.suprsend.android.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext

actual fun ioDispatcher(): CoroutineDispatcher = Dispatchers.Default
actual fun singleThreadDispatcher(): CoroutineDispatcher = newSingleThreadContext("1thread")
actual fun mainDispatcher(): CoroutineDispatcher = Dispatchers.Main
