package app.suprsend.android.android

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.suprsend.android.Greeting
import app.suprsend.android.base.SuprSendAndroidApi
import app.suprsend.android.database.DatabaseDriverFactory
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialize()

        addClickListeners()

        subscribeToTopic()
    }

    private fun subscribeToTopic() {
        val topicName = "all_users"
        FirebaseMessaging.getInstance().subscribeToTopic(topicName)
            .addOnCompleteListener { task ->
                var msg = "Subscribed to topic $topicName"
                if (!task.isSuccessful) {
                    msg = "Failed to subscribe to topic : $topicName"
                }
                Log.d("firebase", msg)
                Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
            }

    }

    private fun addClickListeners() {
        //Todo : Move to data binding
        val greet: TextView = findViewById(R.id.greet)
        greet.text = greet()


        findViewById<View>(R.id.trackEventName).setOnClickListener {
            SuprSendAndroidApi.trackEvent("Product Viewed")
        }

        findViewById<View>(R.id.trackEventProperties).setOnClickListener {
            SuprSendAndroidApi.trackEvent(
                "Product Viewed",
                hashMapOf(
                    "Name" to "Super Bike",
                    "Price" to 99.9,
                    "Quantity" to 45,
                    "Availability" to true
                )
            )
        }

        findViewById<View>(R.id.flush).setOnClickListener {
            SuprSendAndroidApi.flush()
        }
    }

    private fun initialize() {
        GlobalScope.launch((Dispatchers.Main)) {
            SuprSendAndroidApi.initialize(
                applicationContext,
                DatabaseDriverFactory()
            )
        }
    }
}