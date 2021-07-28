package app.suprsend.android.config

import app.suprsend.android.base.BaseDatabase
import kotlin.test.assertEquals
import org.junit.Test

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
