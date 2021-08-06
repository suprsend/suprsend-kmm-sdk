package app.suprsend.android.android

import android.annotation.SuppressLint
import android.content.Context
import android.widget.ImageView
import app.suprsend.android.SSApi
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mixpanel.android.mpmetrics.MixpanelAPI

object Creator {

    private var options: RequestOptions = RequestOptions()
        .centerCrop()
        .placeholder(R.drawable.ic_alarm)
        .error(R.drawable.ic_alarm)

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
}

lateinit var ssApi: SSApi
@SuppressLint("StaticFieldLeak")
lateinit var mixpanelAPI: MixpanelAPI
