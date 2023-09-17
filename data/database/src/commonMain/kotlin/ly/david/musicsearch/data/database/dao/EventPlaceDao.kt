package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.Dispatchers
import ly.david.musicsearch.data.database.Database
import lydavidmusicsearchdatadatabase.Event
import lydavidmusicsearchdatadatabase.Event_place

class EventPlaceDao(
    database: Database,
) {
    private val transacter = database.event_placeQueries

    fun insert(
        eventId: String,
        placeId: String,
    ) {
        transacter.insert(
            Event_place(
                event_id = eventId,
                place_id = placeId,
            )
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

    fun getNumberOfEventsByPlace(placeId: String): Int =
        transacter.getNumberOfEventsByPlace(
            placeId = placeId,
            query = "%%",
        ).executeAsOne().toInt()

    fun getEventsByPlace(
        placeId: String,
        query: String,
    ): PagingSource<Int, Event> = QueryPagingSource(
        countQuery = transacter.getNumberOfEventsByPlace(
            placeId = placeId,
            query = query,
        ),
        transacter = transacter,
        context = Dispatchers.IO,
    ) { limit, offset ->
        transacter.getEventsByPlace(
            placeId = placeId,
            query = query,
            limit = limit,
            offset = offset,
        )
    }
}
