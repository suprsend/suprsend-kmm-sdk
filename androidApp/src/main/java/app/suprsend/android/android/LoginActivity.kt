package app.suprsend.android.android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.suprsend.android.android.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginTv.setOnClickListener {
            Creator.email = binding.emailEt.text.toString()
            Creator.password = binding.passordEt.text.toString()
            ssApi.identify(Creator.email)
            mixpanelAPI.identify(Creator.email)
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
