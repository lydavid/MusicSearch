package ly.david.mbjc.data.persistence

import androidx.room.Dao
import androidx.room.Query

@Dao
abstract class ArtistDao : BaseDao<RoomArtist> {
    @Query("SELECT * FROM artists WHERE id = :artistId")
    abstract suspend fun getArtist(artistId: String): RoomArtist?

    @Query(
        """
        UPDATE artists 
        SET release_group_count = :releaseGroupCount
        WHERE id = :artistId
        """
    )
    abstract suspend fun setReleaseGroupCount(artistId: String, releaseGroupCount: Int)
}
