package app.suprsend.android.android

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.suprsend.android.Greeting
import app.suprsend.android.SuprSendApi
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*


fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {

    lateinit var suprSendApi: SuprSendApi

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
            suprSendApi.track("Product Viewed")
        }

        findViewById<View>(R.id.trackEventProperties).setOnClickListener {
            suprSendApi.track(
                "Product Viewed",
                JSONObject().apply {
                    put("Name", "Super Bike")
                    put("Price", 99.9)
                    put("Quantity", 45)
                    put("Availability", true)
                }
            )
        }

        findViewById<View>(R.id.flush).setOnClickListener {
            suprSendApi.reset()
        }
    }

    private fun initialize() {
        GlobalScope.launch((Dispatchers.IO)) {
            suprSendApi = SuprSendApi.getInstance(applicationContext, "123")
        }
    }
}
