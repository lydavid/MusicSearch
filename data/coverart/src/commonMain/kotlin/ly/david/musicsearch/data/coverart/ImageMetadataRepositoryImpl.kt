package ly.david.musicsearch.data.coverart

import androidx.paging.cachedIn
import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.data.coverart.api.CoverArtArchiveApi
import ly.david.musicsearch.data.coverart.api.CoverArtsResponse
import ly.david.musicsearch.data.coverart.api.toImageMetadataList
import ly.david.musicsearch.shared.domain.coverarts.ImagesSortOption
import ly.david.musicsearch.shared.domain.error.ErrorResolution
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.image.ImageUrlDao
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.image.ImageMetadataRepository

internal class ImageMetadataRepositoryImpl(
    private val coverArtArchiveApi: CoverArtArchiveApi,
    private val imageUrlDao: ImageUrlDao,
    private val logger: Logger,
    private val coroutineScope: CoroutineScope,
) : ImageMetadataRepository {

    override suspend fun getImageMetadata(
        mbid: String,
        entity: MusicBrainzEntity,
        forceRefresh: Boolean,
    ): ImageMetadata {
        if (forceRefresh) {
            imageUrlDao.deleteAllImageMetadtaById(mbid)
        }

        val cachedImageMetadata = imageUrlDao.getFrontImageMetadata(mbid)
        return if (cachedImageMetadata == null) {
            saveImageMetadataFromNetwork(mbid, entity)
            imageUrlDao.getFrontImageMetadata(mbid) ?: ImageMetadata()
        } else {
            cachedImageMetadata
        }
    }

    private suspend fun saveImageMetadataFromNetwork(
        mbid: String,
        entity: MusicBrainzEntity,
    ) {
        try {
            val coverArts: CoverArtsResponse = coverArtArchiveApi.getCoverArts(mbid, entity)
            val imageMetadataList: MutableList<ImageMetadata> = coverArts.toImageMetadataList().toMutableList()

            // We use an empty ImageUrls to represent that we've searched but failed to find any images.
            if (imageMetadataList.isEmpty()) {
                imageMetadataList.add(ImageMetadata())
            }

            imageUrlDao.saveImageMetadata(
                mbid = mbid,
                imageMetadataList = imageMetadataList,
            )
        } catch (ex: HandledException) {
            if (ex.errorResolution == ErrorResolution.None) {
                imageUrlDao.saveImageMetadata(
                    mbid = mbid,
                    imageMetadataList = listOf(ImageMetadata()),
                )
            } else {
                logger.e(ex)
            }
        } catch (ex: Exception) {
            logger.e(ex)
        }
    }

    override fun observeAllImageMetadata(
        mbid: String?,
        query: String,
        sortOption: ImagesSortOption,
    ): Flow<PagingData<ImageMetadata>> = Pager(
        config = PagingConfig(
            pageSize = 100,
            initialLoadSize = 100,
            prefetchDistance = 50,
        ),
        pagingSourceFactory = {
            if (mbid == null) {
                imageUrlDao.getAllImageMetadata(
                    query = query,
                    sortOption = sortOption,
                )
            } else {
                imageUrlDao.getAllImageMetadataById(
                    mbid = mbid,
                    query = query,
                )
            }
        },
    ).flow
        .distinctUntilChanged()
        .cachedIn(scope = coroutineScope)

    override fun getNumberOfImageMetadataById(mbid: String): Int {
        return imageUrlDao.getNumberOfImagesById(mbid).toInt()
    }
}
