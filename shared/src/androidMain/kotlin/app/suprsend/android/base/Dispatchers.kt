package app.suprsend.android.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual fun ioDispatcher(): CoroutineDispatcher = Dispatchers.IO
actual fun mainDispatcher(): CoroutineDispatcher = Dispatchers.Main
