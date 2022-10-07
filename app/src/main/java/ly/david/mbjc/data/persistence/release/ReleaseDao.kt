package ly.david.mbjc.data.persistence.release

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ly.david.mbjc.data.persistence.BaseDao

@Dao
internal abstract class ReleaseDao : BaseDao<ReleaseRoomModel> {

    // Lookup
    @Query("SELECT * FROM releases WHERE id = :releaseId")
    abstract suspend fun getRelease(releaseId: String): ReleaseRoomModel?

    @Query(
        """
            SELECT small_url
            FROM cover_arts ca
            INNER JOIN releases r
            ON ca.resource_id = r.id
            WHERE r.id = :releaseId
        """
    )
    abstract suspend fun getReleaseCoverArt(releaseId: String): String?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertReleaseCoverArt(coverArtsRoomModel: CoverArtsRoomModel): Long

}
