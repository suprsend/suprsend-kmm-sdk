package app.suprsend.android.user

import app.suprsend.android.globalNetwork
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.URLProtocol

class UserNetwork {

    suspend fun getUsers(): String? {
        return globalNetwork.get()?.get<String> {
            url {
                this.protocol = URLProtocol.HTTPS
                host = "reqres.in"
                parameter("page", 2)
                path("api", "users")
            }
        }
    }
}