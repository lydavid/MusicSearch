package ly.david.musicsearch.shared.domain.image

interface ImageUrlDao {
    fun saveUrls(
        mbid: String,
        imageUrls: List<ImageUrls>,
    )

    fun getAllUrlsById(mbid: String): List<ImageUrls>

    fun deleteAllUrlsById(mbid: String)

    fun getNumberOfImagesById(mbid: String): Long
}
