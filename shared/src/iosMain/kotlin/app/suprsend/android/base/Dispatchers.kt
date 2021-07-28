package app.suprsend.android.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual fun mainDispatcher(): CoroutineDispatcher = Dispatchers.Main
actual fun ioDispatcher(): CoroutineDispatcher = Dispatchers.Default
