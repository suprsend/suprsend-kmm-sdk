package app.suprsend.android.android

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.suprsend.android.Greeting
import app.suprsend.android.SuprSendApi
import app.suprsend.android.android.databinding.ActivityMainBinding
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

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initialize()

        addClickListeners()

        subscribeToTopic()
    }

    private fun subscribeToTopic() {
        val topicName = "all_users"
        FirebaseMessaging
            .getInstance()
            .subscribeToTopic(topicName)
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

        binding.greet.text = greet()


        binding.identify.setOnClickListener {
            suprSendApi.identify("1234")
        }


        binding.superProperty.setOnClickListener {
            suprSendApi.setSuperProperties(
                JSONObject().apply {
                    put("Super Property String", "123")
                    put("Super Property Int", 123)
                    put("Super Property Float", 123.43)
                    put("Super Property Boolean", false)
                }
            )
        }

        binding.trackEventName.setOnClickListener {
            suprSendApi.track("Product Viewed")
        }

        binding.trackEventProperties.setOnClickListener {
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

        binding.flush.setOnClickListener {
            suprSendApi.flush()
        }

        binding.crash.setOnClickListener {
            Integer.parseInt("a")
        }
    }

    private fun initialize() {
        GlobalScope.launch((Dispatchers.IO)) {
            suprSendApi = SuprSendApi.getInstance(applicationContext, "123")
        }
    }
}
