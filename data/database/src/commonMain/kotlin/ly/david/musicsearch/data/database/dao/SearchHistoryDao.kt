package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.history.SearchHistory
import ly.david.musicsearch.shared.domain.listitem.SearchHistoryListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.data.database.Database

class SearchHistoryDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.search_historyQueries

    fun upsert(searchHistory: SearchHistory) {
        searchHistory.run {
            transacter.upsert(
                entity = entity,
                query = query,
                lastAccessed = lastAccessed,
            )
        }
    }

    fun getAllSearchHistory(
        entity: MusicBrainzEntityType,
    ): PagingSource<Int, SearchHistoryListItemModel> = QueryPagingSource(
        countQuery = transacter.getAllSearchHistoryCount(
            entity = entity,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getAllSearchHistory(
                entity = entity,
                limit = limit,
                offset = offset,
                mapper = { entity, query ->
                    SearchHistoryListItemModel(
                        id = "${query}_$entity",
                        query = query,
                        entity = entity,
                    )
                },
            )
        },
    )

    fun delete(
        entity: MusicBrainzEntityType,
        query: String,
    ) {
        transacter.delete(
            entity = entity,
            query = query,
        )
    }

    fun deleteAll(
        entity: MusicBrainzEntityType,
    ) {
        transacter.deleteAll(
            entity = entity,
        )
    }
}
