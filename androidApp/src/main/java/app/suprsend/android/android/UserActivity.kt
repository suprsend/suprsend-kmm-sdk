package app.suprsend.android.android

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.suprsend.android.android.databinding.ActivityUserBinding
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONObject

class UserActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserBinding

    private val ssUserApi = ssApi.getUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.set.setOnClickListener {
            ssUserApi.set("SS", "String value")
            ssUserApi.set("SB", true)
            ssUserApi.set("SI", 7653)
            ssUserApi.set("SF", 99.34)
        }
        binding.setP.setOnClickListener {
            val properties = JSONObject()
            properties.put("JSS", "String value")
            properties.put("JSB", true)
            properties.put("JSI", 7653)
            properties.put("JSF", 99.34)
            ssUserApi.set(properties)

        }
        binding.unSet.setOnClickListener {
            ssUserApi.unSet("SS")
            ssUserApi.unSet("SB")
            ssUserApi.unSet("SI")
            ssUserApi.unSet("SF")
        }
        binding.unSet.setOnClickListener {
            ssUserApi.unSet(
                listOf(
                    "JSS",
                    "JSB",
                    "JSI",
                    "JSF"
                )
            )
        }
        binding.setOnce.setOnClickListener {
            ssUserApi.setOnce("OS", "String value")
            ssUserApi.setOnce("OB", true)
            ssUserApi.setOnce("OI", 7653)
            ssUserApi.setOnce("OF", 99.34)
        }
        binding.setOnceP.setOnClickListener {
            val properties = JSONObject()
            properties.put("JOS", "String value")
            properties.put("JOB", true)
            properties.put("JOI", 7653)
            properties.put("JOF", 99.34)
            ssUserApi.setOnce(properties)
        }
        binding.increment.setOnClickListener {
            ssUserApi.increment("IS", "String value")
            ssUserApi.increment("IB", true)
            ssUserApi.increment("II", 7653)
            ssUserApi.increment("IF", 99.34)
        }
        binding.incrementP.setOnClickListener {
            val properties = JSONObject()
            properties.put("JIS", "String value")
            properties.put("JIB", true)
            properties.put("JII", 7653)
            properties.put("JIF", 99.34)
            ssUserApi.increment(properties)

        }
        binding.append.setOnClickListener {
            ssUserApi.append("AS", "String value")
            ssUserApi.append("AB", true)
            ssUserApi.append("AI", 7653)
            ssUserApi.append("AF", 99.34)

        }
        binding.remove.setOnClickListener {
            ssUserApi.remove("RS", "String value")
            ssUserApi.remove("RB", true)
            ssUserApi.remove("RI", 7653)
            ssUserApi.remove("RF", 99.34)

        }
        binding.setEmail.setOnClickListener {
            ssUserApi.setEmail("nikhilesh@suprsend.com")

        }
        binding.unSetEmail.setOnClickListener {
            ssUserApi.unSetEmail("nikhilesh@suprsend.com")

        }
        binding.setSms.setOnClickListener {
            ssUserApi.setSms("+918983364103")

        }
        binding.unSetSms.setOnClickListener {
            ssUserApi.unSetSms("+918983364103")

        }
        binding.setWhatsApp.setOnClickListener {
            ssUserApi.setWhatsApp("+918983364103")

        }
        binding.unSetWhatsApp.setOnClickListener {
            ssUserApi.unSetWhatsApp("+918983364103")

        }
        binding.setAndroidPush.setOnClickListener {
            FirebaseMessaging.getInstance().token.addOnCompleteListener {
                ssUserApi.setAndroidPush(it.result ?: "")
            }
        }
        binding.unSetAndroidPush.setOnClickListener {
            FirebaseMessaging.getInstance().token.addOnCompleteListener {
                ssUserApi.unSetAndroidPush(it.result ?: "")
            }
        }
        binding.reset.setOnClickListener {
            ssUserApi.reset()
        }
    }
}