package ly.david.data.network.api.coverart

import ly.david.data.coverart.CoverArtArchiveApiService
import ly.david.data.coverart.getFrontCoverArtUrl
import ly.david.data.persistence.release.ReleaseDao
import retrofit2.HttpException

/**
 * Logic to retrieve release cover art path.
 */
interface GetReleaseCoverArtPath {

    val coverArtArchiveApiService: CoverArtArchiveApiService

    // TODO: can't move to coverart module unless daos are moved to base
    val releaseDao: ReleaseDao

    /**
     * Returns a url to the cover art. Empty if none found.
     *
     * Also set it in the release.
     */
    suspend fun getReleaseCoverArtPathFromNetwork(releaseId: String): String {
        return try {
            val url = coverArtArchiveApiService.getReleaseCoverArts(releaseId).getFrontCoverArtUrl().orEmpty()
            val coverArtPath = url.extractPathFromUrl()
            releaseDao.setReleaseCoverArtPath(releaseId, coverArtPath)
            return coverArtPath
        } catch (ex: HttpException) {
            if (ex.code() == 404) {
                releaseDao.setReleaseCoverArtPath(releaseId, "")
            }
            ""
        }
    }
}
