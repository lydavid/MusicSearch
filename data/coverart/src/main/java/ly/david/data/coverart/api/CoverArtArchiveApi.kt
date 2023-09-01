package ly.david.data.coverart.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val BASE_URL = "https://coverartarchive.org"
private const val RELEASE = "$BASE_URL/release"
private const val RELEASE_GROUP = "$BASE_URL/release-group"

interface CoverArtArchiveApi {
    /**
     * This is used to get the URLs for this release.
     * So after calling this, we need to make another API call based on the retrieved URLs.
     * The image loading library will generally handle that next call.
     */
    suspend fun getReleaseCoverArts(releaseId: String): CoverArtsResponse

    /**
     * This is used to get the URLs for this release group.
     * So after calling this, we need to make another API call based on the retrieved URLs.
     * The image loading library will generally handle that next call.
     */
    suspend fun getReleaseGroupCoverArts(releaseGroupId: String): CoverArtsResponse

    companion object {
        fun create(
            engine: HttpClientEngine,
        ): CoverArtArchiveApi {
            val client = HttpClient(engine) {
                expectSuccess = true

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
            }

            return CoverArtArchiveApiImpl(
                client = client
            )
        }
    }
}

class CoverArtArchiveApiImpl(
    private val client: HttpClient,
) : CoverArtArchiveApi {
    override suspend fun getReleaseCoverArts(releaseId: String): CoverArtsResponse {
        return client.get(RELEASE) {
            url {
                appendPathSegments(releaseId)
            }
        }.body()
    }

    override suspend fun getReleaseGroupCoverArts(releaseGroupId: String): CoverArtsResponse {
        return client.get(RELEASE_GROUP) {
            url {
                appendPathSegments(releaseGroupId)
            }
        }.body()
    }
}
