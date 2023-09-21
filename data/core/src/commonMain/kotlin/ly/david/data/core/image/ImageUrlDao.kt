package ly.david.data.core.image

interface ImageUrlDao {
    fun saveUrl(mbid: String, thumbnailUrl: String, largeUrl: String)
    fun getLargeUrlForEntity(mbid: String): String?
    fun getThumbnailUrlForEntity(mbid: String): String?
}
