package app.suprsend.fcm

import android.util.Log
import app.suprsend.SSApi
import app.suprsend.coroutineExceptionHandler
import app.suprsend.notification.SSNotificationHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SSFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "FCM From : ${remoteMessage.from}")
        SSNotificationHelper.showFCMNotification(applicationContext, remoteMessage)
    }

    override fun onNewToken(token: String) {
        GlobalScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            Log.d(TAG, "FCM Token : $token")
            val instance = SSApi.getInstanceFromCachedApiKey()
            instance?.getUser()?.setAndroidPush(token)
        }
    }

    companion object {
        private const val TAG = "SS_FCM"
    }
}
