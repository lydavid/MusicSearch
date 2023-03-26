package ly.david.data.network.api

import ly.david.data.coverart.api.CoverArtArchiveApiService
import ly.david.data.coverart.api.CoverArtUrls
import ly.david.data.coverart.api.CoverArtsResponse

class FakeCoverArtArchiveApiService : CoverArtArchiveApiService {
    override suspend fun getReleaseCoverArts(releaseId: String): CoverArtsResponse {
        return CoverArtsResponse(
            coverArtUrls = listOf(
                CoverArtUrls(
                    id = "b",
                    imageUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                    front = true
                )
            ),
            release = "blah"
        )
    }

    override suspend fun getReleaseGroupCoverArts(releaseGroupId: String): CoverArtsResponse {
        return CoverArtsResponse(
            coverArtUrls = listOf(
                CoverArtUrls(
                    id = "b",
                    imageUrl = "http://coverartarchive.org/release/00e48019-5901-4110-b44d-875c3026491b/247510391.png",
                    front = true
                )
            ),
            release = "blah"
        )
    }
}
