package app.suprsend.android.event

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.JsonElement

internal class EventRemoteDataSource : EventDataSourceContract {
    override fun track(value: JsonElement?, isDirty: Boolean) {
//    return globalNetwork.get()?.get<String> {
//        url {
//            this.protocol = URLProtocol.HTTPS
//            host = "reqres.in"
//            parameter("page", 2)
//            path("api", "users")
//        }
//    }
        TODO("Not yet implemented")
    }

    override fun getEvents(limit: Long, isDirty: Boolean): Flow<List<EventModel>> {
        TODO("Not yet implemented")
    }


}