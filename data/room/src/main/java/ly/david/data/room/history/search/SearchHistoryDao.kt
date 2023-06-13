package ly.david.data.room.history.search

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.network.MusicBrainzResource
import ly.david.data.room.BaseDao

@Dao
abstract class SearchHistoryDao : BaseDao<SearchHistoryRoomModel>() {

    @Transaction
    @Query(
        """
        SELECT * FROM search_history
        WHERE `query` LIKE :query AND entity = :entity
        ORDER BY last_accessed DESC
        """
    )
    abstract fun getAllSearchHistory(
        query: String = "%%",
        entity: MusicBrainzResource
    ): PagingSource<Int, SearchHistoryRoomModel>

    @Query(
        """
            DELETE FROM search_history
            WHERE `query` = :query AND entity = :entity
        """
    )
    abstract suspend fun delete(
        query: String,
        entity: MusicBrainzResource
    )

    @Query(
        """
            DELETE FROM search_history
        """
    )
    abstract suspend fun deleteAll()
}
