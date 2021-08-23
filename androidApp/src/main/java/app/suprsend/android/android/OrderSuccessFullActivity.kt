package app.suprsend.android.android

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.suprsend.android.android.databinding.ActivityOrderSuccessFullBinding
import app.suprsend.android.base.getReadableDate
import org.json.JSONObject

class OrderSuccessFullActivity : AppCompatActivity() {

    lateinit var binding: ActivityOrderSuccessFullBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderSuccessFullBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val productVo: ProductVo = intent!!.getParcelableExtra("product")!!

        val orderId = (1..100).random()
        binding.orderIdTv.text = "Your Order id is $orderId"
        binding.orderAmount.text = "Amount : ${productVo.amount}/- Rs"
        binding.goToHomeTv.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        CommonAnalyticsHandler.increment(
            mutableMapOf(
                "order_count" to 1,
                "amount" to productVo.amount
            )
        )

        CommonAnalyticsHandler.purchaseMade(JSONObject().apply {
            put("email", AppCreator.getEmail(this@OrderSuccessFullActivity))
            put("product_id", productVo.id)
            put("product_name", productVo.title)
            put("amount", productVo.amount)
        })

        CommonAnalyticsHandler.setOnce(JSONObject().apply {
            put("first_ordered_at", getReadableDate())
            put("first_ordered_amount", productVo.amount)
            put("first_ordered_product_name", productVo.title)
        })
    }

    override fun onStart() {
        super.onStart()
        CommonAnalyticsHandler.track("order_success_screen_viewed")
    }
}
