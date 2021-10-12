package app.suprsend.android.base

import platform.Foundation.NSUUID

actual fun uuid(): String = NSUUID().UUIDString()