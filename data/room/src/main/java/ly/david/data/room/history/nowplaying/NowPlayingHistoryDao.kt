package ly.david.data.room.history.nowplaying

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.room.BaseDao

@Dao
abstract class NowPlayingHistoryDao : BaseDao<NowPlayingHistoryRoomModel>() {

    @Transaction
    @Query(
        """
        SELECT * FROM now_playing_history
        ORDER BY last_played DESC
        """
    )
    abstract fun getAllNowPlayingHistory(): PagingSource<Int, NowPlayingHistoryRoomModel>
}
