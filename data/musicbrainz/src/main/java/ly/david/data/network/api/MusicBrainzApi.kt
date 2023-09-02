package ly.david.data.network.api

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.userAgent
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import ly.david.data.network.MusicBrainzAuthState
import ly.david.data.network.RecoverableNetworkException

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
                HttpResponseValidator {
                    handleResponseExceptionWithRequest { exception, _ ->
                        val clientException =
                            exception as? ClientRequestException ?: return@handleResponseExceptionWithRequest
                        val exceptionResponse = clientException.response
                        if (exceptionResponse.status == HttpStatusCode.Unauthorized) {
                            val exceptionResponseText = exceptionResponse.bodyAsText()
                            throw RecoverableNetworkException(exceptionResponseText)
                        }
                    }
                }
                defaultRequest {
                    url(MUSIC_BRAINZ_API_BASE_URL)
                    userAgent(USER_AGENT_VALUE)
                    header(ACCEPT, ACCEPT_VALUE)
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
