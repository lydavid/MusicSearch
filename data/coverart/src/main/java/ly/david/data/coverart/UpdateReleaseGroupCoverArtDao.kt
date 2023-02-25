package ly.david.data.coverart

interface UpdateReleaseGroupCoverArtDao {
    suspend fun setReleaseGroupCoverArtPath(releaseGroupId: String, coverArtPath: String)
}
