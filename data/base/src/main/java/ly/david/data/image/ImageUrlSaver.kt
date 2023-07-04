package ly.david.data.image

interface ImageUrlSaver {
    suspend fun saveUrl(mbid: String, thumbnailUrl: String, largeUrl: String)
}
