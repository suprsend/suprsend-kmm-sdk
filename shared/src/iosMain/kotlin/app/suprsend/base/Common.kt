package app.suprsend.base

import platform.Foundation.NSUUID

actual fun uuid(): String = NSUUID().UUIDString()

// Todo : ios dev Phone number validation
actual fun isMobileNumberValid(mobile: String): Boolean = true
