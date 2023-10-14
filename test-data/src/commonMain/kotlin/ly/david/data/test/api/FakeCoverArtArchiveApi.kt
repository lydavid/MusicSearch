package ly.david.data.test.api

import ly.david.data.coverart.api.CoverArtArchiveApi
import ly.david.data.coverart.api.CoverArtUrls
import ly.david.data.coverart.api.CoverArtsResponse

class FakeCoverArtArchiveApi : CoverArtArchiveApi {
    override suspend fun getReleaseCoverArts(releaseId: String): CoverArtsResponse {
        return CoverArtsResponse(
            coverArtUrls = listOf(
                CoverArtUrls(
                    id = "b",
                    imageUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                    front = true,
                ),
            ),
            releaseUrl = "blah",
        )
    }

    override suspend fun getReleaseGroupCoverArts(releaseGroupId: String): CoverArtsResponse {
        return CoverArtsResponse(
            coverArtUrls = listOf(
                CoverArtUrls(
                    id = "b",
                    imageUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                    front = true,
                ),
            ),
            releaseUrl = "blah",
        )
    }
}
