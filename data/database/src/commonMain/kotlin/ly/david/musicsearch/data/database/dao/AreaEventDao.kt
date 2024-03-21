package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ly.david.musicsearch.core.coroutines.CoroutineDispatchers
import ly.david.musicsearch.core.models.listitem.EventListItemModel
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToEventListItemModel
import lydavidmusicsearchdatadatabase.Area_event

class AreaEventDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.area_eventQueries

    fun insert(
        areaId: String,
        eventId: String,
    ) {
        transacter.insert(
            Area_event(
                area_id = areaId,
                event_id = eventId,
            ),
        )
    }

    fun insertAll(
        areaAndEventIds: List<Pair<String, String>>,
    ) {
        transacter.transaction {
            areaAndEventIds.forEach { (areaId, eventId) ->
                insert(
                    areaId = areaId,
                    eventId = eventId,
                )
            }
        }
    }

    fun deleteEventsByArea(areaId: String) {
        transacter.deleteEventsByArea(areaId)
    }

    fun getNumberOfEventsByArea(areaId: String): Flow<Int> =
        transacter.getNumberOfEventsByArea(
            areaId = areaId,
            query = "%%",
        )
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    fun getEventsByArea(
        areaId: String,
        query: String,
    ): PagingSource<Int, EventListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfEventsByArea(
            areaId = areaId,
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getEventsByArea(
                areaId = areaId,
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToEventListItemModel,
            )
        },
    )
}
