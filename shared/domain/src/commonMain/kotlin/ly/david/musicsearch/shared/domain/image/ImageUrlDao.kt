package ly.david.musicsearch.shared.domain.image

import app.cash.paging.PagingSource
import kotlinx.coroutines.flow.Flow

interface ImageUrlDao {
    fun saveImageMetadata(
        mbid: String,
        imageMetadataList: List<ImageMetadata>,
    )

    fun saveImageMetadata(
        mbidToImageMetadataMap: Map<String, List<ImageMetadata>>,
    )

    fun getFrontImageMetadata(mbid: String): ImageMetadata?

    fun getAllImageMetadataById(
        mbid: String,
        query: String = "",
    ): PagingSource<Int, ImageMetadata>

    fun getAllImageMetadata(
        query: String = "",
        sortOption: ImagesSortOption,
    ): PagingSource<Int, ImageMetadata>

    fun observeCountOfAllImageMetadata(): Flow<Long>

    fun deleteAllImageMetadtaById(mbid: String)

    fun getNumberOfImagesById(mbid: String): Long
}
