package ly.david.data.coverart

import ly.david.data.coverart.api.CoverArtArchiveApiService
import ly.david.data.coverart.api.getFrontCoverArtUrl
import retrofit2.HttpException

/**
 * Logic to retrieve release group cover art path.
 */
interface GetReleaseGroupCoverArtPath {

    val coverArtArchiveApiService: CoverArtArchiveApiService
    val updateReleaseGroupCoverArtDao: UpdateReleaseGroupCoverArtDao

    /**
     * Returns an appropriate cover art for the release group with [releaseGroupId].
     * Empty if none found.
     *
     * Also set it in the release group.
     *
     * Make sure to handle non-404 errors at call site.
     */
    suspend fun getReleaseGroupCoverArtPathFromNetwork(releaseGroupId: String): String {
        return try {
            val url = coverArtArchiveApiService.getReleaseGroupCoverArts(releaseGroupId).getFrontCoverArtUrl().orEmpty()
            val coverArtPath = url.extractPathFromUrl()
            updateReleaseGroupCoverArtDao.setReleaseGroupCoverArtPath(releaseGroupId, coverArtPath)
            return coverArtPath
        } catch (ex: HttpException) {
            if (ex.code() == 404) {
                updateReleaseGroupCoverArtDao.setReleaseGroupCoverArtPath(releaseGroupId, "")
            }
            ""
        }
    }
}
