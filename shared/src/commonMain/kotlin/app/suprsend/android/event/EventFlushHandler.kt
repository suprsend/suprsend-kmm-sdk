package app.suprsend.android.event

import app.suprsend.android.base.Logger
import app.suprsend.android.base.SSConstants
import app.suprsend.android.base.SdkCreator
import app.suprsend.android.config.ConfigHelper
import app.suprsend.android.database.json
import app.suprsend.android.globalNetwork
import com.soywiz.krypto.HMAC
import com.soywiz.krypto.md5
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.charsets.Charsets
import io.ktor.utils.io.core.toByteArray
import kotlinx.datetime.Clock
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
            val httpVerb = "POST"
            val contentMd5 = requestJson.toByteArray(Charsets.UTF_8).md5().hexLower
            val contentType = "application/json"
            val date = Clock.System.now().toString()
            val requestURI = "/event"
            val envKey = ConfigHelper.get(SSConstants.CONFIG_API_KEY) ?: ""
            val secret = ConfigHelper.get(SSConstants.CONFIG_SECRET) ?: ""

            val stringToSign = httpVerb + "\n" +
                contentMd5 + "\n" +
                contentType + "\n" +
                date + "\n" +
                requestURI

            val signature = HMAC.hmacSHA256(secret.toByteArray(Charsets.UTF_8), stringToSign.toByteArray(Charsets.UTF_8)).base64

            // Logger.i(TAG, "stringToSign : $stringToSign Signature : $signature")

            val httpResponse = globalNetwork.get()!!.post<HttpResponse> {
                url("$baseUrl$requestURI")
                contentType(ContentType.Application.Json)
                headers {
                    append("Date", date)
                    append(HttpHeaders.Authorization, "$envKey:$signature")
                }

                body = requestJson
            }

            // Logger.i(TAG, "$httpResponse $requestJson")

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
