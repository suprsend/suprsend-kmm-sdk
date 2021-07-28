package app.suprsend.android.fcm

import android.util.Log
import app.suprsend.android.base.SSConstants
import app.suprsend.android.notification.NotificationHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SSFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Log.d(TAG, "From: ${remoteMessage.from}")

        val data = remoteMessage.data
        if (data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: $data")
            if (data.containsKey(SSConstants.NOTIFICATION_PAYLOAD)) {
                GlobalScope.launch(Dispatchers.IO) {
                    NotificationHelper.showRawNotification(applicationContext, data[SSConstants.NOTIFICATION_PAYLOAD] ?: "")
                }
            }
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
    }

    companion object {
        private const val TAG = "SS_FCM"
    }
}
