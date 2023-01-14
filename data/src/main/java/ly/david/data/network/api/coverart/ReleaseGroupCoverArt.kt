package ly.david.data.network.api.coverart

import ly.david.data.persistence.releasegroup.ReleaseGroupDao

/**
 * Logic to retrieve release group cover art urls
 */
interface ReleaseGroupCoverArt {

    val coverArtArchiveApiService: CoverArtArchiveApiService
    val releaseGroupDao: ReleaseGroupDao

    suspend fun getReleaseGroupCoverArtUrlFromNetwork(releaseGroupId: String): String {
        return try {
            val url = coverArtArchiveApiService.getReleaseGroupCoverArts(releaseGroupId).getSmallCoverArtUrl().orEmpty()
            releaseGroupDao.withTransaction {
                val splitUrl = url.split("/")
                val coverArtPath = if (splitUrl.size < 2) {
                    ""
                } else {
                    "${splitUrl[splitUrl.lastIndex - 1]}/${splitUrl.last()}"
                }
                releaseGroupDao.setReleaseGroupCoverArtPath(releaseGroupId, coverArtPath)
            }
            return url
        } catch (ex: Exception) {
            // Because we throw on 404 when we don't find a cover art, we need to make sure we still set empty in room
            // to indicate we shouldn't retry this next time.
            releaseGroupDao.setReleaseGroupCoverArtPath(releaseGroupId, "")
            ""
        }
    }
}
