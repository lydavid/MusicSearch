package ly.david.mbjc.data.persistence.release

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.mbjc.data.persistence.BaseDao

@Dao
abstract class TrackDao : BaseDao<TrackRoomModel> {

    @Transaction
    @Query(
        """
        SELECT t.*
        FROM tracks t
        INNER JOIN media m ON t.medium_id = m.id
        INNER JOIN releases r ON m.release_id = r.id
        WHERE r.id = :releaseId
    """
    )
    abstract fun getTracksInRelease(releaseId: String): PagingSource<Int, TrackRoomModel>
}
