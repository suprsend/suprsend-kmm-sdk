package app.suprsend.android.base

internal object SuprSendConstants {
    const val BASE_URL = "https://trackstaging.suprsend.com"
    const val EVENT_URL = "$BASE_URL/event"
    const val NOTIFICATION_PAYLOAD = "supr_send_n_pl"
    const val EVENT_KEY_MAX_LENGTH = 120
    const val EVENT_VALUE_MAX_LENGTH = 512
    const val FLUSH_EVENT_PAYLOAD_SIZE = 500L

    const val ANONYMOUS_USER_ID_LENGTH = 36
}