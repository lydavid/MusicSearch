package ly.david.data.room.history.search

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.network.MusicBrainzEntity
import ly.david.data.room.BaseDao

@Dao
abstract class SearchHistoryDao : BaseDao<SearchHistoryRoomModel>() {

    @Transaction
    @Query(
        """
        SELECT * FROM search_history
        WHERE entity = :entity
        ORDER BY last_accessed DESC
        """
    )
    abstract fun getAllSearchHistory(
        entity: MusicBrainzEntity
    ): PagingSource<Int, SearchHistoryRoomModel>

    @Query(
        """
            DELETE FROM search_history
            WHERE `query` = :query AND entity = :entity
        """
    )
    abstract suspend fun delete(
        query: String,
        entity: MusicBrainzEntity
    )

    @Query(
        """
            DELETE FROM search_history
            WHERE entity = :entity
        """
    )
    abstract suspend fun deleteAll(entity: MusicBrainzEntity)
}
