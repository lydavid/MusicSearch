package ly.david.data.spotify.api.auth

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.forms.submitForm
import io.ktor.http.parameters
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val SPOTIFY_AUTH = "https://accounts.spotify.com/api/token"
private const val CLIENT_CREDENTIALS = "client_credentials"

interface SpotifyAuthApi {

    companion object {
        fun create(
            engine: HttpClientEngine,
        ): SpotifyAuthApi {
            val client = HttpClient(engine) {
                expectSuccess = true

                install(Logging) {
                    level = LogLevel.ALL
                }
                install(ContentNegotiation) {
                    json(
                        Json {
                            ignoreUnknownKeys = true
                        }
                    )
                }
            }

            return SpotifyAuthApiImpl(
                client = client
            )
        }
    }

    suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        grantType: String = CLIENT_CREDENTIALS,
    ): SpotifyAccessToken
}

class SpotifyAuthApiImpl(
    private val client: HttpClient,
) : SpotifyAuthApi {
    override suspend fun getAccessToken(
        clientId: String,
        clientSecret: String,
        grantType: String,
    ): SpotifyAccessToken {
        return client.submitForm(
            url = SPOTIFY_AUTH,
            formParameters = parameters {
                append("client_id", clientId)
                append("client_secret", clientSecret)
                append("grant_type", grantType)
            }
        ).body()
    }
}
