package ly.david.musicsearch.data.coverart

import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.data.coverart.api.CoverArtArchiveApi
import ly.david.musicsearch.data.coverart.api.getFrontCoverArtUrl
import ly.david.musicsearch.data.coverart.api.getFrontThumbnailCoverArtUrl
import ly.david.musicsearch.shared.domain.image.ImageUrlDao
import ly.david.musicsearch.shared.domain.image.ImageUrls
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupImageRepository

internal class ReleaseGroupImageRepositoryImpl(
    private val coverArtArchiveApi: CoverArtArchiveApi,
    private val imageUrlDao: ImageUrlDao,
    private val logger: Logger,
) : ReleaseGroupImageRepository {

    override suspend fun getReleaseGroupImageUrl(
        releaseGroupId: String,
        forceRefresh: Boolean,
    ): ImageUrls {
        if (forceRefresh) {
            imageUrlDao.deleteAllUrlsById(releaseGroupId)
        }

        val cachedImageUrls = imageUrlDao.getFrontCoverUrl(releaseGroupId)
        return if (cachedImageUrls == null) {
            saveReleaseGroupCoverArtUrlFromNetwork(releaseGroupId)
            getReleaseGroupImageUrl(
                releaseGroupId = releaseGroupId,
                forceRefresh = false,
            )
        } else {
            cachedImageUrls
        }
    }

    private suspend fun saveReleaseGroupCoverArtUrlFromNetwork(
        releaseGroupId: String,
    ) {
        try {
            val coverArts = coverArtArchiveApi.getReleaseGroupCoverArts(releaseGroupId)
            val thumbnailUrl = coverArts.getFrontThumbnailCoverArtUrl().orEmpty()
            val largeUrl = coverArts.getFrontCoverArtUrl().orEmpty()

            imageUrlDao.deleteAllUrlsById(releaseGroupId)
            imageUrlDao.saveUrls(
                mbid = releaseGroupId,
                imageUrls = listOf(
                    ImageUrls(
                        thumbnailUrl = thumbnailUrl.removeFileExtension(),
                        largeUrl = largeUrl.removeFileExtension(),
                    ),
                ),
            )
        } catch (ex: Exception) {
            logger.e(ex)
            imageUrlDao.saveUrls(
                mbid = releaseGroupId,
                imageUrls = listOf(ImageUrls()),
            )
        }
    }
}
