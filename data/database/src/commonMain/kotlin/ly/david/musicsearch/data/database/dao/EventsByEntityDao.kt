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
import lydavidmusicsearchdatadatabase.Events_by_entity

class EventsByEntityDao(
    database: Database,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.events_by_entityQueries

    fun insert(
        entityId: String,
        eventId: String,
    ) {
        transacter.insert(
            Events_by_entity(
                entity_id = entityId,
                event_id = eventId,
            ),
        )
    }

    fun insertAll(
        entityAndEventIds: List<Pair<String, String>>,
    ) {
        transacter.transaction {
            entityAndEventIds.forEach { (entityId, eventId) ->
                insert(
                    entityId = entityId,
                    eventId = eventId,
                )
            }
        }
    }

    fun deleteEventsByEntity(entityId: String) {
        transacter.deleteEventsByEntity(entityId)
    }

    fun getNumberOfEventsByEntity(entityId: String): Flow<Int> =
        transacter.getNumberOfEventsByEntity(
            entityId = entityId,
            query = "%%",
        )
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    fun getEventsByEntity(
        entityId: String,
        query: String,
    ): PagingSource<Int, EventListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfEventsByEntity(
            entityId = entityId,
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getEventsByEntity(
                entityId = entityId,
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToEventListItemModel,
            )
        },
    )
}
