package app.suprsend.android.event

import app.suprsend.android.base.Logger
import app.suprsend.android.base.SSConstants
import app.suprsend.android.globalNetwork
import app.suprsend.android.user.UserEventLocalDataSource
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class EventFlushHandler {
    suspend fun flushEvents() {
        val eventLocalDatasource = EventLocalDatasource()
        var eventModelList: List<EventModel> = eventLocalDatasource.getEvents(SSConstants.FLUSH_EVENT_PAYLOAD_SIZE)
        if (eventModelList.isEmpty()) {
            Logger.i("flush", "No events found")
            return
        }
        while (eventModelList.isNotEmpty()) {
            val requestJson = Json.encodeToString(eventModelList.map { it.value })
            val httpResponse = globalNetwork.get()!!.post<HttpResponse> {
                url(SSConstants.EVENT_URL)
                contentType(ContentType.Application.Json)
                body = requestJson
            }
            Logger.i("flush", "$httpResponse $requestJson")
            eventLocalDatasource.delete(eventModelList.map { it.id })
            eventModelList = eventLocalDatasource.getEvents(SSConstants.FLUSH_EVENT_PAYLOAD_SIZE)
        }
    }

    suspend fun flushUserEvents() {
        val userEventLocalDataSource = UserEventLocalDataSource()
        var eventModelList: List<EventModel> = userEventLocalDataSource.getEvents(SSConstants.FLUSH_EVENT_PAYLOAD_SIZE)
        if (eventModelList.isEmpty()) {
            Logger.i("flush", "No user events found")
            return
        }
        while (eventModelList.isNotEmpty()) {
            val requestJson = Json.encodeToString(eventModelList.map { it.value })
            val httpResponse = globalNetwork.get()!!.post<HttpResponse> {
                url(SSConstants.IDENTITY_URL)
                contentType(ContentType.Application.Json)
                body = requestJson
            }
            Logger.i("flush", "$httpResponse $requestJson")
            userEventLocalDataSource.delete(eventModelList.map { it.id })
            eventModelList = userEventLocalDataSource.getEvents(SSConstants.FLUSH_EVENT_PAYLOAD_SIZE)
        }
    }
}
