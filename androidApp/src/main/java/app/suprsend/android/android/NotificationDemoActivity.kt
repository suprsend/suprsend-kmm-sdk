package app.suprsend.android.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import app.suprsend.android.notification.NotificationHelper

class NotificationDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_demo)

        setClickListeners()
    }

    private fun setClickListeners() {
        findViewById<View>(R.id.textNotification).setOnClickListener {
            NotificationHelper.showTextNotification(this)
        }

        findViewById<View>(R.id.imageNotification).setOnClickListener {
            NotificationHelper.showImageNotification(this)
        }
    }
}