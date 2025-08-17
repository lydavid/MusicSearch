package ly.david.musicsearch.data.database.dao

import app.cash.paging.PagingSource
import app.cash.sqldelight.Query
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.paging3.QueryPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock
import kotlin.time.Instant
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.data.database.Database
import ly.david.musicsearch.data.database.mapper.mapToEventListItemModel
import ly.david.musicsearch.data.musicbrainz.models.core.EventMusicBrainzNetworkModel
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.details.EventDetailsModel
import ly.david.musicsearch.shared.domain.listitem.EventListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import lydavidmusicsearchdatadatabase.Event
import lydavidmusicsearchdatadatabase.Events_by_entity

class EventDao(
    database: Database,
    private val collectionEntityDao: CollectionEntityDao,
    private val coroutineDispatchers: CoroutineDispatchers,
) : EntityDao {
    override val transacter = database.eventQueries

    fun insert(event: EventMusicBrainzNetworkModel) {
        event.run {
            transacter.insertEvent(
                Event(
                    id = id,
                    name = name,
                    disambiguation = disambiguation,
                    type = type,
                    type_id = typeId,
                    time = time,
                    cancelled = cancelled,
                    begin = lifeSpan?.begin,
                    end = lifeSpan?.end,
                    ended = lifeSpan?.ended,
                ),
            )
        }
    }

    fun insertAll(events: List<EventMusicBrainzNetworkModel>) {
        transacter.transaction {
            events.forEach { event ->
                insert(event)
            }
        }
    }

    fun getEventForDetails(eventId: String): EventDetailsModel? {
        return transacter.getEventForDetails(
            eventId,
            mapper = ::toDetailsModel,
        ).executeAsOneOrNull()
    }

    private fun toDetailsModel(
        id: String,
        name: String,
        disambiguation: String?,
        type: String?,
        time: String?,
        cancelled: Boolean?,
        begin: String?,
        end: String?,
        ended: Boolean?,
        lastUpdated: Instant?,
    ) = EventDetailsModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        time = time,
        cancelled = cancelled,
        lifeSpan = LifeSpanUiModel(
            begin = begin,
            end = end,
            ended = ended,
        ),
        lastUpdated = lastUpdated ?: Clock.System.now(),
    )

    fun delete(id: String) {
        transacter.deleteEvent(id)
    }

    fun insertEventsByEntity(
        entityId: String,
        eventIds: List<String>,
    ) {
        transacter.transaction {
            eventIds.forEach { eventId ->
                transacter.insertOrIgnoreEventByEntity(
                    Events_by_entity(
                        entity_id = entityId,
                        event_id = eventId,
                    ),
                )
            }
        }
    }

    fun deleteEventLinksByEntity(entityId: String) {
        transacter.deleteEventLinksByEntity(entityId)
    }

    fun getCountOfEventsByEntity(entityId: String): Int =
        getCountOfEventsByEntityQuery(
            entityId = entityId,
            query = "",
        )
            .executeAsOne()
            .toInt()

    fun getEvents(
        browseMethod: BrowseMethod,
        query: String,
    ): PagingSource<Int, EventListItemModel> = when (browseMethod) {
        is BrowseMethod.All -> {
            getAllEvents(
                query = query,
            )
        }

        is BrowseMethod.ByEntity -> {
            if (browseMethod.entity == MusicBrainzEntityType.COLLECTION) {
                getEventsByCollection(
                    collectionId = browseMethod.entityId,
                    query = query,
                )
            } else {
                getEventsByEntity(
                    entityId = browseMethod.entityId,
                    query = query,
                )
            }
        }
    }

    fun observeCountOfEvents(browseMethod: BrowseMethod): Flow<Int> =
        when (browseMethod) {
            is BrowseMethod.ByEntity -> {
                if (browseMethod.entity == MusicBrainzEntityType.COLLECTION) {
                    collectionEntityDao.getCountOfEntitiesByCollectionQuery(
                        collectionId = browseMethod.entityId,
                    )
                } else {
                    getCountOfEventsByEntityQuery(
                        entityId = browseMethod.entityId,
                        query = "",
                    )
                }
            }

            else -> {
                getCountOfAllEvents(query = "")
            }
        }
            .asFlow()
            .mapToOne(coroutineDispatchers.io)
            .map { it.toInt() }

    private fun getCountOfAllEvents(
        query: String,
    ): Query<Long> = transacter.getCountOfAllEvents(
        query = "%$query%",
    )

    private fun getAllEvents(
        query: String,
    ): PagingSource<Int, EventListItemModel> = QueryPagingSource(
        countQuery = getCountOfAllEvents(
            query = query,
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getAllEvents(
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToEventListItemModel,
            )
        },
    )

    private fun getEventsByEntity(
        entityId: String,
        query: String,
    ): PagingSource<Int, EventListItemModel> = QueryPagingSource(
        countQuery = getCountOfEventsByEntityQuery(
            entityId = entityId,
            query = query,
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

    private fun getCountOfEventsByEntityQuery(
        entityId: String,
        query: String,
    ) = transacter.getNumberOfEventsByEntity(
        entityId = entityId,
        query = "%$query%",
    )

    private fun getEventsByCollection(
        collectionId: String,
        query: String,
    ): PagingSource<Int, EventListItemModel> = QueryPagingSource(
        countQuery = transacter.getNumberOfEventsByCollection(
            collectionId = collectionId,
            query = "%$query%",
        ),
        transacter = transacter,
        context = coroutineDispatchers.io,
        queryProvider = { limit, offset ->
            transacter.getEventsByCollection(
                collectionId = collectionId,
                query = "%$query%",
                limit = limit,
                offset = offset,
                mapper = ::mapToEventListItemModel,
            )
        },
    )
}
