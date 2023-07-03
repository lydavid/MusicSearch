package ly.david.data.coverart

interface ImageUrlSaver {
    suspend fun saveUrl(mbid: String, path: String)
}
