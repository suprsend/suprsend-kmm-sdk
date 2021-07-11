package app.suprsend.android.config

import app.suprsend.android.base.BaseDatabase
import org.junit.Test
import kotlin.test.assertEquals


internal class ConfigHelperTest : BaseDatabase() {

    @Test
    fun testAddOrUpdate() {
        ConfigHelper.addOrUpdate("appName", "SuperSend")
        assertEquals("SuperSend", ConfigHelper.get("appName"))
    }

    @Test
    fun testAddOrUpdateForNotExistingValue() {
        assertEquals(null, ConfigHelper.get("userName"))
    }
}