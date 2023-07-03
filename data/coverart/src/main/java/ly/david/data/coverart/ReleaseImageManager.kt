package ly.david.data.coverart

import java.net.HttpURLConnection.HTTP_NOT_FOUND
import ly.david.data.coverart.api.CoverArtArchiveApiService
import ly.david.data.coverart.api.getFrontLargeCoverArtUrl
import ly.david.data.coverart.api.getFrontThumbnailCoverArtUrl
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
    suspend fun getReleaseCoverArtPathFromNetwork(
        releaseId: String,
        thumbnail: Boolean
    ): String {
        return try {
            val coverArts = coverArtArchiveApiService.getReleaseCoverArts(releaseId)
            val thumbnailUrl = coverArts.getFrontThumbnailCoverArtUrl().orEmpty()
            val largeUrl = coverArts.getFrontLargeCoverArtUrl().orEmpty()
            imageUrlSaver.saveUrl(
                mbid = releaseId,
                thumbnailUrl = thumbnailUrl.removeFileExtension(),
                largeUrl = largeUrl.removeFileExtension()
            )
            return if (thumbnail) thumbnailUrl else largeUrl
        } catch (ex: HttpException) {
            if (ex.code() == HTTP_NOT_FOUND) {
                imageUrlSaver.saveUrl(
                    mbid = releaseId,
                    thumbnailUrl = "",
                    largeUrl = ""
                )
            }
            ""
        }
    }
}
