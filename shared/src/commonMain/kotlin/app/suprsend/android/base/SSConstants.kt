package app.suprsend.android.base

internal object SSConstants {

    const val DEFAULT_BASE_API_URL = "https://hub.suprsend.com"
    const val IMAGE_KIT_BASE_PATH = "https://ik.imagekit.io/l0quatz6utm"

    const val NOTIFICATION_PAYLOAD = "supr_send_n_pl"
    const val EVENT_KEY_MAX_LENGTH = 120
    const val EVENT_VALUE_MAX_LENGTH = 512
    const val FLUSH_EVENT_PAYLOAD_SIZE = 20L

    // Config Helper Keys
    const val CONFIG_API_KEY = "api_key"
    const val CONFIG_SECRET = "app_secret"
    const val CONFIG_USER_ID = "user_id"
    const val CONFIG_IS_APP_LAUNCHED = "is_app_launched"
    const val CONFIG_FCM_PUSH_TOKEN = "fcm_push_token"
    const val CONFIG_DEVICE_ID = "device_id"
    const val CONFIG_API_BASE_URL = "api_url"

    // Properties
    const val IDENTIFIED_ID = "\$identified_id"
    const val ANONYMOUS_ID = "\$anon_id"
    const val DISTINCT_ID = "distinct_id"
    const val INSERT_ID = "\$insert_id"
    const val IDENTIFY = "\$identify"
    const val TIME = "\$time"
    const val FCM_TOKEN_PUSH = "\$androidpush"
    const val DEVICE_ID = "\$deviceId"
    const val SET = "\$set"
    const val SET_ONCE = "\$set_once"
    const val ADD = "\$add"
    const val APPEND = "\$append"
    const val REMOVE = "\$remove"
    const val UNSET = "\$unset"
    const val EMAIL = "\$email"
    const val SMS = "\$sms"
    const val WHATS_APP = "\$whatsapp"

    const val ENV = "env"
    const val PROPERTIES = "properties"
    const val EVENT = "event"

    // System Event
    const val S_EVENT_APP_INSTALLED = "\$app_installed"
    const val S_EVENT_APP_LAUNCHED = "\$app_launched"
    const val S_EVENT_NOTIFICATION_DELIVERED = "\$notification_delivered"
    const val S_EVENT_NOTIFICATION_CLICKED = "\$notification_clicked"
    const val S_EVENT_NOTIFICATION_DISMISS = "\$notification_dismiss"
    const val S_EVENT_USER_LOGIN = "\$user_login"
    const val S_EVENT_USER_LOGOUT = "\$user_logout"
    const val S_EVENT_PURCHASE_MADE = "\$purchase_made"
    const val S_EVENT_NOTIFICATION_SUBSCRIBED = "\$notification_subscribed"
    const val S_EVENT_NOTIFICATION_UNSUBSCRIBED = "\$notification_unsubscribed"
    const val S_EVENT_PAGE_VISITED = "\$page_visited"
}
