package ly.david.musicsearch.shared.domain.image

import app.cash.paging.PagingSource
import ly.david.musicsearch.shared.domain.coverarts.ImagesSortOption

interface ImageUrlDao {
    fun saveImageMetadata(
        mbid: String,
        imageMetadataList: List<ImageMetadata>,
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

    fun deleteAllImageMetadtaById(mbid: String)

    fun getNumberOfImagesById(mbid: String): Long
}
