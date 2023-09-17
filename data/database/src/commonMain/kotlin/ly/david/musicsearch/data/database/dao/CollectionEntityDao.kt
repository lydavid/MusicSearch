package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.Dispatchers
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Area
import lydavidmusicsearchdatadatabase.Artist
import lydavidmusicsearchdatadatabase.Collection_entity
import lydavidmusicsearchdatadatabase.Event
import lydavidmusicsearchdatadatabase.Instrument
import lydavidmusicsearchdatadatabase.Place

class CollectionEntityDao(
    database: Database,
) {
    private val transacter = database.collection_entityQueries

    fun insert(
        collectionId: String,
        entityId: String,
    ) {
        transacter.insert(
            Collection_entity(
                id = collectionId,
                entity_id = entityId,
            )
        )
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
        context = Dispatchers.IO,
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
        context = Dispatchers.IO,
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
        context = Dispatchers.IO,
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
        context = Dispatchers.IO,
    ) { limit, offset ->
        transacter.getInstrumentsByCollection(
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
        context = Dispatchers.IO,
    ) { limit, offset ->
        transacter.getPlacesByCollection(
            collectionId = collectionId,
            query = query,
            limit = limit,
            offset = offset,
        )
    }
}
