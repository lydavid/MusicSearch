package ly.david.musicsearch.data.coverart

import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.core.models.image.ImageUrlDao
import ly.david.musicsearch.core.models.image.ImageUrls
import ly.david.musicsearch.data.coverart.api.CoverArtArchiveApi
import ly.david.musicsearch.data.coverart.api.toImageUrlsList
import ly.david.musicsearch.shared.domain.release.ReleaseImageRepository

internal class ReleaseImageRepositoryImpl(
    private val coverArtArchiveApi: CoverArtArchiveApi,
    private val imageUrlDao: ImageUrlDao,
    private val logger: Logger,
) : ReleaseImageRepository {
    override suspend fun getReleaseCoverArtUrlsFromNetworkAndSave(
        releaseId: String,
        thumbnail: Boolean,
    ): String {
        return try {
            val coverArts = coverArtArchiveApi.getReleaseCoverArts(releaseId)
            val imageUrls: MutableList<ImageUrls> = coverArts.toImageUrlsList().toMutableList()
            if (imageUrls.isEmpty()) {
                imageUrls.add(ImageUrls())
            }
            imageUrlDao.saveUrls(
                mbid = releaseId,
                imageUrls = imageUrls,
            )
            return if (thumbnail) imageUrls.first().thumbnailUrl else imageUrls.first().largeUrl
        } catch (ex: ClientRequestException) {
            if (ex.response.status == HttpStatusCode.NotFound) {
                imageUrlDao.saveUrls(
                    mbid = releaseId,
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

    override fun getAllUrls(mbid: String): List<ImageUrls> {
        return imageUrlDao.getAllUrls(mbid = mbid)
    }
}
