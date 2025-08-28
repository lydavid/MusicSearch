package ly.david.musicsearch.data.musicbrainz.api

import io.ktor.client.HttpClient
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.defaultRequest
import ly.david.musicsearch.data.musicbrainz.MUSIC_BRAINZ_BASE_URL
import ly.david.musicsearch.data.musicbrainz.auth.MusicBrainzAuthRepository
import ly.david.musicsearch.shared.domain.USER_AGENT_VALUE
import ly.david.musicsearch.shared.domain.network.RESOURCE_COLLECTION

private const val MUSIC_BRAINZ_API_BASE_URL = "$MUSIC_BRAINZ_BASE_URL/ws/2/"

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
                            musicBrainzAuthRepository.getAccessToken()?.let {
                                BearerTokens(it, "")
                            }
                        }
                        refreshTokens {
                            musicBrainzAuthRepository.getAccessToken()?.let {
                                BearerTokens(it, "")
                            }
                        }
                        sendWithoutRequest { request ->
                            request.url.parameters[RESOURCE_COLLECTION] != null ||
                                request.url.pathSegments.contains(RESOURCE_COLLECTION) ||
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
