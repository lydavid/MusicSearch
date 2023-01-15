package ly.david.data.network.api.coverart

import ly.david.data.persistence.release.ReleaseDao
import retrofit2.HttpException

interface GetReleaseCoverArtUrl {

    val coverArtArchiveApiService: CoverArtArchiveApiService
    val releaseDao: ReleaseDao

    /**
     * Returns a url to the cover art. Empty if none found.
     *
     * Also set it in the release.
     */
    suspend fun getReleaseCoverArtUrlFromNetwork(releaseId: String): String {
        return try {
            val url = coverArtArchiveApiService.getReleaseCoverArts(releaseId).getSmallCoverArtUrl().orEmpty()
            releaseDao.setReleaseCoverArtPath(releaseId, url.split("/").last())
            return url
        } catch (ex: HttpException) {
            if (ex.code() == 404) {
                releaseDao.setReleaseCoverArtPath(releaseId, "")
            }
            ""
        }
    }
}
