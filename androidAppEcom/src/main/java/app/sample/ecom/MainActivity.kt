package app.sample.ecom

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.sample.ecom.databinding.ActivityMainBinding
import app.suprsend.android.SSApi
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*

class MainActivity : AppCompatActivity() {

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
            ssApi.identify("1234")
        }


        binding.superProperty.setOnClickListener {
            ssApi.setSuperProperties(
                JSONObject().apply {
                    put("Super Property String", "123")
                    put("Super Property Int", 123)
                    put("Super Property Float", 123.43)
                    put("Super Property Boolean", false)
                }
            )
        }

        binding.trackEventName.setOnClickListener {
            ssApi.track("Product Viewed")
        }

        binding.trackEventProperties.setOnClickListener {
            ssApi.track(
                "Product Viewed",
                JSONObject().apply {
                    put("Name", "Super Bike")
                    put("Price", 99.9)
                    put("Quantity", 45)
                    put("Availability", true)
                }
            )
        }

        binding.userEvents.setOnClickListener {
            startActivity(Intent(this, UserActivity::class.java))
        }

        binding.flush.setOnClickListener {
            ssApi.flush()
        }

        binding.crash.setOnClickListener {
            Integer.parseInt("a")
        }
    }

    private fun initialize() {
        GlobalScope.launch((Dispatchers.IO)) {
            ssApi = SSApi.getInstance(applicationContext, "123")
        }
    }
}

lateinit var ssApi: SSApi