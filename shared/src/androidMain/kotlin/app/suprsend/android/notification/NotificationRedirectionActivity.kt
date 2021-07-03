package app.suprsend.android.notification

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.core.os.bundleOf
import kotlinx.parcelize.Parcelize

class NotificationRedirectionActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            val activityExtras = intent.extras
            if (null == intent || activityExtras == null) {
                Log.d(TAG, "meta data not received in $TAG")
                return
            }
            handleFlowPayload(activityExtras)
            finish()
        } catch (e: Exception) {
            Log.e(TAG, "unable to handle meta data in $TAG")
            finish()
        }
    }

    private fun handleFlowPayload(activityExtras: Bundle) {
        if (activityExtras.containsKey(FLOW_NAME)) {
            when (activityExtras.get(FLOW_NAME) as? NotificationRedirection) {
                NotificationRedirection.NOTIFICATION_ACTION_CLICKED -> {
                    handleNotificationActionClicked(activityExtras)
                }
                NotificationRedirection.NOTIFICATION_DISMISS -> {
                    Log.i(TAG, "Notification dismissed")
                }
                else -> {
                    // do nothing
                    Log.e(TAG, "payload not handled")
                }
            }
        } else {
            Log.e(TAG, "payload not found")
        }
    }

    private fun handleNotificationActionClicked(activityExtras: Bundle) {
        val notificationActionVo = activityExtras.get(FLOW_PAYLOAD) as? NotificationActionVo
        notificationActionVo ?: return

        //Remove notification
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as? NotificationManager
        notificationManager?.cancel((notificationActionVo.id ?: "").hashCode())

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
        private const val TAG = "NRA"
        private const val FLOW_NAME = "flow_name"
        private const val FLOW_PAYLOAD = "flow_payload"

        fun getIntent(context: Context, notificationActionVo: NotificationActionVo? = null): Intent? {
            if (notificationActionVo?.link == null) {
                return context.packageManager.getLaunchIntentForPackage(context.packageName)
            }
            return Intent()
                .setClass(context, NotificationRedirectionActivity::class.java)
                .putExtras(
                    bundleOf(
                        FLOW_NAME to NotificationRedirection.NOTIFICATION_ACTION_CLICKED,
                        FLOW_PAYLOAD to notificationActionVo,
                    )
                )
        }

        fun notificationDismissIntent(context: Context, notificationDismissVo: NotificationDismissVo): Intent {
            return Intent()
                .setClass(context, NotificationRedirectionActivity::class.java)
                .putExtras(
                    bundleOf(
                        FLOW_NAME to NotificationRedirection.NOTIFICATION_DISMISS,
                        FLOW_PAYLOAD to notificationDismissVo,
                    )
                )
        }
    }

}

enum class NotificationRedirection {
    NOTIFICATION_ACTION_CLICKED, NOTIFICATION_DISMISS
}

@Parcelize
data class NotificationDismissVo(
    val id: String
) : Parcelable