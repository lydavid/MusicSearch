package ly.david.data.spotify.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import ly.david.data.spotify.api.auth.SpotifyAuthApi
import ly.david.data.spotify.api.auth.SpotifyAuthState

private const val HOST = "api.spotify.com"
private const val BASE_URL = "https://$HOST/v1/"
private const val ARTISTS = "${BASE_URL}artists"

interface SpotifyApi {

    companion object {
        fun create(
            clientId: String,
            clientSecret: String,
            spotifyAuthApi: SpotifyAuthApi,
            spotifyAuthState: SpotifyAuthState,
        ): SpotifyApi {
            val client = HttpClient(Android) {
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
                install(Auth) {
                    bearer {
                        loadTokens {
                            val accessToken = spotifyAuthState.getAccessToken() ?: return@loadTokens null
                            BearerTokens(accessToken, "")
                        }
                        refreshTokens {
                            val newAccessToken = spotifyAuthApi.getAccessToken(
                                clientId = clientId,
                                clientSecret = clientSecret,
                            )
                            spotifyAuthState.saveAccessToken(
                                accessToken = newAccessToken.accessToken,
                            )

                            val accessToken = spotifyAuthState.getAccessToken() ?: return@refreshTokens null
                            BearerTokens(accessToken, "")
                        }
                        sendWithoutRequest { request ->
                            request.url.host == HOST
                        }
                    }
                }
            }

            return SpotifyApiImpl(
                client = client
            )
        }
    }

    suspend fun getArtist(
        spotifyArtistId: String,
    ): SpotifyArtist
}

class SpotifyApiImpl(
    private val client: HttpClient,
) : SpotifyApi {
    override suspend fun getArtist(spotifyArtistId: String): SpotifyArtist {
        return client.get(ARTISTS) {
            url {
                appendPathSegments(spotifyArtistId)
            }
        }.body()
    }
}
