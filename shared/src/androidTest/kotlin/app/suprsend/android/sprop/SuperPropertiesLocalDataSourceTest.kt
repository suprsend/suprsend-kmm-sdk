package app.suprsend.android.sprop

import app.suprsend.android.base.BaseDatabase
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import org.junit.Test
import kotlin.test.assertEquals

internal class SuperPropertiesLocalDataSourceTest : BaseDatabase() {

    @Test
    fun testAddSuperProperty() {
        val superPropertiesLocalDataSource = SuperPropertiesLocalDataSource()
        superPropertiesLocalDataSource.add("Product Name", "Cycle 123")
        assertEquals(
            buildJsonObject {
                put("Product Name", JsonPrimitive("Cycle 123"))
            },
            superPropertiesLocalDataSource.getAll()
        )
    }

    @Test
    fun testRemoveProperty() {
        val superPropertiesLocalDataSource = SuperPropertiesLocalDataSource()
        superPropertiesLocalDataSource.add("Product Name", "Cycle 123")
        superPropertiesLocalDataSource.add("Price", 590)
        assertEquals(
            buildJsonObject {
                put("Product Name", JsonPrimitive("Cycle 123"))
                put("Price", JsonPrimitive(590))
            },
            superPropertiesLocalDataSource.getAll()
        )

        superPropertiesLocalDataSource.remove("Price")
        assertEquals(
            buildJsonObject {
                put("Product Name", JsonPrimitive("Cycle 123"))
            },
            superPropertiesLocalDataSource.getAll()
        )
    }
}