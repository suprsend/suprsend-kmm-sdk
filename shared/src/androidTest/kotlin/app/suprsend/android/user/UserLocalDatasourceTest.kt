package app.suprsend.android.user

import app.suprsend.android.base.BaseDatabase
import org.junit.Test
import kotlin.test.assertEquals

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