package ly.david.musicsearch.data.musicbrainz.api

import io.ktor.client.HttpClient
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.defaultRequest
import ly.david.musicsearch.data.musicbrainz.auth.MusicBrainzAuthRepository

private const val MUSIC_BRAINZ_API_BASE_URL = "$MUSIC_BRAINZ_BASE_URL/ws/2/"
private const val USER_AGENT_VALUE = "MusicSearch (https://github.com/lydavid/MusicSearch)"

interface MusicBrainzApi : SearchApi, BrowseApi, LookupApi, CollectionApi, MusicBrainzUserApi {
    companion object {
        fun create(
            httpClient: HttpClient,
            musicBrainzAuthRepository: MusicBrainzAuthRepository,
        ): MusicBrainzApi {
            val extendedClient = httpClient.config {
                defaultRequest {
                    url(MUSIC_BRAINZ_API_BASE_URL)
                }
                install(UserAgent) {
                    agent = USER_AGENT_VALUE
                }
                install(Auth) {
                    bearer {
                        loadTokens {
                            musicBrainzAuthRepository.getAuthState()?.let {
                                BearerTokens(it, "")
                            }
                        }
                        refreshTokens {
                            musicBrainzAuthRepository.getAuthState()?.let {
                                BearerTokens(it, "")
                            }
                        }
                        sendWithoutRequest { request ->
                            request.url.parameters["collection"] != null ||
                                request.url.pathSegments.contains(USER_INFO)
                        }
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
