package ly.david.data.room.release.tracks

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.room.BaseDao
import ly.david.data.room.release.tracks.MediumRoomModel

@Dao
abstract class MediumDao : BaseDao<MediumRoomModel>() {

    @Transaction
    @Query("""
        SELECT m.*
        FROM medium m
        INNER JOIN track t ON t.medium_id = m.id
        WHERE t.id = :trackId
    """)
    abstract suspend fun getMediumForTrack(trackId: String): MediumRoomModel?

    @Transaction
    @Query("""
        SELECT m.*
        FROM medium m
        INNER JOIN track t ON t.medium_id = m.id
        WHERE t.id = :trackId
    """)
    abstract suspend fun getMediaCountForRelease(trackId: String): MediumRoomModel
}
