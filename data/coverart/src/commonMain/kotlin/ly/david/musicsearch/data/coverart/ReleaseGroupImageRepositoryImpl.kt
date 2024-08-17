package ly.david.musicsearch.data.coverart

import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.shared.domain.image.ImageUrlDao
import ly.david.musicsearch.shared.domain.image.ImageUrls
import ly.david.musicsearch.data.coverart.api.CoverArtArchiveApi
import ly.david.musicsearch.data.coverart.api.getFrontCoverArtUrl
import ly.david.musicsearch.data.coverart.api.getFrontThumbnailCoverArtUrl
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupImageRepository

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
            val largeUrl = coverArts.getFrontCoverArtUrl().orEmpty()
            imageUrlDao.saveUrls(
                mbid = releaseGroupId,
                imageUrls = listOf(
                    ImageUrls(
                        thumbnailUrl = thumbnailUrl.removeFileExtension(),
                        largeUrl = largeUrl.removeFileExtension(),
                    ),
                ),
            )
            return if (thumbnail) thumbnailUrl else largeUrl
        } catch (ex: ClientRequestException) {
            if (ex.response.status == HttpStatusCode.NotFound) {
                imageUrlDao.saveUrls(
                    mbid = releaseGroupId,
                    imageUrls = listOf(
                        ImageUrls(
                            thumbnailUrl = "",
                            largeUrl = "",
                        ),
                    ),
                )
            } else {
                logger.e(ex)
            }
            ""
        }
    }
}
