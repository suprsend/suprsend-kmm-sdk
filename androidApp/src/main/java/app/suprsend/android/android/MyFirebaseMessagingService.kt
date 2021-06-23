package app.suprsend.android.android

import android.util.Log
import app.suprsend.android.notification.NotificationHelper
import app.suprsend.android.notification.NotificationVo
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Log.d(TAG, "From: ${remoteMessage.from}")

        val data = remoteMessage.data
        if (data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${data}")
            if (data.containsKey("supr_send_n_pl")) {
                val jsonStr = data.get("supr_send_n_pl")
                val notificationVo = Creator.gson.fromJson(jsonStr, NotificationVo::class.java)
                NotificationHelper.showNotification(applicationContext, notificationVo)
            }
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
    }


    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}