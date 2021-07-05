package app.suprsend.android.database

import com.squareup.sqldelight.ColumnAdapter
import kotlinx.serialization.KSerializer

internal class DataModelColumnAdapter<T : Any>(
    private val serializer: KSerializer<T>
) : ColumnAdapter<T, String> {

    override fun decode(databaseValue: String) = format.decodeFromString(serializer, databaseValue)

    override fun encode(value: T) = format.encodeToString(serializer, value)
}
