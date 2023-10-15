package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.core.models.CoroutineDispatchers
import ly.david.musicsearch.core.models.listitem.EventListItemModel
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToEventListItemModel
import lydavidmusicsearchdatadatabase.Event_place

class EventPlaceDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.event_placeQueries

    fun insert(
        eventId: String,
        placeId: String,
    ) {
        transacter.insert(
            Event_place(
                event_id = eventId,
                place_id = placeId,
            ),
        )
    }

    fun insertAll(
        eventAndPlaceIds: List<Pair<String, String>>,
    ) {
        transacter.transaction {
            eventAndPlaceIds.forEach { (eventId, placeId) ->
                insert(
                    eventId = eventId,
                    placeId = placeId,
                )
            }
        }
    }

    fun deleteEventsByPlace(placeId: String) {
        transacter.deleteEventsByPlace(placeId)
    }

    fun getNumberOfEventsByPlace(placeId: String): Flow<Int> =
        transacter.getNumberOfEventsByPlace(
            placeId = placeId,
            query = "%%",
        )
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    fun getEventsByPlace(
        placeId: String,
        query: String,
    ): PagingSource<Int, EventListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfEventsByPlace(
            placeId = placeId,
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
    ) { limit, offset ->
        transacter.getEventsByPlace(
            placeId = placeId,
            query = query,
            limit = limit,
            offset = offset,
            mapper = ::mapToEventListItemModel,
        )
    }
}
