package ly.david.data.persistence.artist

import androidx.room.Dao
import androidx.room.Query
import ly.david.data.persistence.BaseDao

@Dao
abstract class ArtistDao : BaseDao<ArtistRoomModel>() {

    @Query("SELECT * FROM artists WHERE id = :artistId")
    abstract suspend fun getArtist(artistId: String): ArtistRoomModel?

    // TODO: use browse_resources
    @Query(
        """
        UPDATE artists
        SET release_group_count = :releaseGroupCount
        WHERE id = :artistId
        """
    )
    abstract suspend fun setReleaseGroupCount(artistId: String, releaseGroupCount: Int)
}
