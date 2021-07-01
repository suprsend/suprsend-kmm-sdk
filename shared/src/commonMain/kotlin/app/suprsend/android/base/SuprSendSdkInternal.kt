package app.suprsend.android.base

import app.suprsend.android.database.SuprSendDatabaseWrapper
import app.suprsend.android.database.DatabaseDriverFactory
import app.suprsend.android.event.EventModel
import app.suprsend.android.event.EventRepository
import app.suprsend.android.network.httpClientEngine
import com.squareup.sqldelight.internal.Atomic
import io.ktor.client.HttpClient
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlin.native.concurrent.SharedImmutable

@SharedImmutable
internal val GLOBAL_SUPR_SEND_DATABASE_WRAPPER: Atomic<SuprSendDatabaseWrapper?> = Atomic(null)

@SharedImmutable
internal val globalNetwork: Atomic<HttpClient?> = Atomic(null)

internal object SuprSendSdkInternal {

    fun initialize(databaseDriverFactory: DatabaseDriverFactory) {
        initializeDatabase(databaseDriverFactory)
        initializeNetworking()
    }

    private fun initializeNetworking() {
        val httpClient = HttpClient(engine = httpClientEngine) {
            install(Logging) {
                logger = io.ktor.client.features.logging.Logger.DEFAULT
                level = LogLevel.HEADERS
            }
        }
        globalNetwork.set(httpClient)
    }

    private fun initializeDatabase(databaseDriverFactory: DatabaseDriverFactory) {
        val database = SuprSendDatabaseWrapper(databaseDriverFactory)
        GLOBAL_SUPR_SEND_DATABASE_WRAPPER.set(database)
    }


    fun trackEvent(eventName: String, properties: Map<String, Any>?) {
        if (eventName.isBlank()) {
            Logger.i("", "event name cannot be blank")
            return
        }
        GlobalScope.launch(ioDispatcher()) {
            val eventRepository = EventRepository()
            val json = buildJsonObject {
                properties
                    ?.forEach { (key, value) ->
                        val processedKey = if (key.length > SuprSendConstants.EVENT_KEY_MAX_LENGTH) {
                            key.substring(0, SuprSendConstants.EVENT_KEY_MAX_LENGTH - 1)
                        } else {
                            key
                        }
                        if (value is Boolean)
                            put(processedKey, value)
                        else if (value is Number)
                            put(processedKey, value)
                        else if (value is String) {
                            val processedValue = if (value.length > SuprSendConstants.EVENT_VALUE_MAX_LENGTH) {
                                key.substring(0, SuprSendConstants.EVENT_VALUE_MAX_LENGTH - 1)
                            } else {
                                value
                            }
                            put(processedKey, processedValue)
                        }
                    }
            }
            eventRepository.trackEvent(eventName, json.toString())
        }
    }

    fun flush() {
        GlobalScope
            .launch {
                val eventRepository = EventRepository()
                eventRepository
                    .getDirtyEvents(SuprSendConstants.FLUSH_EVENT_SIZE)
                    .map { eventModelList ->
                        Json.encodeToJsonElement(ListSerializer(EventModel.serializer()), eventModelList).toString()
                    }
                    .collect { json ->
                        //Todo Send to server
                        Logger.i("flush", json)
                    }
            }
    }

}