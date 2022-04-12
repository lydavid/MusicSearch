package ly.david.mbjc.data.persistence

import androidx.room.Dao
import androidx.room.Query

@Dao
abstract class ArtistDao : BaseDao<ArtistRoomModel> {
    @Query("SELECT * FROM artists WHERE id = :artistId")
    abstract suspend fun getArtist(artistId: String): ArtistRoomModel?

    @Query(
        """
        UPDATE artists 
        SET release_group_count = :releaseGroupCount
        WHERE id = :artistId
        """
    )
    abstract suspend fun setReleaseGroupCount(artistId: String, releaseGroupCount: Int)
}
