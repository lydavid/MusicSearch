package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.INSERTION_FAILED_DUE_TO_CONFLICT
import ly.david.musicsearch.data.database.mapper.mapToGenreListItemModel
import ly.david.musicsearch.data.database.mapper.mapToInstrumentListItemModel
import ly.david.musicsearch.data.database.mapper.mapToSeriesListItemModel
import ly.david.musicsearch.shared.domain.listitem.GenreListItemModel
import ly.david.musicsearch.shared.domain.listitem.InstrumentListItemModel
import ly.david.musicsearch.shared.domain.listitem.SeriesListItemModel
import lydavidmusicsearchdatadatabase.Collection_entity

class CollectionEntityDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.collection_entityQueries

    @Suppress("SwallowedException")
    fun insert(
        collectionId: String,
        entityId: String,
    ): Long {
        return try {
            transacter.insertOrFail(
                Collection_entity(
                    id = collectionId,
                    entity_id = entityId,
                ),
            )
            1
        } catch (ex: Exception) {
            INSERTION_FAILED_DUE_TO_CONFLICT
        }
    }

    fun insertAll(
        collectionId: String,
        entityIds: List<String>,
    ): Int {
        return transacter.transactionWithResult {
            entityIds.count { entityId ->
                insert(
                    collectionId = collectionId,
                    entityId = entityId,
                ) != INSERTION_FAILED_DUE_TO_CONFLICT
            }
        }
    }

    fun deleteAllFromCollection(collectionId: String) {
        transacter.deleteAllFromCollection(collectionId)
    }

    fun deleteFromCollection(
        collectionId: String,
        collectableId: String,
    ) {
        transacter.deleteFromCollection(
            collectionId = collectionId,
            collectableId = collectableId,
        )
    }

    fun deleteCollection(
        collectionId: String,
    ) {
        transacter.deleteCollection(collectionId)
    }

    fun getGenresByCollection(
        collectionId: String,
        query: String,
    ): PagingSource<Int, GenreListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfGenresByCollection(
            collectionId = collectionId,
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getGenresByCollection(
                collectionId = collectionId,
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToGenreListItemModel,
            )
        },
    )

    fun getInstrumentsByCollection(
        collectionId: String,
        query: String,
    ): PagingSource<Int, InstrumentListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfInstrumentsByCollection(
            collectionId = collectionId,
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getInstrumentsByCollection(
                collectionId = collectionId,
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToInstrumentListItemModel,
            )
        },
    )

    fun getSeriesByCollection(
        collectionId: String,
        query: String,
    ): PagingSource<Int, SeriesListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfSeriesByCollection(
            collectionId = collectionId,
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getSeriesByCollection(
                collectionId = collectionId,
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToSeriesListItemModel,
            )
        },
    )

    fun getCountOfEntitiesByCollection(collectionId: String): Int =
        transacter.getCountOfEntitiesByCollection(
            collectionId = collectionId,
        )
            .executeAsOne()
            .toInt()
}
