package app.suprsend.android.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import app.suprsend.android.base.AndroidCreator
import app.suprsend.android.R

internal object NotificationHelper {

    suspend fun showRawNotification(context: Context, payloadJson: String) {
        try {
            val rawNotification = AndroidCreator.gson.fromJson(payloadJson, RawNotification::class.java)
            showNotificationInternal(context, rawNotification.getNotificationVo())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showNotificationInternal(context: Context, notificationVo: NotificationVo) {

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        setChannel(notificationManager, notificationVo.notificationChannelVo)

        val notificationBuilder = NotificationCompat.Builder(context, notificationVo.notificationChannelVo.id)

        setBasicVo(context, notificationBuilder, notificationVo)

        setStyle(notificationBuilder, notificationVo)

        setNotificationAction(context, notificationBuilder, notificationVo)

        notificationBuilder.build()

        notificationManager.notify(notificationVo.id.hashCode(), notificationBuilder.build())
    }

    private fun setNotificationAction(context: Context, notificationBuilder: NotificationCompat.Builder, notificationVo: NotificationVo) {

        try {
            notificationVo.actions?.forEachIndexed { index, notificationActionVo ->

                val actionIcon = try {
                    context.resources.getIdentifier(notificationActionVo.iconDrawableName, "drawable", context.packageName)
                } catch (e: Exception) {
                    e.printStackTrace()
                    0
                }
                val actionLink = notificationActionVo.link
                val actionIntent = if (actionLink.isNullOrBlank()) {
                    context.packageManager.getLaunchIntentForPackage(context.packageName)
                } else {
                    Intent(context, NotificationRedirectionActivity::class.java)
                        .setClass(context, NotificationRedirectionActivity::class.java)
                        .putExtras(
                            bundleOf(
                                NotificationRedirectionActivity.FLOW_NAME to NotificationRedirection.NOTIFICATION_ACTION_CLICKED,
                                NotificationRedirectionActivity.FLOW_PAYLOAD to notificationActionVo,
                            )
                        )
                        .setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                }

                actionIntent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP

                notificationBuilder.addAction(
                    actionIcon,
                    notificationActionVo.title,
                    PendingIntent.getActivity(
                        context,
                        (System.currentTimeMillis() + index).toInt(),
                        actionIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun setBasicVo(context: Context, notificationBuilder: NotificationCompat.Builder, notificationVo: NotificationVo) {
        val notificationBasicVo = notificationVo.notificationBasicVo

        notificationBuilder.setChannelId(notificationVo.notificationChannelVo.id)


        notificationBasicVo.contentTitle.let { contentTitle ->
            notificationBuilder.setContentTitle(contentTitle)
        }

        notificationBasicVo.contentText.let { contentText ->
            notificationBuilder.setContentText(contentText)
        }

        notificationBasicVo.largeIconUrl?.let { largeIconUrl ->
            if (largeIconUrl.isNotBlank())
                notificationBuilder.setLargeIcon(BitmapHelper.getBitmapFromUrl(largeIconUrl))
        }


        notificationBasicVo.color?.let { stringColorCode ->
            if (stringColorCode.isNotBlank())
                notificationBuilder.color = Color.parseColor(stringColorCode)
        }

//        val smallIcon = context.applicationInfo.icon
        notificationBuilder.setSmallIcon(R.drawable.ic_notification)

        notificationBasicVo.subText?.let { subText ->
            notificationBuilder.setSubText(subText)
        }

        notificationBasicVo.showWhenTimeStamp?.let { showWhenTimeStamp ->
            notificationBuilder.setShowWhen(showWhenTimeStamp)
        }

        //Dismiss the notification on click?
        //.setAutoCancel(payload.meta.cancelOnClick)

        //Set the click handler for the notifications
        //.setContentIntent(payload.meta.clickIntent)

        //Set the handler in the event that the notification is dismissed.
        //.setDeleteIntent(payload.meta.clearIntent)

        //The category of the notification which allows android to prioritize the notification as required.
        //.setCategory(payload.meta.category)

        //Set the key by which this notification will be grouped.
        //.setGroup(payload.meta.group)

        //Set whether or not this notification is only relevant to the current device.
        //.setLocalOnly(payload.meta.localOnly)

        //Set whether this notification is sticky.
        //.setOngoing(payload.meta.sticky)

        //The duration of time after which the notification is automatically dismissed.
        //.setTimeoutAfter(payload.meta.timeout)


        //builder.setProgress(0,0,true)

        // builder.addPerson(it)


        try {
            // Todo : set big text / picture notification content intent
            val contentIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
            contentIntent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            val contentPI = PendingIntent.getBroadcast(context, System.currentTimeMillis().toInt(), contentIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            notificationBuilder.setContentIntent(contentPI)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun setChannel(notificationManager: NotificationManager, notificationChannelVo: NotificationChannelVo): Boolean {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return false
        }

        notificationManager.getNotificationChannel(notificationChannelVo.id)?.run {
            return true
        }

        val importance = when (notificationChannelVo.importance) {
            NotificationChannelImportance.HIGH -> NotificationManager.IMPORTANCE_HIGH
            NotificationChannelImportance.LOW -> NotificationManager.IMPORTANCE_LOW
            NotificationChannelImportance.MAX -> NotificationManager.IMPORTANCE_MAX
            NotificationChannelImportance.MIN -> NotificationManager.IMPORTANCE_MIN
            NotificationChannelImportance.DEFAULT -> NotificationManager.IMPORTANCE_DEFAULT
        }

        val notificationChannel = NotificationChannel(notificationChannelVo.id, notificationChannelVo.name, importance).apply {
            description = notificationChannelVo.description
            lockscreenVisibility = when (notificationChannelVo.visibility) {
                NotificationChannelVisibility.PUBLIC -> {
                    Notification.VISIBILITY_PUBLIC
                }
                NotificationChannelVisibility.PRIVATE -> {
                    Notification.VISIBILITY_PRIVATE
                }
                NotificationChannelVisibility.SECRET -> {
                    Notification.VISIBILITY_SECRET
                }
            }
            setShowBadge(notificationChannelVo.showBadge)
        }
        notificationManager.createNotificationChannel(notificationChannel)
        return true
    }

    private fun setStyle(builder: NotificationCompat.Builder, notificationVo: NotificationVo) {


        val inboxStyleVo = notificationVo.inboxStyleVo

        if (inboxStyleVo != null) {
            val inboxStyle = NotificationCompat.InboxStyle()

            inboxStyleVo.bigContentTitle?.let { bigContentTitle ->
                inboxStyle.setBigContentTitle(bigContentTitle)
            }

            inboxStyleVo.summaryText?.let { summaryText ->
                inboxStyle.setSummaryText(summaryText)
            }

            inboxStyleVo.lines?.forEach { line ->
                inboxStyle.addLine(line)
            }

            builder.setStyle(inboxStyle)
        }


        val bigTextVo = notificationVo.bigTextVo
        if (bigTextVo != null) {

            val bigTextStyle = NotificationCompat.BigTextStyle()

            bigTextVo.bigContentTitle?.let { bigContentTitle ->
                bigTextStyle.setBigContentTitle(bigContentTitle)
            }

            bigTextVo.summaryText?.let { summaryText ->
                bigTextStyle.setSummaryText(summaryText)
            }

            bigTextVo.bigText?.let { bigText ->
                bigTextStyle.bigText(bigText)
            }

            builder.setStyle(bigTextStyle)
        }


        val bigPictureVo = notificationVo.bigPictureVo
        if (bigPictureVo != null) {
            //Big Picture
            val bigPictureStyle = NotificationCompat.BigPictureStyle()

            bigPictureVo.bigContentTitle?.let { bigContentTitle ->
                bigPictureStyle.setBigContentTitle(bigContentTitle)
            }

            bigPictureVo.summaryText?.let { summaryText ->
                bigPictureStyle.setSummaryText(summaryText)
            }

            bigPictureVo.bigPictureUrl?.let { bigPictureUrl ->
                bigPictureStyle.bigPicture(BitmapHelper.getBitmapFromUrl(bigPictureUrl))
            }

            bigPictureVo.largeIconUrl?.let { largeIconUrl ->
                bigPictureStyle.bigLargeIcon(BitmapHelper.getBitmapFromUrl(largeIconUrl))
            }

            builder.setStyle(bigPictureStyle)

        }


//        NotificationCompat
//            .MessagingStyle(
//                Person
//                    .Builder()
//                    .setImportant(true)
//                    .setName("")
//                    //.setIcon()
//                    //.setUri()
//                    .build()
//            )
//
//            .setConversationTitle(content.conversationTitle)
//            .also { s ->
//                content.messages.forEach { s.addMessage(it.text, it.timestamp, it.sender) }
//            }

    }

    suspend fun showTextNotification(context: Context) {
        showNotificationInternal(
            context,
            NotificationVo(
                id = "showTextNotification",
                notificationChannelVo = NotificationChannelVo(
                    id = "Channel Id",
                    name = "Channel Name",
                    description = "Channel Description",
                    showBadge = true,
                    visibility = NotificationChannelVisibility.PUBLIC,
                    importance = NotificationChannelImportance.HIGH
                ),
                notificationBasicVo = NotificationBasicVo(
                    contentTitle = "Content Title",
                    contentText = "Content Text",
                    largeIconUrl = "https://niksdevelop.herokuapp.com/images/346kb.jpg",
                    color = "#FF0000",
                    subText = "Sub Text",
                    showWhenTimeStamp = true
                ),
                bigTextVo = BigTextVo(
                    title = "Title",
                    contentText = "Content Text",
                    summaryText = "Summary Text",
                    bigContentTitle = "Big Content Title",
                    bigText = "Big Text"
                )
            )
        )
    }

    suspend fun showImageNotification(context: Context) {
        showNotificationInternal(
            context,
            NotificationVo(
                id = "showImageNotification",
                notificationChannelVo = NotificationChannelVo(
                    id = "Channel Id",
                    name = "Channel Name",
                    description = "Channel Description",
                    showBadge = true,
                    visibility = NotificationChannelVisibility.PUBLIC,
                    importance = NotificationChannelImportance.HIGH
                ),
                notificationBasicVo = NotificationBasicVo(
                    contentTitle = "Content Title",
                    contentText = "Content Text",
                    largeIconUrl = "https://niksdevelop.herokuapp.com/images/346kb.jpg",
                    color = "#FF0000",
                    subText = "Sub Text",
                    showWhenTimeStamp = true
                ),
                bigPictureVo = BigPictureVo(
                    bigContentTitle = "Big Content Title",
                    summaryText = "Summary Text",
                    bigPictureUrl = "https://niksdevelop.herokuapp.com/images/346kb.jpg",
                    largeIconUrl = "https://niksdevelop.herokuapp.com/images/346kb.jpg",
                )
            )
        )
    }
}