package app.suprsend.android.android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import app.suprsend.android.android.databinding.ActivityProductDetailsBinding
import org.json.JSONObject

class ProductDetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityProductDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (AppCreator.getEmail(this).isBlank()) {
            startActivity(Intent(this, WelcomeActivity::class.java))
            finishAffinity()
            return
        }

        binding = ActivityProductDetailsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val productId = intent?.getStringExtra("productId") ?: "P1"

        val productVo: ProductVo = AppCreator.homeItemsList.find { item -> item.getItemId() == productId } as ProductVo

        binding.obj = productVo

        AppCreator.loadUrl(this, productVo.url, binding.imageIV)

        binding.executePendingBindings()

        CommonAnalyticsHandler.track("Product Viewed", JSONObject().apply {
            put("Product ID", productVo.id)
            put("Product Name", productVo.title)
            put("Amount", productVo.amount)
        })

        binding.buyNow.setOnClickListener {
            val intent = Intent(this, PlaceOrderActivity::class.java)
            intent.putExtra("product", productVo)
            startActivity(intent)
        }
        binding.thumbUp.setOnClickListener {
            myToast("Called : append : choices")
            CommonAnalyticsHandler.append("choices", productVo.title)
        }
        binding.thumbDown.setOnClickListener {
            myToast("Called : remove : choices")
            CommonAnalyticsHandler.remove("choices", productVo.title)
        }
    }
}
