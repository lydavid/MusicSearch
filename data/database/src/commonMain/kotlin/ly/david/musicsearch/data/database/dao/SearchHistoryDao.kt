package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.core.models.history.SearchHistory
import ly.david.musicsearch.core.models.listitem.SearchHistoryListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
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
        entity: MusicBrainzEntity,
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
        entity: MusicBrainzEntity,
        query: String,
    ) {
        transacter.delete(
            entity = entity,
            query = query,
        )
    }

    fun deleteAll(
        entity: MusicBrainzEntity,
    ) {
        transacter.deleteAll(
            entity = entity,
        )
    }
}
