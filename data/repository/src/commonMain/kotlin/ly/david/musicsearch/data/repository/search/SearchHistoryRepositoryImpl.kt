package ly.david.musicsearch.data.repository.search

import app.cash.paging.Pager
import app.cash.paging.PagingData
import app.cash.paging.insertSeparators
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.data.database.dao.SearchHistoryDao
import ly.david.musicsearch.data.repository.internal.paging.CommonPagingConfig
import ly.david.musicsearch.shared.domain.history.SearchHistory
import ly.david.musicsearch.shared.domain.listitem.Header
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.SearchHistoryListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.search.history.SearchHistoryRepository

internal class SearchHistoryRepositoryImpl(
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
                .insertSeparators { before: SearchHistoryListItemModel?, _: SearchHistoryListItemModel? ->
                    if (before == null) Header else null
                }
        }

    override fun recordSearchHistory(
        entity: MusicBrainzEntity,
        query: String,
    ) {
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

    override fun delete(
        entity: MusicBrainzEntity,
        query: String,
    ) {
        searchHistoryDao.delete(
            entity,
            query,
        )
    }
}
