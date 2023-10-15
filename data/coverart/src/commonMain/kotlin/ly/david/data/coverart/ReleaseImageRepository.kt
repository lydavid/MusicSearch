package ly.david.data.coverart

import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import ly.david.musicsearch.core.models.image.ImageUrlDao
import ly.david.musicsearch.core.models.logging.Logger
import ly.david.data.coverart.api.CoverArtArchiveApi
import ly.david.data.coverart.api.getFrontLargeCoverArtUrl
import ly.david.data.coverart.api.getFrontThumbnailCoverArtUrl
import org.koin.core.annotation.Single

/**
 * Logic to retrieve release cover art path.
 */
@Single
class ReleaseImageRepository(
    private val coverArtArchiveApi: CoverArtArchiveApi,
    private val imageUrlDao: ImageUrlDao,
    private val logger: Logger,
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
            imageUrlDao.saveUrl(
                mbid = releaseId,
                thumbnailUrl = thumbnailUrl.removeFileExtension(),
                largeUrl = largeUrl.removeFileExtension(),
            )
            return if (thumbnail) thumbnailUrl else largeUrl
        } catch (ex: ClientRequestException) {
            if (ex.response.status == HttpStatusCode.NotFound) {
                imageUrlDao.saveUrl(
                    mbid = releaseId,
                    thumbnailUrl = "",
                    largeUrl = "",
                )
            } else {
                logger.e(ex)
            }
            ""
        }
    }
}
