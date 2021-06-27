package app.suprsend.android.notification

import android.app.Activity
import android.app.NotificationManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import app.suprsend.android.log.Logger

class NotificationRedirectionActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            val activityExtras = intent.extras
            if (null == intent || activityExtras == null) {
                Logger.d(TAG, "meta data not received in $TAG")
                return
            }
            handleFlowPayload(activityExtras)
            finish()
        } catch (e: Exception) {
            Logger.e(TAG, "unable to handle meta data in $TAG")
            finish()
        }
    }

    private fun handleFlowPayload(activityExtras: Bundle) {
        if (activityExtras.containsKey(FLOW_NAME)) {
            when (activityExtras.get(FLOW_NAME) as? NotificationRedirection) {
                NotificationRedirection.NOTIFICATION_ACTION_CLICKED -> {
                    handleNotificationActionClicked(activityExtras)
                }
                else -> {
                    // do nothing
                    Logger.e(TAG, "payload not handled")
                }
            }
        } else {
            Logger.e(TAG, "payload not found")
        }
    }

    private fun handleNotificationActionClicked(activityExtras: Bundle) {
        val notificationActionVo = activityExtras.get(FLOW_PAYLOAD) as? NotificationActionVo
        notificationActionVo ?: return

        //Remove notification
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as? NotificationManager
        notificationManager?.cancel((notificationActionVo.notificationID ?: "").hashCode())

        //Target intent
        val link = notificationActionVo.link
        val notificationActionIntent = if (!link.isNullOrBlank()) {
            Intent(Intent.ACTION_VIEW, Uri.parse(link))
        } else {
            packageManager.getLaunchIntentForPackage(packageName)
        }
        notificationActionIntent?.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(notificationActionIntent)
    }

    companion object {
        const val TAG = "NotificationRedirectionActivity"
        const val FLOW_NAME = "flow_name"
        const val FLOW_PAYLOAD = "flow_payload"
    }

}

enum class NotificationRedirection {
    NOTIFICATION_ACTION_CLICKED
}