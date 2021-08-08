package app.suprsend.android.android

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import app.suprsend.android.android.databinding.ActivityHomeBinding
import app.suprsend.android.android.databinding.GridProductImageBinding

class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.gridView.adapter = HomeAdapter(
            layoutInflater,
            (1..30).map { index ->
                ProductVo(
                    id = index.toString(),
                    url = Creator.getRandomImage(index)
                )
            }
        )

        binding.gridView.setOnItemClickListener { parent, view, position, id ->
            mixpanelAPI.track("Item Clicked $position")
            mixpanelAPI.people.set("amount_i", position)
        }
        binding.logoutTv.setOnClickListener {
            ssApi.reset()
            mixpanelAPI.reset()
            Creator.email = ""
            Creator.password = ""
            startActivity(Intent(this, WelcomeActivity::class.java))
            finishAffinity()
        }
    }
}

data class ProductVo(
    val id: String,
    val url: String
)

class HomeAdapter
constructor(
    private val layoutInflater: LayoutInflater,
    private val list: List<ProductVo>
) : BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    // 3
    override fun getItemId(position: Int): Long {
        return list[position].id.hashCode().toLong()
    }

    // 4
    override fun getItem(position: Int): Any? {
        return list[position]
    }

    // 5
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view = convertView
        if (view == null) {
            view = GridProductImageBinding.inflate(layoutInflater, null, false).root
        }
        view!!
        Creator.loadUrl(
            view.context,
            list[position].url,
            view.findViewById<ImageView>(R.id.imageIV)
        )

        return view
    }
}
