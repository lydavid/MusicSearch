package ly.david.data.spotify.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments
import ly.david.data.spotify.api.auth.SpotifyAuthApi
import ly.david.data.spotify.api.auth.SpotifyAuthState

private const val BASE_URL = "https://api.spotify.com/v1/"
private const val ARTISTS = "${BASE_URL}artists"

interface SpotifyApi {

    companion object {
        fun create(
            httpClient: HttpClient,
            clientId: String,
            clientSecret: String,
            spotifyAuthApi: SpotifyAuthApi,
            spotifyAuthState: SpotifyAuthState,
        ): SpotifyApi {
            val extendedClient = httpClient.config {
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

                            val accessToken = newAccessToken.accessToken
                            spotifyAuthState.saveAccessToken(
                                accessToken = accessToken,
                            )

                            BearerTokens(accessToken, "")
                        }
                        sendWithoutRequest { true }
                    }
                }
            }

            return SpotifyApiImpl(
                client = extendedClient
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
