package app.suprsend.android.sprop

import kotlinx.serialization.json.JsonObject

internal class SuperPropertiesRepository(
    private val superPropertiesLocalDataSource: SuperPropertiesLocalDataSource = SuperPropertiesLocalDataSource(),
    private val superPropertiesRemoteDataSource: SuperPropertiesRemoteDataSource = SuperPropertiesRemoteDataSource()
) : SuperPropertiesRepositoryContract {
    override fun add(key: String, value: Any) {
        superPropertiesLocalDataSource.add(key, value)
    }

    override fun add(properties: JsonObject) {
        superPropertiesLocalDataSource.add(properties)
    }

    override fun remove(key: String) {
        superPropertiesLocalDataSource.remove(key)
    }

    override fun getAll(): JsonObject {
        return superPropertiesLocalDataSource.getAll()
    }
}