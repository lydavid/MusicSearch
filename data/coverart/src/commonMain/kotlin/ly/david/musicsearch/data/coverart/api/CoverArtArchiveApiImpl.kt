package ly.david.musicsearch.data.coverart.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments

private const val BASE_URL = "https://coverartarchive.org"
private const val RELEASE = "$BASE_URL/release"
private const val RELEASE_GROUP = "$BASE_URL/release-group"

internal class CoverArtArchiveApiImpl(
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
