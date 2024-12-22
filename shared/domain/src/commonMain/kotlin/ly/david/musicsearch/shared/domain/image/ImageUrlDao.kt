package ly.david.musicsearch.shared.domain.image

interface ImageUrlDao {
    fun saveUrls(
        mbid: String,
        imageUrls: List<ImageUrls>,
    )

    fun getFrontCoverUrl(mbid: String): ImageUrls?

    fun getAllUrlsById(
        mbid: String,
        query: String = "",
    ): List<ImageUrls>

    fun deleteAllUrlsById(mbid: String)

    fun getNumberOfImagesById(mbid: String): Long
}
