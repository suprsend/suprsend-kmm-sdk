package app.suprsend.base

import android.annotation.SuppressLint
import android.content.Context
import com.google.i18n.phonenumbers.PhoneNumberUtil
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("StaticFieldLeak")
internal object SdkAndroidCreator {

    // Keeping only application context here
    lateinit var context: Context

    fun isContextInitialized(): Boolean {
        return this::context.isInitialized
    }

    val deviceInfo: DeviceInfo by lazy { DeviceInfo() }

    val networkInfo: NetworkInfo by lazy { NetworkInfo() }
    val phoneNumberUtils: PhoneNumberUtil by lazy { PhoneNumberUtil.getInstance() }
}

@SuppressLint("SimpleDateFormat")
fun getReadableDate(date: Date = Date()): String {
    return SimpleDateFormat("dd-MM-yyyy").format(date)
}
