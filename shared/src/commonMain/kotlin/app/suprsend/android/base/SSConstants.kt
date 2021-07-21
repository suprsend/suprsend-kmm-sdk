package app.suprsend.android.base

internal object SSConstants {
    const val BASE_URL = "https://trackstaging.suprsend.com"
    const val EVENT_URL = "$BASE_URL/event"
    const val NOTIFICATION_PAYLOAD = "supr_send_n_pl"
    const val EVENT_KEY_MAX_LENGTH = 120
    const val EVENT_VALUE_MAX_LENGTH = 512
    const val FLUSH_EVENT_PAYLOAD_SIZE = 20L

    const val ANONYMOUS_USER_ID_LENGTH = 36


    const val IDENTIFIED_ID = "\$identified_id"
    const val ANONYMOUS_ID = "\$anon_id"
    const val DISTINCT_ID = "distinct_id"
    const val ENV = "ENV"
    const val INSERT_ID = "\$insert_id"
    const val IDENTIFY = "\$identify"
    const val TIME = "\$time"
    const val PROPERTIES = "properties"
    const val EVENT = "EVENT"
}