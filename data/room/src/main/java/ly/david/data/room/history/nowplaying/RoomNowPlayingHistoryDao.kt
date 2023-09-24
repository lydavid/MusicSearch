package ly.david.data.room.history.nowplaying

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.room.BaseDao

@Dao
abstract class RoomNowPlayingHistoryDao : BaseDao<NowPlayingHistoryRoomModel>() {

    @Transaction
    @Query(
        """
        SELECT * FROM now_playing_history
        WHERE raw LIKE :query
        ORDER BY last_played DESC
        """
    )
    abstract fun getAllNowPlayingHistory(query: String = "%%"): PagingSource<Int, NowPlayingHistoryRoomModel>

    @Query(
        """
          DELETE FROM now_playing_history 
          WHERE raw = :raw
    """
    )
    abstract suspend fun delete(raw: String)
}
