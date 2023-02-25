package ly.david.data.coverart

interface UpdateReleaseCoverArtDao {
    suspend fun setReleaseCoverArtPath(releaseId: String, coverArtPath: String)
}
