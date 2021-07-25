package app.suprsend.android.base

import java.util.UUID

actual fun uuid(): String = UUID.randomUUID().toString()