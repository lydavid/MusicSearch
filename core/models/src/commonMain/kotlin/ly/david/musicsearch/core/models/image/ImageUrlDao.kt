package ly.david.musicsearch.core.models.image

interface ImageUrlDao {
    fun saveUrl(mbid: String, thumbnailUrl: String, largeUrl: String)
}
