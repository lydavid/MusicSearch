package ly.david.musicsearch.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.insertSeparators
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.core.history.SearchHistory
import ly.david.musicsearch.data.core.listitem.Header
import ly.david.musicsearch.data.core.listitem.ListItemModel
import ly.david.musicsearch.data.core.listitem.SearchHistoryListItemModel
import ly.david.musicsearch.data.core.listitem.toSearchHistoryListItemModel
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.dao.SearchHistoryDao
import ly.david.musicsearch.data.repository.internal.paging.CommonPagingConfig
import ly.david.musicsearch.domain.search.history.SearchHistoryRepository

class SearchHistoryRepositoryImpl(
    private val searchHistoryDao: SearchHistoryDao,
) : SearchHistoryRepository {
    override fun observeSearchHistory(entity: MusicBrainzEntity): Flow<PagingData<ListItemModel>> =
        Pager(
            config = CommonPagingConfig.pagingConfig,
            pagingSourceFactory = {
                searchHistoryDao.getAllSearchHistory(
                    entity = entity,
                )
            },
        ).flow.map { pagingData ->
            pagingData
                .map(SearchHistory::toSearchHistoryListItemModel)
                .insertSeparators { before: SearchHistoryListItemModel?, after: SearchHistoryListItemModel? ->
                    if (before == null) Header() else null
                }
        }

    override fun recordSearchHistory(entity: MusicBrainzEntity, query: String) {
        searchHistoryDao.upsert(
            SearchHistory(
                entity = entity,
                query = query,
            ),
        )
    }

    override fun deleteAll(entity: MusicBrainzEntity) {
        searchHistoryDao.deleteAll(entity)
    }

    override fun delete(entity: MusicBrainzEntity, query: String) {
        searchHistoryDao.delete(entity, query)
    }
}
