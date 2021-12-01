package app.suprsend.base

import org.junit.Assert
import org.junit.Test

class Common {

    @Test
    fun testMobileNumber() {
        Assert.assertFalse(isMobileNumberValid("8983364103"))
        Assert.assertTrue(isMobileNumberValid("+918983364103"))
        Assert.assertTrue(isMobileNumberValid("+91 8983364103"))
        Assert.assertTrue(isMobileNumberValid("+91 898 3364103"))
    }
}
