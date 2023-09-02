package ly.david.data.coverart

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode.Companion.NotFound
import javax.inject.Inject
import ly.david.data.coverart.api.CoverArtArchiveApi
import ly.david.data.coverart.api.getFrontLargeCoverArtUrl
import ly.david.data.coverart.api.getFrontThumbnailCoverArtUrl
import ly.david.data.core.image.ImageUrlSaver

/**
 * Logic to retrieve release cover art path.
 */
class ReleaseImageRepository @Inject constructor(
    private val coverArtArchiveApi: CoverArtArchiveApi,
    private val imageUrlSaver: ImageUrlSaver,
) {

    /**
     * Returns a url to the cover art.
     * Empty if none found.
     *
     * Also saves it to db.
     *
     * Make sure to handle non-404 errors at call site.
     */
    suspend fun getReleaseCoverArtUrlFromNetwork(
        releaseId: String,
        thumbnail: Boolean,
    ): String {
        return try {
            val coverArts = coverArtArchiveApi.getReleaseCoverArts(releaseId)
            val thumbnailUrl = coverArts.getFrontThumbnailCoverArtUrl().orEmpty()
            val largeUrl = coverArts.getFrontLargeCoverArtUrl().orEmpty()
            imageUrlSaver.saveUrl(
                mbid = releaseId,
                thumbnailUrl = thumbnailUrl.removeFileExtension(),
                largeUrl = largeUrl.removeFileExtension()
            )
            return if (thumbnail) thumbnailUrl else largeUrl
        } catch (ex: ClientRequestException) {
            if (ex.response.status == NotFound) {
                imageUrlSaver.saveUrl(
                    mbid = releaseId,
                    thumbnailUrl = "",
                    largeUrl = ""
                )
            } else {
                // TODO: log
            }
            ""
        } catch (ex: ServerResponseException) {
            // TODO: should offer retry
            ""
        }
    }
}
