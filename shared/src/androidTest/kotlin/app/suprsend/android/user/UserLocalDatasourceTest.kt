package app.suprsend.android.user

import app.suprsend.android.base.BaseDatabase
import kotlin.test.assertEquals
import org.junit.Test

internal class UserLocalDatasourceTest : BaseDatabase() {

    @Test
    fun testIdentifyWithCallingIdentity() {
        val userLocalDatasource = UserLocalDatasource()
        assertEquals("", userLocalDatasource.getIdentity())
    }

    @Test
    fun testIdentify() {
        val userLocalDatasource = UserLocalDatasource()
        userLocalDatasource.identify("123")
        assertEquals("123", userLocalDatasource.getIdentity())
    }
}
