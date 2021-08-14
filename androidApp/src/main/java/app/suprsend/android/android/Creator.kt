package app.suprsend.android.android

import android.annotation.SuppressLint
import android.content.Context
import android.widget.ImageView
import app.suprsend.android.SSApi
import com.bumptech.glide.Glide
import com.mixpanel.android.mpmetrics.MixpanelAPI

object Creator {

    fun loadUrl(context: Context, url: String, imageView: ImageView) {
        Glide.with(context)
            .load(url)
//            .placeholder(R.drawable.placeholder) // any placeholder to load at start
//            .error(R.drawable.imagenotfound)  // any image in case of error
//            .override(200, 200) // resizing
//            .centerCrop()
            .into(imageView)
    }

    var email = ""
    var password = ""

    fun getRandomImage(index: Int): String {
        return when {
            index % 10 == 0 -> "https://niksdevelop.herokuapp.com/images/ecommerce/11461225530026-Antony-Morato-Men-Tshirts-8781461225528839-1.jpg"
            index % 10 == 1 -> "https://niksdevelop.herokuapp.com/images/ecommerce/11461225529845-Antony-Morato-Men-Tshirts-8781461225528839-2.jpg"
            index % 10 == 2 -> "https://niksdevelop.herokuapp.com/images/ecommerce/11461226111809-Antony-Morato-Navy-Polo-T-shirt-7711461226111027-1.jpg"
            index % 10 == 3 -> "https://niksdevelop.herokuapp.com/images/ecommerce/11467807016267-AKS-Red-Printed-Anarkali-Kurta-1501467807016047-2.jpg"
            index % 10 == 4 -> "https://niksdevelop.herokuapp.com/images/ecommerce/11467807016203-AKS-Red-Printed-Anarkali-Kurta-1501467807016047-5.jpg"
            index % 10 == 5 -> "https://niksdevelop.herokuapp.com/images/ecommerce/11467807016293-AKS-Red-Printed-Anarkali-Kurta-1501467807016047-1.jpg"
            else -> "https://niksdevelop.herokuapp.com/images/ecommerce/11461226111575-Antony-Morato-Navy-Polo-T-shirt-7711461226111027-3.jpg"
        }
    }
}

lateinit var ssApi: SSApi

@SuppressLint("StaticFieldLeak")
lateinit var mixpanelAPI: MixpanelAPI
