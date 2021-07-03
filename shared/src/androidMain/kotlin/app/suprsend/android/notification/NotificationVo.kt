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
    val color: String?,
    val notificationTitle: String?,
    val subText: String?,
    val shortDescription: String?,
    val longDescription: String?,
    val iconUrl: String?,
    val imageUrl: String? = null,
    val smallIconDrawableName: String? = null,
    val category: String? = null,
    val group: String? = null,
    val autoCancel: Boolean? = null,
    val showWhenTimeStamp: Boolean? = null,
    val localOnly: Boolean? = null,
    val onGoing: Boolean? = null,
    val timeout: Long? = null,

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
                largeIconUrl = iconUrl,
                color = color,
                subText = subText,
                showWhenTimeStamp = showWhenTimeStamp,
                autoCancel = autoCancel,
                smallIconDrawableName = smallIconDrawableName,
                category = category,
                group = group,
                localOnly = localOnly,
                onGoing = onGoing,
                timeout = timeout,
            ),
            actions = actions
                ?.map { notificationActionVo ->
                    if (notificationActionVo.id == null)
                        notificationActionVo
                            .copy(
                                id = id
                            )
                    else notificationActionVo
                }
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
    val actions: List<NotificationActionVo>? = null,
    val deeplink: String? = null
) {
    fun getDeeplinkNotificationActionVo(): NotificationActionVo? {
        return if (deeplink == null)
            null
        else
            NotificationActionVo(id = id, link = deeplink)
    }
}

@Parcelize
data class NotificationActionVo(
    val id: String?,
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
    //#000000
    val color: String? = null,
    val contentTitle: String,
    val subText: String? = null,
    val contentText: String,
    val largeIconUrl: String? = null,
    val showWhenTimeStamp: Boolean? = null,
    val autoCancel: Boolean? = null,
    val smallIconDrawableName: String? = null,
    val category: String? = null,
    val group: String? = null,
    val localOnly: Boolean? = null,
    val onGoing: Boolean? = null,
    val timeout: Long? = null
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