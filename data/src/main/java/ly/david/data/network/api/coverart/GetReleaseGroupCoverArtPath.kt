package ly.david.data.network.api.coverart

import ly.david.data.persistence.releasegroup.ReleaseGroupDao
import retrofit2.HttpException

/**
 * Logic to retrieve release group cover art path.
 */
interface GetReleaseGroupCoverArtPath {

    val coverArtArchiveApiService: CoverArtArchiveApiService
    val releaseGroupDao: ReleaseGroupDao

    /**
     * Returns an appropriate cover art for the release group with [releaseGroupId].
     * Empty if none found.
     *
     * Make sure to handle non-404 errors as call site.
     */
    suspend fun getReleaseGroupCoverArtPathFromNetwork(releaseGroupId: String): String {
        return try {
            val url = coverArtArchiveApiService.getReleaseGroupCoverArts(releaseGroupId).getFrontCoverArtUrl().orEmpty()
            val splitUrl = url.split("/")
            val coverArtPath = if (splitUrl.size < 2) {
                ""
            } else {
                "${splitUrl[splitUrl.lastIndex - 1]}/${splitUrl.last().replace(".jpg", "")}"
            }
            releaseGroupDao.setReleaseGroupCoverArtPath(releaseGroupId, coverArtPath)
            return coverArtPath
        } catch (ex: HttpException) {
            if (ex.code() == 404) {
                releaseGroupDao.setReleaseGroupCoverArtPath(releaseGroupId, "")
            }
            ""
        }
    }
}
