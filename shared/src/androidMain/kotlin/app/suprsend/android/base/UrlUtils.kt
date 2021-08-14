package app.suprsend.android.base

internal object UrlUtils {

    fun createNotificationBannerImage(url: String, widthPixels: Int): String {
        return if (url.contains("http")) {
            url
        } else {
            val transformedHeight = (widthPixels / 2)
            "https://ik.imagekit.io/l0quatz6utm/tr:w-$widthPixels,h-$transformedHeight$url"
        }
    }

    fun createNotificationLogoImage(url: String, size: Int): String {
        return if (url.contains("http")) {
            url
        } else {
            "https://ik.imagekit.io/l0quatz6utm/tr:w-$size,h-$size$url"
        }
    }
}
