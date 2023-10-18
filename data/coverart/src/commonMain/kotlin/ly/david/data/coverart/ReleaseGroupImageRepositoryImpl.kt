package ly.david.data.coverart

import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import ly.david.musicsearch.core.models.image.ImageUrlDao
import ly.david.musicsearch.core.logging.Logger
import ly.david.data.coverart.api.CoverArtArchiveApi
import ly.david.data.coverart.api.getFrontLargeCoverArtUrl
import ly.david.data.coverart.api.getFrontThumbnailCoverArtUrl

internal class ReleaseGroupImageRepositoryImpl(
    private val coverArtArchiveApi: CoverArtArchiveApi,
    private val imageUrlDao: ImageUrlDao,
    private val logger: Logger,
) : ReleaseGroupImageRepository {
    override suspend fun getReleaseGroupCoverArtUrlFromNetwork(
        releaseGroupId: String,
        thumbnail: Boolean,
    ): String {
        return try {
            val coverArts = coverArtArchiveApi.getReleaseGroupCoverArts(releaseGroupId)
            val thumbnailUrl = coverArts.getFrontThumbnailCoverArtUrl().orEmpty()
            val largeUrl = coverArts.getFrontLargeCoverArtUrl().orEmpty()
            imageUrlDao.saveUrl(
                mbid = releaseGroupId,
                thumbnailUrl = thumbnailUrl.removeFileExtension(),
                largeUrl = largeUrl.removeFileExtension(),
            )
            return if (thumbnail) thumbnailUrl else largeUrl
        } catch (ex: ClientRequestException) {
            if (ex.response.status == HttpStatusCode.NotFound) {
                imageUrlDao.saveUrl(
                    mbid = releaseGroupId,
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
