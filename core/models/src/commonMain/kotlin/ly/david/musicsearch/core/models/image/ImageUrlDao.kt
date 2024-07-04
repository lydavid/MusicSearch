package ly.david.musicsearch.core.models.image

interface ImageUrlDao {
    fun saveUrls(
        mbid: String,
        imageUrls: List<ImageUrls>,
    )

    fun getAllUrls(mbid: String): List<ImageUrls>
}
