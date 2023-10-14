package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import ly.david.musicsearch.data.core.CoroutineDispatchers
import ly.david.musicsearch.data.core.listitem.AreaListItemModel
import ly.david.musicsearch.data.core.listitem.ArtistListItemModel
import ly.david.musicsearch.data.core.listitem.EventListItemModel
import ly.david.musicsearch.data.core.listitem.PlaceListItemModel
import ly.david.musicsearch.data.core.listitem.RecordingListItemModel
import ly.david.musicsearch.data.core.listitem.ReleaseGroupListItemModel
import ly.david.musicsearch.data.core.listitem.ReleaseListItemModel
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.INSERTION_FAILED_DUE_TO_CONFLICT
import ly.david.musicsearch.data.database.mapper.mapToAreaListItemModel
import ly.david.musicsearch.data.database.mapper.mapToArtistListItemModel
import ly.david.musicsearch.data.database.mapper.mapToEventListItemModel
import ly.david.musicsearch.data.database.mapper.mapToPlaceListItemModel
import ly.david.musicsearch.data.database.mapper.mapToRecordingListItemModel
import ly.david.musicsearch.data.database.mapper.mapToReleaseGroupListItemModel
import ly.david.musicsearch.data.database.mapper.mapToReleaseListItemModel
import lydavidmusicsearchdatadatabase.Collection_entity
import lydavidmusicsearchdatadatabase.Instrument
import lydavidmusicsearchdatadatabase.Label
import lydavidmusicsearchdatadatabase.Series
import lydavidmusicsearchdatadatabase.Work

class CollectionEntityDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.collection_entityQueries

    fun insert(
        collectionId: String,
        entityId: String,
    ): Long {
        return try {
            transacter.insertOrFail(
                Collection_entity(
                    id = collectionId,
                    entity_id = entityId,
                )
            )
            transacter.lastInsertRowId().executeAsOne()
        } catch (ex: Exception) {
            INSERTION_FAILED_DUE_TO_CONFLICT
        }
    }

    fun insertAll(
        collectionId: String,
        entityIds: List<String>,
    ) {
        transacter.transaction {
            entityIds.forEach { entityId ->
                insert(
                    collectionId = collectionId,
                    entityId = entityId,
                )
            }
        }
    }

    fun deleteAllFromCollection(collectionId: String) {
        transacter.deleteAllFromCollection(collectionId)
    }

    fun deleteFromCollection(collectionId: String, collectableId: String) {
        transacter.deleteFromCollection(
            collectionId = collectionId,
            collectableId = collectableId,
        )
    }

    fun getAreasByCollection(
        collectionId: String,
        query: String,
    ): PagingSource<Int, AreaListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfAreasByCollection(
            collectionId = collectionId,
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
    ) { limit, offset ->
        transacter.getAreasByCollection(
            collectionId = collectionId,
            query = query,
            limit = limit,
            offset = offset,
            mapper = ::mapToAreaListItemModel,
        )
    }

    fun getArtistsByCollection(
        collectionId: String,
        query: String,
    ): PagingSource<Int, ArtistListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfArtistsByCollection(
            collectionId = collectionId,
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
    ) { limit, offset ->
        transacter.getArtistsByCollection(
            collectionId = collectionId,
            query = query,
            limit = limit,
            offset = offset,
            mapper = ::mapToArtistListItemModel,
        )
    }

    fun getEventsByCollection(
        collectionId: String,
        query: String,
    ): PagingSource<Int, EventListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfEventsByCollection(
            collectionId = collectionId,
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
    ) { limit, offset ->
        transacter.getEventsByCollection(
            collectionId = collectionId,
            query = query,
            limit = limit,
            offset = offset,
            mapper = ::mapToEventListItemModel,
        )
    }

    // TODO:
    fun getInstrumentsByCollection(
        collectionId: String,
        query: String,
    ): PagingSource<Int, Instrument> = QueryPagingSource(
        countQuery = transacter.getNumberOfInstrumentsByCollection(
            collectionId = collectionId,
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
    ) { limit, offset ->
        transacter.getInstrumentsByCollection(
            collectionId = collectionId,
            query = query,
            limit = limit,
            offset = offset,
        )
    }

    // TODO:
    fun getLabelsByCollection(
        collectionId: String,
        query: String,
    ): PagingSource<Int, Label> = QueryPagingSource(
        countQuery = transacter.getNumberOfLabelsByCollection(
            collectionId = collectionId,
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
    ) { limit, offset ->
        transacter.getLabelsByCollection(
            collectionId = collectionId,
            query = query,
            limit = limit,
            offset = offset,
        )
    }

    fun getPlacesByCollection(
        collectionId: String,
        query: String,
    ): PagingSource<Int, PlaceListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfPlacesByCollection(
            collectionId = collectionId,
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
    ) { limit, offset ->
        transacter.getPlacesByCollection(
            collectionId = collectionId,
            query = query,
            limit = limit,
            offset = offset,
            mapper = ::mapToPlaceListItemModel,
        )
    }

    fun getRecordingsByCollection(
        collectionId: String,
        query: String,
    ): PagingSource<Int, RecordingListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfRecordingsByCollection(
            collectionId = collectionId,
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
    ) { limit, offset ->
        transacter.getRecordingsByCollection(
            collectionId = collectionId,
            query = query,
            limit = limit,
            offset = offset,
            mapper = ::mapToRecordingListItemModel,
        )
    }

    fun getReleasesByCollection(
        collectionId: String,
        query: String,
    ): PagingSource<Int, ReleaseListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfReleasesByCollection(
            collectionId = collectionId,
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
    ) { limit, offset ->
        transacter.getReleasesByCollection(
            collectionId = collectionId,
            query = query,
            limit = limit,
            offset = offset,
            mapper = ::mapToReleaseListItemModel,
        )
    }

    fun getReleaseGroupsByCollection(
        collectionId: String,
        query: String,
        sorted: Boolean,
    ): PagingSource<Int, ReleaseGroupListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfReleaseGroupsByCollection(
            collectionId = collectionId,
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
    ) { limit, offset ->
        transacter.getReleaseGroupsByCollection(
            collectionId = collectionId,
            query = query,
            sorted = sorted,
            limit = limit,
            offset = offset,
            mapper = ::mapToReleaseGroupListItemModel,
        )
    }

    // TODO:
    fun getSeriesByCollection(
        collectionId: String,
        query: String,
    ): PagingSource<Int, Series> = QueryPagingSource(
        countQuery = transacter.getNumberOfSeriesByCollection(
            collectionId = collectionId,
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
    ) { limit, offset ->
        transacter.getSeriesByCollection(
            collectionId = collectionId,
            query = query,
            limit = limit,
            offset = offset,
        )
    }

    // TODO:
    fun getWorksByCollection(
        collectionId: String,
        query: String,
    ): PagingSource<Int, Work> = QueryPagingSource(
        countQuery = transacter.getNumberOfWorksByCollection(
            collectionId = collectionId,
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
    ) { limit, offset ->
        transacter.getWorksByCollection(
            collectionId = collectionId,
            query = query,
            limit = limit,
            offset = offset,
        )
    }
}
