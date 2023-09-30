package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import ly.david.data.core.CoroutineDispatchers
import ly.david.data.core.RecordingForListItem
import ly.david.data.core.release.ReleaseForListItem
import ly.david.data.core.releasegroup.ReleaseGroupForListItem
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.INSERTION_FAILED_DUE_TO_CONFLICT
import ly.david.musicsearch.data.database.mapper.mapToRecordingForListItem
import ly.david.musicsearch.data.database.mapper.mapToReleaseForListItem
import ly.david.musicsearch.data.database.mapper.mapToReleaseGroupForListItem
import lydavidmusicsearchdatadatabase.Area
import lydavidmusicsearchdatadatabase.Artist
import lydavidmusicsearchdatadatabase.Collection_entity
import lydavidmusicsearchdatadatabase.Event
import lydavidmusicsearchdatadatabase.Instrument
import lydavidmusicsearchdatadatabase.Label
import lydavidmusicsearchdatadatabase.Place
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
    ): PagingSource<Int, Area> = QueryPagingSource(
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
        )
    }

    fun getArtistsByCollection(
        collectionId: String,
        query: String,
    ): PagingSource<Int, Artist> = QueryPagingSource(
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
        )
    }

    fun getEventsByCollection(
        collectionId: String,
        query: String,
    ): PagingSource<Int, Event> = QueryPagingSource(
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
        )
    }

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
    ): PagingSource<Int, Place> = QueryPagingSource(
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
        )
    }

    fun getRecordingsByCollection(
        collectionId: String,
        query: String,
    ): PagingSource<Int, RecordingForListItem> = QueryPagingSource(
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
            mapper = ::mapToRecordingForListItem,
        )
    }

    fun getReleasesByCollection(
        collectionId: String,
        query: String,
    ): PagingSource<Int, ReleaseForListItem> = QueryPagingSource(
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
            mapper = ::mapToReleaseForListItem,
        )
    }

    fun getReleaseGroupsByCollection(
        collectionId: String,
        query: String,
        sorted: Boolean,
    ): PagingSource<Int, ReleaseGroupForListItem> = QueryPagingSource(
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
            mapper = ::mapToReleaseGroupForListItem,
        )
    }

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
