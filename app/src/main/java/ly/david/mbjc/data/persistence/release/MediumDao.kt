package ly.david.mbjc.data.persistence.release

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.mbjc.data.persistence.BaseDao

@Dao
internal abstract class MediumDao : BaseDao<MediumRoomModel> {

    @Transaction
    @Query("""
        SELECT m.*
        FROM media m
        INNER JOIN tracks t ON t.medium_id = m.id
        WHERE t.id = :trackId
    """)
    abstract suspend fun getMediumForTrack(trackId: String): MediumRoomModel
}
