package ly.david.musicsearch.data.coverart

import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import ly.david.musicsearch.core.models.image.ImageUrlDao
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.data.coverart.api.CoverArtArchiveApi
import ly.david.musicsearch.data.coverart.api.getFrontLargeCoverArtUrl
import ly.david.musicsearch.data.coverart.api.getFrontThumbnailCoverArtUrl
import ly.david.musicsearch.domain.release.ReleaseImageRepository

internal class ReleaseImageRepositoryImpl(
    private val coverArtArchiveApi: CoverArtArchiveApi,
    private val imageUrlDao: ImageUrlDao,
    private val logger: Logger,
) : ReleaseImageRepository {
    override suspend fun getReleaseCoverArtUrlFromNetwork(
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
