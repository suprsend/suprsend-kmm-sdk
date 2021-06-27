package app.suprsend.android.notification

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class RawNotification(
    val id: String,

    //Channel Details
    val channelId: String?,
    val channelName: String?,
    val channelDescription: String?,
    val channelShowBadge: Boolean?,
    val channelVisibility: NotificationChannelVisibility?,
    val channelImportance: NotificationChannelImportance?,

    //Notification Details
    val notificationTitle: String?,
    val shortDescription: String?,
    val longDescription: String?,
    val iconUrl: String?,
    val imageUrl: String? = null,

    //Actions
    val actions: List<NotificationActionVo>? = null

) {
    fun getNotificationVo(): NotificationVo {
        var notificationVo = NotificationVo(
            id = id,
            notificationChannelVo = NotificationChannelVo(
                id = channelId ?: "default_channel",
                name = channelName ?: "Default Channel",
                description = channelDescription ?: "",
                showBadge = channelShowBadge ?: true,
                visibility = channelVisibility ?: NotificationChannelVisibility.PUBLIC,
                importance = channelImportance ?: NotificationChannelImportance.HIGH
            ),
            notificationBasicVo = NotificationBasicVo(
                contentTitle = notificationTitle ?: "",
                contentText = shortDescription ?: "",
            ),
            actions = actions?.map { notificationActionVo -> notificationActionVo.copy(notificationID = id) }
        )

        notificationVo = if (!imageUrl.isNullOrBlank()) {
            notificationVo.copy(
                bigPictureVo = BigPictureVo(
                    bigContentTitle = notificationTitle ?: "",
                    summaryText = shortDescription ?: "",
                    bigPictureUrl = imageUrl,
                    largeIconUrl = iconUrl
                )
            )
        } else {
            notificationVo.copy(
                bigTextVo = BigTextVo(
                    title = notificationTitle ?: "",
                    contentText = shortDescription ?: "",
                    bigContentTitle = notificationTitle ?: "",
                    bigText = longDescription ?: ""
                )
            )
        }

        return notificationVo
    }
}


data class NotificationVo(
    val id: String,
    val notificationChannelVo: NotificationChannelVo,
    val notificationBasicVo: NotificationBasicVo,
    val bigTextVo: BigTextVo? = null,
    val bigPictureVo: BigPictureVo? = null,
    val inboxStyleVo: InBoxStyleVo? = null,
    val actions: List<NotificationActionVo>? = null
)

@Parcelize
data class NotificationActionVo(
    val notificationID: String?,
    val title: String? = null,
    val link: String? = null,
    val iconDrawableName: String? = null
) : Parcelable

data class NotificationChannelVo(
    val id: String,
    val name: String,
    val description: String,
    val showBadge: Boolean,
    val visibility: NotificationChannelVisibility = NotificationChannelVisibility.PUBLIC,
    val importance: NotificationChannelImportance = NotificationChannelImportance.HIGH
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