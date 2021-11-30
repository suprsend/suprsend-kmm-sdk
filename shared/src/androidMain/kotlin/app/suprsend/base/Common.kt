package app.suprsend.base

import java.util.UUID

actual fun uuid(): String = UUID.randomUUID().toString()

actual fun isMobileNumberValid(mobile: String): Boolean = SdkAndroidCreator.phoneNumberUtils.isValidNumber(mobile)
