package app.suprsend.android.base

internal object EventSanitation {

    fun processKey(value:String): String {
        return if (value.length > SuprSendConstants.EVENT_KEY_MAX_LENGTH) {
            value.substring(0, SuprSendConstants.EVENT_KEY_MAX_LENGTH - 1)
        } else {
            value
        }
    }

    fun processValue(value:String): String {
        return if (value.length > SuprSendConstants.EVENT_VALUE_MAX_LENGTH) {
            value.substring(0, SuprSendConstants.EVENT_VALUE_MAX_LENGTH - 1)
        } else {
            value
        }
    }
}