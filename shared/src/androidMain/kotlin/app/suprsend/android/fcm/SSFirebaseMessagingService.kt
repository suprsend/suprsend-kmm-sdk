package app.suprsend.android.fcm

import android.util.Log
import app.suprsend.android.SSApi
import app.suprsend.android.base.SSConstants
import app.suprsend.android.notification.NotificationHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SSFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        try {
            Log.d(TAG, "FCM From : ${remoteMessage.from}")
            val data = remoteMessage.data
            if (data.isNotEmpty()) {
                Log.d(TAG, "Message data payload: $data")
                if (data.containsKey(SSConstants.NOTIFICATION_PAYLOAD)) {
                    GlobalScope.launch(Dispatchers.IO) {
                        val rawNotification = NotificationHelper.getRawNotification(payloadJson = data[SSConstants.NOTIFICATION_PAYLOAD] ?: "")
                        NotificationHelper.showRawNotification(context = applicationContext, rawNotification = rawNotification)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onNewToken(token: String) {
        GlobalScope.launch(Dispatchers.IO) {
            Log.d(TAG, "FCM Token : $token")
            val instance = SSApi.getInstanceFromCachedApiKey(baseContext)
            instance?.getUser()?.setAndroidPush(token)
        }
    }

    companion object {
        private const val TAG = "SS_FCM"
    }
}
