package ly.david.data.musicbrainz.api

import io.ktor.client.HttpClient
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import ly.david.data.musicbrainz.MusicBrainzAuthState

private const val MUSIC_BRAINZ_API_BASE_URL = "$MUSIC_BRAINZ_BASE_URL/ws/2/"
private const val USER_AGENT_VALUE = "MusicSearch (https://github.com/lydavid/MusicSearch)"
private const val ACCEPT = "Accept"
private const val ACCEPT_VALUE = "application/json"

interface MusicBrainzApi : SearchApi, BrowseApi, LookupApi, CollectionApi, MusicBrainzUserApi {
    companion object {
        fun create(
            httpClient: HttpClient,
            musicBrainzAuthState: MusicBrainzAuthState,
        ): MusicBrainzApi {
            val extendedClient = httpClient.config {
                defaultRequest {
                    url(MUSIC_BRAINZ_API_BASE_URL)
                    header(ACCEPT, ACCEPT_VALUE)
                }
                install(UserAgent) {
                    agent = USER_AGENT_VALUE
                }
                install(Auth) {
                    bearer {
                        loadTokens {
                            val accessToken = musicBrainzAuthState.getAccessToken()
                            if (accessToken.isNullOrEmpty()) return@loadTokens null
                            val refreshToken = musicBrainzAuthState.getRefreshToken()
                            if (refreshToken.isNullOrEmpty()) return@loadTokens null

                            BearerTokens(accessToken, refreshToken)
                        }
                        refreshTokens {
                            // TODO: handle refresh
                            val accessToken = musicBrainzAuthState.getAccessToken() ?: return@refreshTokens null
                            val refreshToken = musicBrainzAuthState.getRefreshToken() ?: return@refreshTokens null
                            BearerTokens(accessToken, refreshToken)
                        }
                        // TODO: handle collection browse, one way to do it is to split up the
                        //  api that requires auth and just return true here
//                        sendWithoutRequest { request ->
//                            request.url.pathSegments.contains(USER_INFO)
//                        }
                    }
                }
            }

            return MusicBrainzApiImpl(
                httpClient = extendedClient,
            )
        }
    }
}

class MusicBrainzApiImpl(
    override val httpClient: HttpClient,
) : SearchApiImpl, BrowseApiImpl, LookupApiImpl, CollectionApiImpl, MusicBrainzUserApiImpl, MusicBrainzApi
