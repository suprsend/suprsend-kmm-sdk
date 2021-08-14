package app.suprsend.android.base

import junit.framework.TestCase
import org.junit.Assert
import org.junit.Test

internal class UrlUtilsTest : TestCase() {

    @Test
    fun testCreateNotificationBannerImage() {
        Assert.assertEquals(
            "https://niksdevelop.herokuapp.com/images/346kb.jpg",
            UrlUtils.createNotificationBannerImage("https://niksdevelop.herokuapp.com/images/346kb.jpg", 200)
        )

        Assert
            .assertEquals(
                "https://ik.imagekit.io/l0quatz6utm/tr:w-200,h-100/suprsend/staging/media/suprsend-only-logo_c8aa27faef118418e8c5bd7b31a1cafc74e09200.png",
                UrlUtils.createNotificationBannerImage("/suprsend/staging/media/suprsend-only-logo_c8aa27faef118418e8c5bd7b31a1cafc74e09200.png", 200)
            )
    }

    @Test
    fun testCreateNotificationLogoImage() {
        Assert.assertEquals(
            "https://niksdevelop.herokuapp.com/images/346kb.jpg",
            UrlUtils.createNotificationBannerImage("https://niksdevelop.herokuapp.com/images/346kb.jpg", 200)
        )

        Assert
            .assertEquals(
                "https://ik.imagekit.io/l0quatz6utm/tr:w-200,h-200/suprsend/staging/media/suprsend-only-logo_c8aa27faef118418e8c5bd7b31a1cafc74e09200.png",
                UrlUtils.createNotificationLogoImage("/suprsend/staging/media/suprsend-only-logo_c8aa27faef118418e8c5bd7b31a1cafc74e09200.png", 200)
            )
    }
}
