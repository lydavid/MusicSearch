package ly.david.data.coverart

import ly.david.data.coverart.api.CoverArtArchiveApiService
import ly.david.data.coverart.api.getFrontCoverArtUrl
import retrofit2.HttpException

/**
 * Logic to retrieve release cover art path.
 */
interface GetReleaseCoverArtPath {

    val coverArtArchiveApiService: CoverArtArchiveApiService
    val updateReleaseCoverArtDao: UpdateReleaseCoverArtDao

    /**
     * Returns a url to the cover art.
     * Empty if none found.
     *
     * Also set it in the release.
     *
     * Make sure to handle non-404 errors at call site.
     */
    suspend fun getReleaseCoverArtPathFromNetwork(releaseId: String): String {
        return try {
            val url = coverArtArchiveApiService.getReleaseCoverArts(releaseId).getFrontCoverArtUrl().orEmpty()
            val coverArtPath = url.extractPathFromUrl()
            updateReleaseCoverArtDao.setReleaseCoverArtPath(releaseId, coverArtPath)
            return coverArtPath
        } catch (ex: HttpException) {
            if (ex.code() == 404) {
                updateReleaseCoverArtDao.setReleaseCoverArtPath(releaseId, "")
            }
            ""
        }
    }
}
