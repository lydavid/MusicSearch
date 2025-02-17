package ly.david.data.test.api

import ly.david.musicsearch.data.coverart.api.CoverArtArchiveApi
import ly.david.musicsearch.data.coverart.api.CoverArtUrls
import ly.david.musicsearch.data.coverart.api.CoverArtsResponse
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

open class FakeCoverArtArchiveApi : CoverArtArchiveApi {
    override suspend fun getCoverArts(
        mbid: String,
        entity: MusicBrainzEntity,
    ): CoverArtsResponse {
        return CoverArtsResponse(
            coverArtUrls = listOf(
                CoverArtUrls(
                    imageUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                    front = true,
                ),
            ),
        )
    }
}
