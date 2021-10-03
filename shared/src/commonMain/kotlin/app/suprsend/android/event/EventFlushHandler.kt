package app.suprsend.android.event

import app.suprsend.android.base.Logger
import app.suprsend.android.base.SSConstants
import app.suprsend.android.base.SdkCreator
import app.suprsend.android.config.ConfigHelper
import app.suprsend.android.database.json
import app.suprsend.android.globalNetwork
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.encodeToString

class EventFlushHandler {
    suspend fun flushEvents() {
        val eventLocalDatasource = SdkCreator.eventLocalDatasource
        var eventModelList: List<EventModel> = eventLocalDatasource.getEvents(SSConstants.FLUSH_EVENT_PAYLOAD_SIZE)
        if (eventModelList.isEmpty()) {
            Logger.i(TAG, "No events found")
            return
        }
        val baseUrl = ConfigHelper.get(SSConstants.CONFIG_API_BASE_URL) ?: SSConstants.DEFAULT_BASE_API_URL
        while (eventModelList.isNotEmpty()) {
            val requestJson = json.encodeToString(eventModelList.map { it.value })
            val httpResponse = globalNetwork.get()!!.post<HttpResponse> {
                url("$baseUrl/event")
                contentType(ContentType.Application.Json)
                body = requestJson
            }

            Logger.i(TAG, "$httpResponse $requestJson")

            if (httpResponse.status.value == 202) {
                eventLocalDatasource.delete(eventModelList.map { it.id })
                eventModelList = eventLocalDatasource.getEvents(SSConstants.FLUSH_EVENT_PAYLOAD_SIZE)
            } else {
                eventModelList = emptyList()
                break
            }
        }
    }

    companion object {
        const val TAG = "flush"
    }
}
