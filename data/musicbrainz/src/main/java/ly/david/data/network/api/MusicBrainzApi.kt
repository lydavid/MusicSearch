package ly.david.data.network.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.userAgent
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import ly.david.data.network.MusicBrainzAuthState

private const val MUSIC_BRAINZ_API_BASE_URL = "$MUSIC_BRAINZ_BASE_URL/ws/2/"
private const val USER_AGENT_VALUE = "MusicSearch (https://github.com/lydavid/MusicSearch)"

interface MusicBrainzApi : SearchApi, BrowseApi, LookupApi, CollectionApi, MusicBrainzAuthApi {
    companion object {
        fun create(
            musicBrainzAuthState: MusicBrainzAuthState,
        ): MusicBrainzApi {
            val client = HttpClient(Android) {
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
                install(Auth) {
                    bearer {
                        loadTokens {
                            val accessToken = musicBrainzAuthState.getAccessToken() ?: return@loadTokens null
                            val refreshToken = musicBrainzAuthState.getRefreshToken() ?: return@loadTokens null
                            BearerTokens(accessToken, refreshToken)
                        }
                        refreshTokens {
                            // TODO: handle refresh
                            val accessToken = musicBrainzAuthState.getAccessToken() ?: return@refreshTokens null
                            val refreshToken = musicBrainzAuthState.getRefreshToken() ?: return@refreshTokens null
                            BearerTokens(accessToken, refreshToken)
                        }
//                        sendWithoutRequest { request ->
//                            // TODO: handle collection browse
//                            request.url.pathSegments.contains(USER_INFO)
//                        }
                    }
                }
            }

            return MusicBrainzApiImpl(
                httpClient = client,
            )
        }
    }
}

class MusicBrainzApiImpl(
    override val httpClient: HttpClient,
) : SearchApiImpl, BrowseApiImpl, LookupApiImpl, CollectionApiImpl, MusicBrainzAuthApiImpl, MusicBrainzApi
