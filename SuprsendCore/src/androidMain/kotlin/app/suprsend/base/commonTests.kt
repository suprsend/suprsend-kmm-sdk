package app.suprsend.base

import java.util.UUID

actual fun uuid(): String = UUID.randomUUID().toString()

actual fun isMobileNumberValid(mobile: String): Boolean {
    return if (mobile.contains("+"))
        SdkAndroidCreator.phoneNumberUtils.isValidNumber(SdkAndroidCreator.phoneNumberUtils.parse(mobile, null))
    else
        false
}
