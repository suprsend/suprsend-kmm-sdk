package app.suprsend.android.event

import app.suprsend.android.base.Logger
import app.suprsend.android.base.SuprSendConstants
import app.suprsend.android.globalNetwork
import app.suprsend.android.user.UserEventLocalDataSource
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class EventFlushHandler {
    suspend fun flushEvents() {
        val eventLocalDatasource = EventLocalDatasource()
        var eventModelList: List<EventModel> = eventLocalDatasource.getEvents(SuprSendConstants.FLUSH_EVENT_PAYLOAD_SIZE)
        while (eventModelList.isNotEmpty()) {
            val bodyJsonElement = Json.encodeToJsonElement(ListSerializer(EventModel.serializer()), eventModelList)
            val httpResponse = globalNetwork.get()!!.post<HttpResponse> {
                url("https://niksdevelop.herokuapp.com/http/http_test.php")
                contentType(ContentType.Application.Json)
                body = bodyJsonElement.toString()
            }
            Logger.i("flush", httpResponse.readText())
            eventLocalDatasource.delete(eventModelList.filter { it.id != null }.map { it.id!! })
            eventModelList = eventLocalDatasource.getEvents(SuprSendConstants.FLUSH_EVENT_PAYLOAD_SIZE)
        }
    }

    suspend fun flushUserEvents() {
        val userEventLocalDataSource = UserEventLocalDataSource()
        var eventModelList: List<EventModel> = userEventLocalDataSource.getEvents(SuprSendConstants.FLUSH_EVENT_PAYLOAD_SIZE)
        while (eventModelList.isNotEmpty()) {
            val bodyJsonElement = Json.encodeToJsonElement(ListSerializer(EventModel.serializer()), eventModelList)
            val httpResponse = globalNetwork.get()!!.post<HttpResponse> {
                url("https://niksdevelop.herokuapp.com/http/http_test.php")
                contentType(ContentType.Application.Json)
                body = bodyJsonElement.toString()
            }
            Logger.i("flush", httpResponse.readText())
            userEventLocalDataSource.delete(eventModelList.filter { it.id != null }.map { it.id!! })
            eventModelList = userEventLocalDataSource.getEvents(SuprSendConstants.FLUSH_EVENT_PAYLOAD_SIZE)
        }
    }
}