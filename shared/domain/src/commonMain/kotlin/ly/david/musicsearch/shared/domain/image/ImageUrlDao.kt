package ly.david.musicsearch.shared.domain.image

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow

interface ImageUrlDao {
    fun saveImageMetadata(
        mbid: String,
        imageMetadataList: List<RawImageMetadata>,
    )

    fun saveImageMetadata(
        mbidToImageMetadataMap: Map<String, List<RawImageMetadata>>,
    )

    fun getFrontImageMetadata(mbid: String): ImageMetadataWithCount?

    fun getAllImageMetadataById(
        mbid: String,
        query: String = "",
    ): PagingSource<Int, ImageMetadataWithEntity>

    fun getAllImageMetadata(
        query: String = "",
        sortOption: ImagesSortOption,
    ): PagingSource<Int, ImageMetadataWithEntity>

    fun observeCountOfAllImageMetadata(): Flow<Long>

    fun deleteAllImageMetadtaById(mbid: String)
}
