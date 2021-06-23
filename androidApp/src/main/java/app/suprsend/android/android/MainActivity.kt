package app.suprsend.android.android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.suprsend.android.Greeting
import app.suprsend.android.SuprSendAndroidConfig
import app.suprsend.android.SuprSendSdk
import app.suprsend.android.UserModel
import app.suprsend.android.database.DatabaseDriverFactory
import app.suprsend.android.user.Company
import app.suprsend.android.user.UserRepository
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {

    var count = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialize()

        addClickListeners()

        subscribeToTopic()
    }

    private fun subscribeToTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic("all_users")
            .addOnCompleteListener { task ->
                var msg = "Subscribed to topic all_users"
                if (!task.isSuccessful) {
                    msg = "Failed to subscribe to topic : all_users"
                }
                Log.d("firebase", msg)
                Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
            }

    }

    private fun addClickListeners() {
        //Todo : Move to data binding
        val greet: TextView = findViewById(R.id.greet)
        greet.text = greet()

        findViewById<View>(R.id.insertInDb).setOnClickListener {
            GlobalScope.launch() {
                val userRepository = UserRepository.getInstance()
                userRepository.insertUser(UserModel("${count++}", "Niks", Company("C1", "nik@gmail.com")))
            }
        }

        findViewById<View>(R.id.makeNwCall).setOnClickListener {
            GlobalScope.launch() {
                val userRepository = UserRepository.getInstance()
                Log.i("yep", "${userRepository.makeRemoteCall()}")
            }
        }

        findViewById<View>(R.id.notificationSample).setOnClickListener {
            startActivity(Intent(this@MainActivity, NotificationDemoActivity::class.java))
        }
    }

    private fun initialize() {
        GlobalScope.launch {
            SuprSendAndroidConfig.initialize(applicationContext)

            SuprSendSdk.initialize(DatabaseDriverFactory())

            val userRepository = UserRepository.getInstance()

            userRepository
                .getUsers()
                .collect { list ->
                    Log.i("yep", "${list.map { it.id }}")
                }
        }
    }
}
