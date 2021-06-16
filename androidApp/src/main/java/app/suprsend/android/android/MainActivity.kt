package app.suprsend.android.android

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import app.suprsend.android.Greeting
import android.widget.TextView
import app.suprsend.android.SuprSendAndroidConfig
import app.suprsend.android.Company
import app.suprsend.android.DatabaseDriverFactory
import app.suprsend.android.SuprSendSdk
import app.suprsend.android.UserModel
import app.suprsend.android.UserRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

fun greet(): String {
    return Greeting().greeting()
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv: TextView = findViewById(R.id.text_view)
        tv.text = greet()

        tv.setOnClickListener {
            GlobalScope.launch {
                val userRepository = UserRepository.getInstance()
                userRepository.insertUser(UserModel("1", "Niks", Company("C1", "nik@gmail.com")))
            }
        }



        GlobalScope.launch {
            SuprSendAndroidConfig.initialize(applicationContext)

            SuprSendSdk.initialize(DatabaseDriverFactory())

            val userRepository = UserRepository.getInstance()

            userRepository
                .getUsers()
                .collect { list ->

                }

            Log.i("yep", "${userRepository.makeRemoteCall()}")
        }
    }
}
