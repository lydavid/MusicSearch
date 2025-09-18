package ly.david.musicsearch.shared.domain.history

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.listitem.ListItemModel

interface LookupHistoryRepository {
    fun upsert(
        oldId: String,
        lookupHistory: LookupHistory,
    )
    fun observeAllLookupHistory(query: String, sortOption: HistorySortOption): Flow<PagingData<ListItemModel>>
    fun markHistoryAsDeleted(mbid: String)
    fun undoDeleteHistory(mbid: String)
    fun markAllHistoryAsDeleted()
    fun undoDeleteAllHistory()
    fun deleteHistory(mbid: String)
    fun deleteAllHistory()
}
