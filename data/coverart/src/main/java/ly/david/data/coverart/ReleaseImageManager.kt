package ly.david.data.coverart

import java.net.HttpURLConnection.HTTP_NOT_FOUND
import ly.david.data.coverart.api.CoverArtArchiveApiService
import ly.david.data.coverart.api.getFrontCoverArtUrl
import ly.david.data.image.ImageUrlSaver
import retrofit2.HttpException

/**
 * Logic to retrieve release cover art path.
 */
interface ReleaseImageManager {

    val coverArtArchiveApiService: CoverArtArchiveApiService
    val imageUrlSaver: ImageUrlSaver

    /**
     * Returns a url to the cover art.
     * Empty if none found.
     *
     * Also saves it to db.
     *
     * Make sure to handle non-404 errors at call site.
     */
    suspend fun getReleaseCoverArtPathFromNetwork(releaseId: String): String {
        return try {
            val url = coverArtArchiveApiService.getReleaseCoverArts(releaseId).getFrontCoverArtUrl().orEmpty()
            imageUrlSaver.saveUrl(releaseId, url.removeFileExtension())
            return url
        } catch (ex: HttpException) {
            if (ex.code() == HTTP_NOT_FOUND) {
                imageUrlSaver.saveUrl(releaseId, "")
            }
            ""
        }
    }
}
