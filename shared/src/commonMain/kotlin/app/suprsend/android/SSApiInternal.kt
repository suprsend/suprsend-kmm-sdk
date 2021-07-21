package app.suprsend.android

import app.suprsend.android.base.Logger
import app.suprsend.android.base.SSConstants
import app.suprsend.android.base.ioDispatcher
import app.suprsend.android.base.toKotlinJsonObject
import app.suprsend.android.base.uuid
import app.suprsend.android.database.DatabaseDriverFactory
import app.suprsend.android.database.SSDatabaseWrapper
import app.suprsend.android.event.EventFlushHandler
import app.suprsend.android.event.EventLocalDatasource
import app.suprsend.android.event.EventModel
import app.suprsend.android.event.PayloadCreator
import app.suprsend.android.network.httpClientEngine
import app.suprsend.android.sprop.SuperPropertiesLocalDataSource
import app.suprsend.android.user.UserEventLocalDataSource
import app.suprsend.android.user.UserLocalDatasource
import app.suprsend.android.user.UserRepository
import app.suprsend.android.user.api.UserApiInternalContract
import app.suprsend.android.user.api.UserApiInternalImpl
import com.squareup.sqldelight.internal.Atomic
import io.ktor.client.*
import io.ktor.client.features.logging.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonObject
import kotlin.native.concurrent.SharedImmutable

@SharedImmutable
internal val GLOBAL_SUPR_SEND_DATABASE_WRAPPER: Atomic<SSDatabaseWrapper?> = Atomic(null)

@SharedImmutable
internal val globalNetwork: Atomic<HttpClient?> = Atomic(null)

internal object SSApiInternal {

    private val userImpl = UserApiInternalImpl()

    var apiKey: String = ""
    private var flushing: Boolean = false


    fun identify(uniqueId: String) {
        GlobalScope.launch(ioDispatcher()) {
            Logger.i("api", "identity : $uniqueId")
            val userRepository = UserRepository()
            UserEventLocalDataSource()
                .track(
                    EventModel(
                        value = PayloadCreator
                            .buildIdentityEventPayload(
                                identifiedId = uniqueId,
                                anonymousId = userRepository.getIdentity(),
                            ),
                        id = uuid()
                    )
                )
            userRepository.identify(uniqueId)
        }
    }

    fun setSuperProperties(propertiesJsonObject: String?) {
        GlobalScope.launch(ioDispatcher()) {
            Logger.i("api", "Setting super properties")
            val superPropertiesRepository = SuperPropertiesLocalDataSource()
            superPropertiesRepository.add(propertiesJsonObject.toKotlinJsonObject())
        }
    }

    fun track(eventName: String, propertiesJsonString: String?) {
        GlobalScope.launch(ioDispatcher()) {
            track(eventName, propertiesJsonString.toKotlinJsonObject())
        }
    }

    fun track(eventName: String, propertiesJO: JsonObject?) {
        Logger.i("api", "Track Event : $eventName")
        if (eventName.isBlank()) {
            Logger.i("", "event name cannot be blank")
            return
        }

        val userLocalDatasource = UserLocalDatasource()
        val superPropertiesLocalDataSource = SuperPropertiesLocalDataSource()
        EventLocalDatasource()
            .track(
                EventModel(
                    value = PayloadCreator.buildTrackEventPayload(
                        eventName = eventName,
                        distinctId = userLocalDatasource.getIdentity(),
                        superProperties = superPropertiesLocalDataSource.getAll(),
                        userProperties = propertiesJO
                    ),
                    id = uuid()
                )
            )
    }

    fun getUser(): UserApiInternalContract {
        return userImpl
    }

    fun flush() {
        if (flushing)
            return

        Logger.i("api", "Trying to flush events")

        GlobalScope.launch(ioDispatcher() + CoroutineExceptionHandler { _, throwable ->
            Logger.e("flush", "", throwable)
        }) {
            val eventFlushHandler = EventFlushHandler()
            eventFlushHandler.flushUserEvents()
            eventFlushHandler.flushEvents()
        }
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
        val database = SSDatabaseWrapper(databaseDriverFactory)
        GLOBAL_SUPR_SEND_DATABASE_WRAPPER.set(database)
    }


}