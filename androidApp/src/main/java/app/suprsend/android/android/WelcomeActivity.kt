package app.suprsend.android.android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import app.suprsend.android.SSApi
import app.suprsend.android.android.databinding.ActivityWelcomeBinding
import app.suprsend.android.android.databinding.ItemImageBinding
import app.suprsend.android.base.LogLevel
import com.google.firebase.messaging.FirebaseMessaging
import com.mixpanel.android.mpmetrics.MixpanelAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class WelcomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Creator.email.isNotBlank()) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeSdk()

        binding.viewPager.adapter = WelcomeAdapter(
            layoutInflater, (1..10).map { index ->
                WelcomeVo(
                    id = index.toString(),
                    url = Creator.getRandomImage(index)
                )
            }
        )

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                binding.pageNumTv.text = "Page $position"
                mixpanelAPI.track("Walk Through Viewed $position")
                mixpanelAPI.people.set("amount_o", position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        binding.loginTv.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
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
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
    }

    private fun initializeSdk() {
        GlobalScope.launch((Dispatchers.IO)) {
            ssApi = SSApi.getInstance(applicationContext, "kfWdrPL1nFqs7OUihiBn")
            ssApi.setLogLevel(LogLevel.VERBOSE)
            mixpanelAPI = MixpanelAPI.getInstance(applicationContext, "ed91e8fcdae7f6bd2b379a0785a06544")
        }
    }
}

data class WelcomeVo(
    val id: String,
    val url: String
)

class WelcomeAdapter(
    private val layoutInflater: LayoutInflater,
    private val list: List<WelcomeVo>
) : PagerAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as View
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = ItemImageBinding.inflate(layoutInflater)
        Creator.loadUrl(
            container.context,
            list[position].url,
            itemView.imageIV
        )
        container.addView(itemView.root)
        return itemView.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
