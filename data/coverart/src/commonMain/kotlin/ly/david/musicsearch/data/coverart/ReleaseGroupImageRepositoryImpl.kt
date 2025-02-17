package ly.david.musicsearch.data.coverart

import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.data.coverart.api.CoverArtArchiveApi
import ly.david.musicsearch.data.coverart.api.getFrontCoverArtUrl
import ly.david.musicsearch.data.coverart.api.getFrontThumbnailCoverArtUrl
import ly.david.musicsearch.shared.domain.error.ErrorResolution
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.image.ImageUrlDao
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupImageRepository

internal class ReleaseGroupImageRepositoryImpl(
    private val coverArtArchiveApi: CoverArtArchiveApi,
    private val imageUrlDao: ImageUrlDao,
    private val logger: Logger,
) : ReleaseGroupImageRepository {

    override suspend fun getImageMetadata(
        mbid: String,
        forceRefresh: Boolean,
    ): ImageMetadata {
        if (forceRefresh) {
            imageUrlDao.deleteAllImageMetadtaById(mbid)
        }

        val cachedImageMetadata = imageUrlDao.getFrontImageMetadata(mbid)
        return if (cachedImageMetadata == null) {
            saveReleaseGroupImageMetadataFromNetwork(mbid)
            imageUrlDao.getFrontImageMetadata(mbid) ?: ImageMetadata()
        } else {
            cachedImageMetadata
        }
    }

    private suspend fun saveReleaseGroupImageMetadataFromNetwork(
        releaseGroupId: String,
    ) {
        try {
            val coverArts = coverArtArchiveApi.getReleaseGroupCoverArts(releaseGroupId)
            val thumbnailUrl = coverArts.getFrontThumbnailCoverArtUrl().orEmpty()
            val largeUrl = coverArts.getFrontCoverArtUrl().orEmpty()

            imageUrlDao.saveImageMetadata(
                mbid = releaseGroupId,
                imageMetadataList = listOf(
                    ImageMetadata(
                        thumbnailUrl = thumbnailUrl.removeFileExtension(),
                        largeUrl = largeUrl.removeFileExtension(),
                    ),
                ),
            )
        } catch (ex: HandledException) {
            if (ex.errorResolution == ErrorResolution.None) {
                imageUrlDao.saveImageMetadata(
                    mbid = releaseGroupId,
                    imageMetadataList = listOf(ImageMetadata()),
                )
            } else {
                logger.e(ex)
            }
        } catch (ex: Exception) {
            logger.e(ex)
        }
    }
}
