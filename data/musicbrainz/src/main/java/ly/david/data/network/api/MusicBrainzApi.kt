package ly.david.data.network.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.userAgent
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val MUSIC_BRAINZ_API_BASE_URL = "$MUSIC_BRAINZ_BASE_URL/ws/2/"
private const val USER_AGENT_VALUE = "MusicSearch (https://github.com/lydavid/MusicSearch)"

interface MusicBrainzApi : SearchApi, BrowseApi, LookupApi, CollectionApi {
    companion object {
        fun create(): MusicBrainzApi {
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
//                install(Auth) {
//                    bearer {
//                        loadTokens {
//                            val accessToken = spotifyOAuth.getAccessToken() ?: return@loadTokens null
//                            BearerTokens(accessToken, "")
//                        }
//                        refreshTokens {
//                            val newAccessToken = spotifyAuthApi.getAccessToken(
//                                clientId = clientId,
//                                clientSecret = clientSecret,
//                            )
//                            spotifyOAuth.saveAccessToken(
//                                accessToken = newAccessToken.accessToken,
//                            )
//
//                            val accessToken = spotifyOAuth.getAccessToken() ?: return@refreshTokens null
//                            BearerTokens(accessToken, "")
//                        }
//                        sendWithoutRequest { request ->
//                            request.url.host == HOST
//                        }
//                    }
//                }
            }

            return MusicBrainzApiImpl(
                client = client,
            )
        }
    }
}

class MusicBrainzApiImpl(
    override val client: HttpClient,
) : SearchApiImpl, BrowseApiImpl, LookupApiImpl, MusicBrainzApi {
    override suspend fun uploadToCollection(
        collectionId: String,
        resourceUriPlural: String,
        mbids: String,
        client: String,
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFromCollection(
        collectionId: String,
        resourceUriPlural: String,
        mbids: String,
        client: String,
    ) {
        TODO("Not yet implemented")
    }
}
