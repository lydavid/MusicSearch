package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToArtistListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.search.results.SearchResultMetadata

class SearchResultDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.search_resultQueries

    fun insert(
        entityId: String,
    ) {
        transacter.insert(
            entity_id = entityId,
        )
    }

    fun insertAll(
        entityIds: List<String>,
    ) {
        transacter.transaction {
            entityIds.forEach { entityId ->
                insert(
                    entityId = entityId,
                )
            }
        }
    }

    fun rewriteMetadata(
        entity: MusicBrainzEntity,
        query: String,
        localCount: Int,
        remoteCount: Int,
    ) {
        withTransaction {
            transacter.removeMetadata()
            transacter.setMetadata(
                entity = entity,
                query = query,
                localCount = localCount,
                remoteCount = remoteCount,
            )
        }
    }

    fun getMetadata(): SearchResultMetadata? = transacter.getMetadata(
        mapper = { entity, query, localCount, remoteCount ->
            SearchResultMetadata(
                entity = entity,
                query = query,
                localCount = localCount ?: 0,
                remoteCount = remoteCount ?: 0,
            )
        },
    ).executeAsOneOrNull()

    fun getArtistsSearchResult(): PagingSource<Int, ListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfArtistsSearchResult(),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getArtistsSearchResult(
                limit = limit,
                offset = offset,
                mapper = ::mapToArtistListItemModel,
            )
        },
    )

    fun removeAll() {
        transacter.removeAll()
    }
}
