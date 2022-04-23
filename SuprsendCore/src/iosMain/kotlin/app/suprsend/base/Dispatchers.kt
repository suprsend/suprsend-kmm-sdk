package app.suprsend.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext

private val SINGLE_DISPATCHER: CoroutineDispatcher by lazy { newSingleThreadContext("1thread") }

actual fun ioDispatcher(): CoroutineDispatcher = Dispatchers.Default

actual fun singleThreadDispatcher(): CoroutineDispatcher = SINGLE_DISPATCHER

actual fun mainDispatcher(): CoroutineDispatcher = Dispatchers.Main
