package ly.david.musicsearch.data.coverart.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

private const val BASE_URL = "https://coverartarchive.org"
private const val RELEASE_URL = "$BASE_URL/release"
private const val RELEASE_GROUP_URL = "$BASE_URL/release-group"
private const val EVENT_URL = "https://eventartarchive.org/event"

internal class CoverArtArchiveApiImpl(
    private val client: HttpClient,
) : CoverArtArchiveApi {
    override suspend fun getCoverArts(
        mbid: String,
        entity: MusicBrainzEntityType,
    ): CoverArtsResponse {
        val url = when (entity) {
            MusicBrainzEntityType.RELEASE -> RELEASE_URL
            MusicBrainzEntityType.RELEASE_GROUP -> RELEASE_GROUP_URL
            MusicBrainzEntityType.EVENT -> EVENT_URL
            else -> error("$entity cover arts not supported.")
        }
        return client.get(url) {
            url {
                appendPathSegments(mbid)
            }
        }.body()
    }
}
