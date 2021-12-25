package app.suprsend.event

import app.suprsend.base.Logger
import app.suprsend.base.SSConstants
import app.suprsend.base.SdkCreator
import app.suprsend.base.network
import app.suprsend.config.ConfigHelper
import app.suprsend.database.json
import com.soywiz.krypto.HMAC
import com.soywiz.krypto.md5
/* ktlint-disable no-wildcard-imports */
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
/* ktlint-enable no-wildcard-imports */
import io.ktor.utils.io.charsets.Charsets
import io.ktor.utils.io.core.toByteArray
import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString

object EventFlushHandler {
    const val TAG = "flush"

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
            val secret = ConfigHelper.get(SSConstants.CONFIG_API_SECRET) ?: ""

            val stringToSign = httpVerb + "\n" +
                contentMd5 + "\n" +
                contentType + "\n" +
                date + "\n" +
                requestURI

            val signature = HMAC.hmacSHA256(secret.toByteArray(Charsets.UTF_8), stringToSign.toByteArray(Charsets.UTF_8)).base64

            // Logger.i(TAG, "stringToSign : $stringToSign Signature : $signature")

            val httpResponse = network.get()!!.post<HttpResponse> {
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
}
