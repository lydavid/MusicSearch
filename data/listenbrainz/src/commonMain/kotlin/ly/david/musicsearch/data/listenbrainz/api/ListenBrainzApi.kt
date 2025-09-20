package ly.david.musicsearch.data.listenbrainz.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.AuthProvider
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.http.appendPathSegments
import io.ktor.http.auth.HttpAuthHeader
import ly.david.musicsearch.shared.domain.USER_AGENT_VALUE
import ly.david.musicsearch.shared.domain.common.ifNotEmpty
import ly.david.musicsearch.shared.domain.listen.ListenBrainzAuthStore

private const val API_BASE_URL = "https://api.listenbrainz.org/1/"

private const val AUTHORIZATION = "Authorization"

interface ListenBrainzApi {
    suspend fun validateToken(token: String): TokenValidationResponse

    suspend fun getListensByUser(
        username: String,
        minTimestamp: Long? = null,
        maxTimestamp: Long? = null,
    ): ListensResponse

    suspend fun getManualMapping(
        recordingMessyBrainzId: String,
    ): ManualMappingResponse

    suspend fun getRecordingMetadata(
        recordingMusicBrainzId: String,
    ): RecordingMetadata?

    companion object {
        fun create(
            httpClient: HttpClient,
            authStore: ListenBrainzAuthStore,
        ): ListenBrainzApi {
            val extendedClient = httpClient.config {
                defaultRequest {
                    url(API_BASE_URL)
                }
                install(UserAgent) {
                    agent = USER_AGENT_VALUE
                }
                install(Auth) {
                    providers += object : AuthProvider {
                        override val sendWithoutRequest: Boolean = true

                        override suspend fun addRequestHeaders(
                            request: HttpRequestBuilder,
                            authHeader: HttpAuthHeader?,
                        ) {
                            val token = authStore.getUserToken()
                            token.ifNotEmpty {
                                request.headers[AUTHORIZATION] = "Token $token"
                            }
                        }

                        override fun isApplicable(auth: HttpAuthHeader): Boolean = false
                    }
                }
            }
            return ListenBrainzApiImpl(extendedClient)
        }
    }
}

private const val LISTENS_COUNT = 100

class ListenBrainzApiImpl(
    private val httpClient: HttpClient,
) : ListenBrainzApi {

    override suspend fun validateToken(token: String): TokenValidationResponse {
        return httpClient.get {
            url {
                appendPathSegments("validate-token")
                header(AUTHORIZATION, "Token $token")
            }
        }.body()
    }

    // This will likely timeout if the user has listens spread far apart in time:
    //  https://tickets.metabrainz.org/browse/LB-1584
    override suspend fun getListensByUser(
        username: String,
        minTimestamp: Long?,
        maxTimestamp: Long?,
    ): ListensResponse {
        require(!(minTimestamp != null && maxTimestamp != null)) {
            "minTimestamp and maxTimestamp cannot both be set"
        }
        return httpClient.get {
            url {
                appendPathSegments("user/$username/listens")
                // TODO: have a background task to load 1000 at a time if the user wants to load all
                parameter("count", LISTENS_COUNT)
                parameter("min_ts", minTimestamp)
                parameter("max_ts", maxTimestamp)
            }
        }.body()
    }

    override suspend fun getManualMapping(recordingMessyBrainzId: String): ManualMappingResponse {
        return httpClient.get {
            url {
                appendPathSegments("metadata/get_manual_mapping/")
                parameter("recording_msid", recordingMessyBrainzId)
            }
        }.body()
    }

    override suspend fun getRecordingMetadata(recordingMusicBrainzId: String): RecordingMetadata? {
        return httpClient.get {
            url {
                appendPathSegments("metadata/recording/")
                parameter("recording_mbids", recordingMusicBrainzId)
                parameter("inc", "artist release")
            }
        }.body<Map<String, RecordingMetadata>>()[recordingMusicBrainzId]
    }
}
