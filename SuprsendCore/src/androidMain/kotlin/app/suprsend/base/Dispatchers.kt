package app.suprsend.base

import java.util.concurrent.Executors
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher

val singleThreadDispatcher: CoroutineDispatcher by lazy { Executors.newFixedThreadPool(1).asCoroutineDispatcher() }
actual fun ioDispatcher(): CoroutineDispatcher = Dispatchers.IO
actual fun singleThreadDispatcher(): CoroutineDispatcher = singleThreadDispatcher
actual fun mainDispatcher(): CoroutineDispatcher = Dispatchers.Main
