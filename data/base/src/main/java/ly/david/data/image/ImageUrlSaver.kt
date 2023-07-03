package ly.david.data.image

interface ImageUrlSaver {
    suspend fun saveUrl(mbid: String, path: String)
}
