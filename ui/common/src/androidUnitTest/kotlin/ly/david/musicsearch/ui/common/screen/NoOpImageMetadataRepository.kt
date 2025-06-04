package ly.david.musicsearch.ui.common.screen

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.image.ImageMetadataRepository
import ly.david.musicsearch.shared.domain.image.ImageMetadataWithCount
import ly.david.musicsearch.shared.domain.image.ImagesSortOption
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity

class NoOpImageMetadataRepository : ImageMetadataRepository {
    override suspend fun getAndSaveImageMetadata(
        mbid: String,
        entity: MusicBrainzEntity,
        forceRefresh: Boolean,
    ): ImageMetadataWithCount {
        error("Not used")
    }

    override suspend fun saveImageMetadata(
        mbid: String,
        entity: MusicBrainzEntity,
        itemsCount: Int,
    ) {
        // No-op
    }

    override fun observeAllImageMetadata(
        mbid: String?,
        query: String,
        sortOption: ImagesSortOption,
    ): Flow<PagingData<ImageMetadata>> {
        error("Not used")
    }

    override fun observeCountOfAllImageMetadata(): Flow<Long> {
        error("Not used")
    }
}
