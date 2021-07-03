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
import app.suprsend.android.base.AndroidCreator
import app.suprsend.android.R

internal object NotificationHelper {

    fun showRawNotification(context: Context, payloadJson: String) {
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

                val actionIcon = context.getDrawableIdFromName(notificationActionVo.iconDrawableName) ?: 0

                val actionIntent = NotificationRedirectionActivity.getIntent(context, notificationActionVo)

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

        val smallIcon = context.getDrawableIdFromName(notificationBasicVo.smallIconDrawableName) ?: R.drawable.ic_notification

        notificationBuilder.setSmallIcon(smallIcon)

        notificationBasicVo.subText?.let { subText ->
            notificationBuilder.setSubText(subText)
        }

        notificationBasicVo.showWhenTimeStamp?.let { showWhenTimeStamp ->
            notificationBuilder.setShowWhen(showWhenTimeStamp)
        }

        notificationBasicVo.whenTimeStamp?.let { whenTimeStamp ->
            notificationBuilder.setWhen(whenTimeStamp)
        }

        //Dismiss the notification on click?
        notificationBasicVo.autoCancel?.let { autoCancel ->
            notificationBuilder.setAutoCancel(autoCancel)
        }


        //Set the handler in the event that the notification is dismissed.
        val notificationDeleteIntent = NotificationRedirectionActivity.notificationDismissIntent(context, NotificationDismissVo(notificationVo.id))
        notificationDeleteIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val notificationDeletePI = PendingIntent.getActivity(context, System.currentTimeMillis().toInt(), notificationDeleteIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        notificationBuilder.setDeleteIntent(notificationDeletePI)

        //The category of the notification which allows android to prioritize the notification as required.
        notificationBasicVo.category?.let { category ->
            notificationBuilder.setCategory(category)
        }

        //Set the key by which this notification will be grouped.
        notificationBasicVo.group?.let { group ->
            notificationBuilder.setGroup(group)
        }

        //Set whether or not this notification is only relevant to the current device.
        notificationBasicVo.localOnly?.let { localOnly ->
            notificationBuilder.setLocalOnly(localOnly)
        }

        //Set whether this notification is sticky.
        notificationBasicVo.onGoing?.let { onGoing ->
            notificationBuilder.setOngoing(onGoing)
        }

        //The duration of time after which the notification is automatically dismissed.
        notificationBasicVo.timeoutAfter?.let { timeoutAfter ->
            notificationBuilder.setTimeoutAfter(timeoutAfter)
        }


        //notificationBuilder.setProgress(0,0,true)

//        notificationBuilder.addPerson(
//            Person
//                .Builder()
//                .setImportant(true)
//                .setName("")
//                .setIcon()
//                .setUri()
//                .build()
//        )


        try {
            // Todo : set big text / picture notification content intent
            val notificationActionVo = notificationVo.getDeeplinkNotificationActionVo()
            val contentIntent = NotificationRedirectionActivity.getIntent(context, notificationActionVo)
            contentIntent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            val contentPI = PendingIntent.getActivity(context, System.currentTimeMillis().toInt(), contentIntent, PendingIntent.FLAG_UPDATE_CURRENT)
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
        handleInboxStyleVo(notificationVo, builder)
        handleBigTextVo(notificationVo, builder)
        handleBigPictureVo(notificationVo, builder)
        handleMessagingStyleVo(notificationVo, builder)
    }

    private fun handleMessagingStyleVo(notificationVo: NotificationVo, builder: NotificationCompat.Builder) {
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

    private fun handleBigPictureVo(notificationVo: NotificationVo, builder: NotificationCompat.Builder) {
        val bigPictureVo = notificationVo.bigPictureVo ?: return
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

    private fun handleBigTextVo(notificationVo: NotificationVo, builder: NotificationCompat.Builder) {
        val bigTextVo = notificationVo.bigTextVo ?: return

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

    private fun handleInboxStyleVo(notificationVo: NotificationVo, builder: NotificationCompat.Builder) {
        val inboxStyleVo = notificationVo.inboxStyleVo ?: return

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


}

fun Context.getDrawableIdFromName(drawableName: String?): Int? {
    drawableName ?: return null
    return try {
        resources.getIdentifier(drawableName, "drawable", packageName)
    } catch (e: Exception) {
        null
    }
}