package ly.david.data.persistence.releasegroup

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ly.david.data.persistence.BaseDao
import ly.david.data.persistence.artist.ArtistCreditNameRoomModel
import ly.david.data.persistence.artist.ArtistCreditResource
import ly.david.data.persistence.artist.ArtistCreditRoomModel

// TODO:
@Dao
abstract class ReleaseGroupArtistDao : BaseDao<ReleaseGroupArtistCreditRoomModel> {
    @Query(
        """
        SELECT rga.*
        FROM release_groups rg
        INNER JOIN release_groups_artists rga ON rg.id = rga.release_group_id
        where rg.id = :releaseGroupId
        ORDER BY rga.`order`
    """
    )
    abstract suspend fun getReleaseGroupArtistCredits(releaseGroupId: String): List<ReleaseGroupArtistCreditRoomModel>
}

/**
 * This will be implemented multiple times, but at least we don't have to copy/paste it ourselves.
 */
interface ArtistCreditInterface {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArtistCredit(artistCredit: ArtistCreditRoomModel): Long

    @Query(
        """
            SELECT *
            FROM artist_credits
            WHERE name = :name
        """
    )
    suspend fun getArtistCreditByName(name: String): ArtistCreditRoomModel

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllArtistCreditNames(artistCreditNameRoomModel: List<ArtistCreditNameRoomModel>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArtistCreditResource(artistCreditResource: ArtistCreditResource): Long
}
