package ly.david.musicsearch.data.musicbrainz.api

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.defaultRequest
import ly.david.musicsearch.data.musicbrainz.auth.MusicBrainzAuthRepository
import ly.david.musicsearch.shared.domain.network.RESOURCE_COLLECTION

interface MusicBrainzApi : SearchApi, BrowseApi, LookupApi, CollectionApi, MusicBrainzUserApi {
    companion object {
        fun create(
            httpClient: HttpClient,
            musicBrainzBaseUrl: String,
            musicBrainzAuthRepository: MusicBrainzAuthRepository,
        ): MusicBrainzApi {
            val extendedClient = httpClient.config {
                defaultRequest {
                    url("$musicBrainzBaseUrl/ws/2/")
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
