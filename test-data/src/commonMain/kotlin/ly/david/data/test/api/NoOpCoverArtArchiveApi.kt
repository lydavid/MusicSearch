package ly.david.data.test.api

import ly.david.musicsearch.data.coverart.api.CoverArtArchiveApi
import ly.david.musicsearch.data.coverart.api.CoverArtsResponse
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType

open class NoOpCoverArtArchiveApi : CoverArtArchiveApi {
    override suspend fun getCoverArts(
        mbid: String,
        entity: MusicBrainzEntityType,
    ): CoverArtsResponse {
        return CoverArtsResponse(
            coverArtUrls = listOf(),
        )
    }
}
