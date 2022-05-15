package app.suprsend.base

import platform.Foundation.NSUUID

actual fun uuid(): String = NSUUID().UUIDString().lowercase()

actual fun isMobileNumberValid(mobile: String): Boolean = true
