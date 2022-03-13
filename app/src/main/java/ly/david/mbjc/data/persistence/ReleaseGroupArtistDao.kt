package ly.david.mbjc.data.persistence

import androidx.room.Dao
import androidx.room.Query

@Dao
abstract class ReleaseGroupArtistDao : BaseDao<RoomReleaseGroupArtistCredit> {
    @Query(
        """
        SELECT rga.*
        FROM release_groups rg
        INNER JOIN release_groups_artists rga ON rg.id = rga.release_group_id
        where rg.id = :releaseGroupId
        ORDER BY rga.`order`
    """
    )
    abstract suspend fun getReleaseGroupArtistCredits(releaseGroupId: String): List<RoomReleaseGroupArtistCredit>
}
