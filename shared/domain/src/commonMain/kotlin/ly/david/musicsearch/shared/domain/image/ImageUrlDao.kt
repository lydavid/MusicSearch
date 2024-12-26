package ly.david.musicsearch.shared.domain.image

import app.cash.paging.PagingSource

interface ImageUrlDao {
    fun saveUrls(
        mbid: String,
        imageUrls: List<ImageUrls>,
    )

    fun getFrontCoverUrl(mbid: String): ImageUrls?

    fun getAllUrlsById(
        mbid: String,
        query: String = "",
    ): PagingSource<Int, ImageUrls>

    fun getAllUrls(
        query: String = "",
    ): PagingSource<Int, ImageUrls>

    fun deleteAllUrlsById(mbid: String)

    fun getNumberOfImagesById(mbid: String): Long
}
