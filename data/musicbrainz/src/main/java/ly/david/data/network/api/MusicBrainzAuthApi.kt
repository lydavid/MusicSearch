package ly.david.data.network.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.http.parameters
import io.ktor.http.userAgent
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

interface MusicBrainzAuthApi {

    companion object {
        private val client = HttpClient(Android) {
            defaultRequest {
                userAgent(USER_AGENT_VALUE)
                url(MUSIC_BRAINZ_API_BASE_URL)
            }
            install(Logging) {
                level = LogLevel.ALL
            }
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                    }
                )
            }
        }

        fun create(): MusicBrainzAuthApi {
            return MusicBrainzAuthApiImpl(
                httpClient = client
            )
        }
    }

    suspend fun getUserInfo(): UserInfo

    suspend fun logout(
        token: String,
        clientId: String,
        clientSecret: String,
    )
}

class MusicBrainzAuthApiImpl(
    private val httpClient: HttpClient,
) : MusicBrainzAuthApi {
    override suspend fun getUserInfo(): UserInfo {
        return httpClient.get("$MUSIC_BRAINZ_BASE_URL/oauth2/userinfo").body()
    }

    override suspend fun logout(token: String, clientId: String, clientSecret: String) {
        httpClient.submitForm(
            url = "$MUSIC_BRAINZ_BASE_URL/oauth2/revoke",
            formParameters = parameters {
                append("token", token)
                append("client_id", clientId)
                append("client_secret", clientSecret)
            },
        )
    }
}
