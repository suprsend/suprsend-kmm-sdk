package app.suprsend.android

import app.suprsend.android.base.Logger
import app.suprsend.android.base.ioDispatcher
import app.suprsend.android.base.toKotlinJsonObject
import app.suprsend.android.database.DatabaseDriverFactory
import app.suprsend.android.database.SuprSendDatabaseWrapper
import app.suprsend.android.event.EventLocalDatasource
import app.suprsend.android.event.PayloadCreator
import app.suprsend.android.network.httpClientEngine
import app.suprsend.android.sprop.SuperPropertiesLocalDataSource
import app.suprsend.android.sprop.SuperPropertiesRepository
import app.suprsend.android.user.UserLocalDatasource
import app.suprsend.android.user.UserRepository
import app.suprsend.android.user.api.UserApi
import app.suprsend.android.user.api.UserApiImpl
import com.squareup.sqldelight.internal.Atomic
import io.ktor.client.*
import io.ktor.client.features.logging.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import kotlin.native.concurrent.SharedImmutable

@SharedImmutable
internal val GLOBAL_SUPR_SEND_DATABASE_WRAPPER: Atomic<SuprSendDatabaseWrapper?> = Atomic(null)

@SharedImmutable
internal val globalNetwork: Atomic<HttpClient?> = Atomic(null)

internal object SuprSendApiInternal {

    private val userImpl = UserApiImpl()

    var apiKey: String = ""

    fun identify(uniqueId: String) {
        GlobalScope.launch(ioDispatcher()) {
            val userRepository = UserRepository()
            EventLocalDatasource()
                .track(
                    PayloadCreator
                        .buildIdentityEventPayload(
                            identifiedId = uniqueId,
                            anonymousId = userRepository.getIdentity(),
                        )
                )
            userRepository.identify(uniqueId)
        }
    }

    fun setSuperProperties(propertiesJsonObject: String?) {
        GlobalScope.launch(ioDispatcher()) {
            val superPropertiesRepository = SuperPropertiesRepository()
            superPropertiesRepository.add(propertiesJsonObject.toKotlinJsonObject())
        }
    }

    fun track(eventName: String, propertiesJsonString: String?) {
        GlobalScope.launch(ioDispatcher()) {
            track(eventName, propertiesJsonString.toKotlinJsonObject())
        }
    }

    fun track(eventName: String, propertiesJO: JsonObject?) {
        if (eventName.isBlank()) {
            Logger.i("", "event name cannot be blank")
            return
        }

        val userLocalDatasource = UserLocalDatasource()
        val superPropertiesLocalDataSource = SuperPropertiesLocalDataSource()
        EventLocalDatasource()
            .track(
                PayloadCreator.buildTrackEventPayload(
                    eventName = eventName,
                    distinctId = userLocalDatasource.getIdentity(),
                    superProperties = superPropertiesLocalDataSource.getAll(),
                    userProperties = propertiesJO
                )
            )
    }

    fun reset() {
        //TODO
//        GlobalScope
//            .launch {
//                val eventRepository = EventRepository()
//                eventRepository
//                    .getEvents(SuprSendConstants.FLUSH_EVENT_PAYLOAD_SIZE)
//                    .map { eventModelList ->
//                        Json.encodeToJsonElement(ListSerializer(EventModel.serializer()), eventModelList)
//                    }
//                    .map { bodyJsonElement ->
//                        globalNetwork.get()!!.post<HttpResponse> {
//                            url("")
//                            contentType(ContentType.Application.Json)
//                            body = bodyJsonElement
//                        }
//                    }
//                    .collect { httpResponse ->
//                        //Todo Send to server
//                        Logger.i("flush", "$httpResponse")
//                    }
//            }
    }

    fun getUser(): UserApi {
        return userImpl
    }


    fun initialize(databaseDriverFactory: DatabaseDriverFactory, apiKey: String) {
        this.apiKey = apiKey
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


}
//Todo - values sanitation
//            val json = buildJsonObject {
//                properties
//                    ?.forEach { (key, value) ->
//                        val processedKey = EventSanitation.processKey(key)
//                        when (value) {
//                            is Boolean -> put(processedKey, value)
//                            is Number -> put(processedKey, value)
//                            is String -> {
//                                put(processedKey, EventSanitation.processValue(value))
//                            }
//                        }
//                    }
//            }