package ly.david.data.coverart

import java.net.HttpURLConnection.HTTP_NOT_FOUND
import ly.david.data.coverart.api.CoverArtArchiveApi
import ly.david.data.coverart.api.getFrontLargeCoverArtUrl
import ly.david.data.coverart.api.getFrontThumbnailCoverArtUrl
import ly.david.data.image.ImageUrlSaver
import retrofit2.HttpException

/**
 * Logic to retrieve release group cover art path.
 */
interface ReleaseGroupImageManager {

    val coverArtArchiveApi: CoverArtArchiveApi
    val imageUrlSaver: ImageUrlSaver

    /**
     * Returns an appropriate cover art for the release group with [releaseGroupId].
     * Empty if none found.
     *
     * Also saves it to db.
     *
     * Make sure to handle non-404 errors at call site.
     */
    suspend fun getReleaseGroupCoverArtUrlFromNetwork(
        releaseGroupId: String,
        thumbnail: Boolean,
    ): String {
        return try {
            val coverArts = coverArtArchiveApi.getReleaseGroupCoverArts(releaseGroupId)
            val thumbnailUrl = coverArts.getFrontThumbnailCoverArtUrl().orEmpty()
            val largeUrl = coverArts.getFrontLargeCoverArtUrl().orEmpty()
            imageUrlSaver.saveUrl(
                mbid = releaseGroupId,
                thumbnailUrl = thumbnailUrl.removeFileExtension(),
                largeUrl = largeUrl.removeFileExtension()
            )
            return if (thumbnail) thumbnailUrl else largeUrl
        } catch (ex: HttpException) {
            if (ex.code() == HTTP_NOT_FOUND) {
                imageUrlSaver.saveUrl(
                    mbid = releaseGroupId,
                    thumbnailUrl = "",
                    largeUrl = ""
                )
            }
            ""
        }
    }
}
