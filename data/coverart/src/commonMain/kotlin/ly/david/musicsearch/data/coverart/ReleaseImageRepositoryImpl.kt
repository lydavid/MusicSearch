package ly.david.musicsearch.data.coverart

import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.data.coverart.api.CoverArtArchiveApi
import ly.david.musicsearch.data.coverart.api.CoverArtsResponse
import ly.david.musicsearch.data.coverart.api.toImageUrlsList
import ly.david.musicsearch.shared.domain.error.ErrorResolution
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.image.ImageUrlDao
import ly.david.musicsearch.shared.domain.image.ImageUrls
import ly.david.musicsearch.shared.domain.release.ReleaseImageRepository

internal class ReleaseImageRepositoryImpl(
    private val coverArtArchiveApi: CoverArtArchiveApi,
    private val imageUrlDao: ImageUrlDao,
    private val logger: Logger,
) : ReleaseImageRepository {

    override suspend fun getReleaseImageUrl(
        releaseId: String,
        thumbnail: Boolean,
        forceRefresh: Boolean,
    ): String {
        if (forceRefresh) {
            imageUrlDao.deleteAllUrlsById(releaseId)
        }

        val cachedImageUrls = imageUrlDao.getAllUrlsById(releaseId)
        return if (cachedImageUrls.isNotEmpty()) {
            val frontCoverArt = cachedImageUrls.first()
            return if (thumbnail) frontCoverArt.thumbnailUrl else frontCoverArt.largeUrl
        } else {
            getReleaseImageUrlFromNetwork(releaseId, thumbnail)
        }
    }

    private suspend fun getReleaseImageUrlFromNetwork(
        releaseId: String,
        thumbnail: Boolean,
    ): String {
        return try {
            val coverArts: CoverArtsResponse = coverArtArchiveApi.getReleaseCoverArts(releaseId)
            val imageUrls: MutableList<ImageUrls> = coverArts.toImageUrlsList().toMutableList()

            // We use an empty ImageUrls to represent that we've searched but failed to find any images.
            if (imageUrls.isEmpty()) {
                imageUrls.add(ImageUrls())
            }

            imageUrlDao.saveUrls(
                mbid = releaseId,
                imageUrls = imageUrls,
            )
            val frontCoverArt = imageUrls.first()
            return if (thumbnail) frontCoverArt.thumbnailUrl else frontCoverArt.largeUrl
        } catch (ex: HandledException) {
            if (ex.errorResolution == ErrorResolution.None) {
                imageUrlDao.saveUrls(
                    mbid = releaseId,
                    imageUrls = listOf(ImageUrls()),
                )
            } else {
                logger.e(ex)
            }
            ""
        } catch (ex: Exception) {
            logger.e(ex)
            ""
        }
    }

    override fun getAllUrlsById(mbid: String): List<ImageUrls> {
        return imageUrlDao.getAllUrlsById(mbid = mbid)
    }

    override fun getNumberOfImagesById(mbid: String): Int {
        return imageUrlDao.getNumberOfImagesById(mbid).toInt()
    }
}
