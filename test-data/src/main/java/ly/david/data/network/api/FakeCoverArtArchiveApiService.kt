package ly.david.data.network.api

import ly.david.data.network.api.coverart.CoverArtArchiveApiService
import ly.david.data.network.api.coverart.CoverArtUrls
import ly.david.data.network.api.coverart.CoverArtsResponse
import ly.david.data.network.api.coverart.ThumbnailsUrls

class FakeCoverArtArchiveApiService : CoverArtArchiveApiService {

    override suspend fun getReleaseGroupCoverArts(releaseGroupId: String): CoverArtsResponse {
        return TODO()
    }

    override suspend fun getReleaseCoverArts(releaseId: String): CoverArtsResponse {
        return CoverArtsResponse(
            coverArtUrls = listOf(
                CoverArtUrls(
                    id = releaseId,
                    imageUrl = "500",
                    thumbnailsUrls = ThumbnailsUrls(
                        resolution250Url = "http://coverartarchive.org/release/165f6643-2edb-4795-9abe-26bd0533e59d/30440741667-250.jpg",
                        resolution500Url = "500",
                        resolution1200Url = "1200"
                    ),
                    front = true
                )
            ),
            release = "https://musicbrainz.org/release/45cfff65-0808-45a7-a087-a536a1af1f5e"
        )
    }
}
