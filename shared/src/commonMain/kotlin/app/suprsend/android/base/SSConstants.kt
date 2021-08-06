package app.suprsend.android.base

internal object SSConstants {
    const val BASE_URL = "https://collector.suprsend.workers.dev"
    const val EVENT_URL = "$BASE_URL/event"
    const val IDENTITY_URL = "$BASE_URL/event"
    const val NOTIFICATION_PAYLOAD = "supr_send_n_pl"
    const val EVENT_KEY_MAX_LENGTH = 120
    const val EVENT_VALUE_MAX_LENGTH = 512
    const val FLUSH_EVENT_PAYLOAD_SIZE = 20L

    const val ANONYMOUS_USER_ID_LENGTH = 36

    const val API_KEY = "api_key"
    const val IDENTIFIED_ID = "\$identified_id"
    const val ANONYMOUS_ID = "\$anon_id"
    const val DISTINCT_ID = "distinct_id"
    const val ENV = "ENV"
    const val INSERT_ID = "\$insert_id"
    const val IDENTIFY = "\$identify"
    const val TIME = "\$time"
    const val FCM_TOKEN_PUSH = "\$androidpush"
    const val PROPERTIES = "properties"
    const val EVENT = "EVENT"

    // System Event
    const val S_EVENT_APP_INSTALLED = "\$app_installed"
    const val S_EVENT_APP_LAUNCHED = "\$app_launched"
    const val S_EVENT_NOTIFICATION_DELIVERED = "\$notification_delivered"
    const val S_EVENT_NOTIFICATION_CLICKED = "\$notification_clicked"
    const val S_EVENT_NOTIFICATION_DISMISS = "\$notification_dismiss"
    const val S_EVENT_USER_LOGOUT = "\$user_logout"
    const val S_EVENT_PURCHASE_MADE = "\$purchase_made"
    const val S_EVENT_NOTIFICATION_SUBSCRIBED = "\$notification_subscribed"
    const val S_EVENT_NOTIFICATION_UNSUBSCRIBED = "\$notification_unsubscribed"
    const val S_EVENT_PAGE_VISITED = "\$page_visited"
}
