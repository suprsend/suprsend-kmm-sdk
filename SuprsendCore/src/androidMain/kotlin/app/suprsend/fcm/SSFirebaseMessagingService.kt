package app.suprsend.fcm

import android.util.Log
import app.suprsend.notification.SSNotificationHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class SSFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "FCM From : ${remoteMessage.from}")
        SSNotificationHelper.showFCMNotification(applicationContext, remoteMessage)
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "FCM Token : $token")
//        GlobalScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
//            val instance = SSApi.getInstanceFromCachedApiKey()
//            instance?.getUser()?.setAndroidFcmPush(token)
//        }
    }

    companion object {
        const val TAG = "push_fcm"
    }
}
