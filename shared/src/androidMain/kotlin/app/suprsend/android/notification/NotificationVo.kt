package app.suprsend.android.notification

data class NotificationVo(
    val id: String,
    val notificationChannelVo: NotificationChannelVo,
    val notificationBasicVo: NotificationBasicVo,
    val inboxStyleVo: InBoxStyleVo? = null,
    val bigTextVo: BigTextVo? = null,
    val bigPictureVo: BigPictureVo? = null
)

data class NotificationChannelVo(
    val id: String,
    val name: String,
    val description: String,
    val showBadge: Boolean,
    val visibility: NotificationChannelVisibility,
    val importance: NotificationChannelImportance
)

enum class NotificationChannelVisibility {
    PUBLIC, PRIVATE, SECRET
}

enum class NotificationChannelImportance {
    HIGH, LOW, MAX, MIN, DEFAULT
}

data class NotificationBasicVo(
    val contentTitle: String,
    val contentText: String,
    val largeIconUrl: String? = null,
    //#000000
    val color: String? = null,
    val subText: String? = null,
    val showWhenTimeStamp: Boolean? = null,
)

data class BigTextVo(
    val title: String? = null,
    val contentText: String? = null,
    val summaryText: String? = null,
    val bigContentTitle: String? = null,
    val bigText: String? = null
)

data class BigPictureVo(
    val bigContentTitle: String? = null,
    val summaryText: String? = null,
    val bigPictureUrl: String? = null,
    val largeIconUrl: String? = null
)

data class InBoxStyleVo(
    val bigContentTitle: String? = null,
    val summaryText: String? = null,
    val lines: List<String>? = null
)