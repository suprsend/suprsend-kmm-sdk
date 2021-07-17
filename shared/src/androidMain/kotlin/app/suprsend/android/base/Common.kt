package app.suprsend.android.base

import java.util.UUID

actual fun timeInMillis(): Long = System.currentTimeMillis()
actual fun uuid(): String = UUID.randomUUID().toString()