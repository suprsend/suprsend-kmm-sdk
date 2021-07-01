package app.suprsend.android.fcm

import android.util.Log
import app.suprsend.android.base.SuprSendConstants
import app.suprsend.android.notification.NotificationHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

//Todo : Rename me
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Log.d(TAG, "From: ${remoteMessage.from}")

        val data = remoteMessage.data
        if (data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: $data")
            if (data.containsKey(SuprSendConstants.NOTIFICATION_PAYLOAD)) {
                GlobalScope.launch {
                    NotificationHelper.showRawNotification(applicationContext, data[SuprSendConstants.NOTIFICATION_PAYLOAD] ?: "")
                }
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