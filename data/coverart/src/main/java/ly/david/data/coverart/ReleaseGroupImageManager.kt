package ly.david.data.coverart

import java.net.HttpURLConnection.HTTP_NOT_FOUND
import ly.david.data.coverart.api.CoverArtArchiveApiService
import ly.david.data.coverart.api.getFrontCoverArtUrl
import ly.david.data.image.ImageUrlSaver
import retrofit2.HttpException

/**
 * Logic to retrieve release group cover art path.
 */
interface ReleaseGroupImageManager {

    val coverArtArchiveApiService: CoverArtArchiveApiService
    val imageUrlSaver: ImageUrlSaver

    /**
     * Returns an appropriate cover art for the release group with [releaseGroupId].
     * Empty if none found.
     *
     * Also saves it to db.
     *
     * Make sure to handle non-404 errors at call site.
     */
    suspend fun getReleaseGroupCoverArtPathFromNetwork(releaseGroupId: String): String {
        return try {
            val url = coverArtArchiveApiService.getReleaseGroupCoverArts(releaseGroupId).getFrontCoverArtUrl().orEmpty()
            imageUrlSaver.saveUrl(releaseGroupId, url.removeFileExtension())
            return url
        } catch (ex: HttpException) {
            if (ex.code() == HTTP_NOT_FOUND) {
                imageUrlSaver.saveUrl(releaseGroupId, "")
            }
            ""
        }
    }
}
