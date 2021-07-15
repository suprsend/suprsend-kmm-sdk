package app.suprsend.android

import app.suprsend.android.base.Logger
import app.suprsend.android.base.ioDispatcher
import app.suprsend.android.base.toKotlinJsonObject
import app.suprsend.android.database.DatabaseDriverFactory
import app.suprsend.android.database.SuprSendDatabaseWrapper
import app.suprsend.android.event.EventFlushHandler
import app.suprsend.android.event.EventLocalDatasource
import app.suprsend.android.event.PayloadCreator
import app.suprsend.android.network.httpClientEngine
import app.suprsend.android.sprop.SuperPropertiesLocalDataSource
import app.suprsend.android.sprop.SuperPropertiesRepository
import app.suprsend.android.user.UserEventLocalDataSource
import app.suprsend.android.user.UserLocalDatasource
import app.suprsend.android.user.UserRepository
import app.suprsend.android.user.api.UserApi
import app.suprsend.android.user.api.UserApiImpl
import com.squareup.sqldelight.internal.Atomic
import io.ktor.client.*
import io.ktor.client.features.logging.*
import kotlinx.coroutines.CoroutineExceptionHandler
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
    var flushing: Boolean = false

    fun identify(uniqueId: String) {
        GlobalScope.launch(ioDispatcher()) {
            Logger.i("api", "identity : $uniqueId")
            val userRepository = UserRepository()
            UserEventLocalDataSource()
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
            Logger.i("api", "Setting super properties")
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
        Logger.i("api", "Track Event : $eventName")
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

    fun getUser(): UserApi {
        return userImpl
    }

    fun flush() {
        if (flushing)
            return

        Logger.i("api", "Trying to flush events")

        GlobalScope.launch(ioDispatcher() + CoroutineExceptionHandler { coroutineContext, throwable ->
            Logger.e("flush", "", throwable)
        }) {
            val eventFlushHandler = EventFlushHandler()
            eventFlushHandler.flushUserEvents()
            eventFlushHandler.flushEvents()
        }
    }

    fun reset() {

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