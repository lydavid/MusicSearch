package ly.david.data.core.image

interface ImageUrlDao {
    fun saveUrl(mbid: String, thumbnailUrl: String, largeUrl: String)
}
