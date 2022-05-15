package app.suprsend.database

import com.squareup.sqldelight.ColumnAdapter
import kotlinx.serialization.KSerializer

internal class DataModelColumnAdapter<T : Any>(
    private val serializer: KSerializer<T>
) : ColumnAdapter<T, String> {

    override fun decode(databaseValue: String) = json.decodeFromString(serializer, databaseValue)

    override fun encode(value: T) = json.encodeToString(serializer, value)
}
