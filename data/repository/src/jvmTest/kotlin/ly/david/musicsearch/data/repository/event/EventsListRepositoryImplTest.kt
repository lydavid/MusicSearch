package ly.david.musicsearch.data.repository.event

import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.aimerAtBudokanEventMusicBrainzModel
import ly.david.data.test.aimerAtBudokanListItemModel
import ly.david.data.test.api.FakeBrowseApi
import ly.david.data.test.budokanPlaceMusicBrainzModel
import ly.david.data.test.canadaAreaMusicBrainzModel
import ly.david.data.test.kissArtistMusicBrainzModel
import ly.david.data.test.kissAtBudokanEventMusicBrainzModel
import ly.david.data.test.kissAtBudokanListItemModel
import ly.david.data.test.kissAtScotiabankArenaEventMusicBrainzModel
import ly.david.data.test.kissAtScotiabankArenaListItemModel
import ly.david.data.test.kitanomaruAreaMusicBrainzModel
import ly.david.data.test.tsoAtMasseyHallEventMusicBrainzModel
import ly.david.data.test.tsoAtMasseyHallListItemModel
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.data.database.dao.EventDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseEventsResponse
import ly.david.musicsearch.data.musicbrainz.models.core.EventMusicBrainzModel
import ly.david.musicsearch.data.repository.helpers.FilterTestCase
import ly.david.musicsearch.data.repository.helpers.TestEventRepository
import ly.david.musicsearch.data.repository.helpers.testFilter
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.event.EventDetailsModel
import ly.david.musicsearch.shared.domain.event.EventsListRepository
import ly.david.musicsearch.shared.domain.history.VisitedDao
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class EventsListRepositoryImplTest : KoinTest, TestEventRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    override val eventDao: EventDao by inject()
    override val entityHasRelationsDao: EntityHasRelationsDao by inject()
    override val visitedDao: VisitedDao by inject()
    override val relationDao: RelationDao by inject()
    private val collectionDao: CollectionDao by inject()
    private val browseEntityCountDao: BrowseEntityCountDao by inject()
    private val collectionEntityDao: CollectionEntityDao by inject()

    private fun createEventsListRepository(
        events: List<EventMusicBrainzModel>,
    ): EventsListRepository {
        return EventsListRepositoryImpl(
            browseEntityCountDao = browseEntityCountDao,
            collectionEntityDao = collectionEntityDao,
            eventDao = eventDao,
            browseApi = object : FakeBrowseApi() {
                override suspend fun browseEventsByEntity(
                    entityId: String,
                    entity: MusicBrainzEntity,
                    limit: Int,
                    offset: Int,
                ): BrowseEventsResponse {
                    return BrowseEventsResponse(
                        count = 1,
                        offset = 0,
                        musicBrainzModels = events,
                    )
                }
            },
        )
    }

    @Test
    fun setupEventsByCollection() = runTest {
        val collectionId = "950cea33-433e-497f-93bb-a05a393a2c02"
        val events = listOf(
            kissAtScotiabankArenaEventMusicBrainzModel,
            aimerAtBudokanEventMusicBrainzModel,
        )
        val eventsListRepository = createEventsListRepository(
            events = events,
        )

        collectionDao.insertLocal(
            collection = CollectionListItemModel(
                id = collectionId,
                isRemote = false,
                name = "events",
                entity = MusicBrainzEntity.EVENT,
            ),
        )
        collectionEntityDao.insertAll(
            collectionId = collectionId,
            entityIds = events.map { it.id },
        )

        testFilter(
            pagingFlowProducer = { query ->
                eventsListRepository.observeEvents(
                    browseMethod = BrowseMethod.ByEntity(
                        entityId = collectionId,
                        entity = MusicBrainzEntity.COLLECTION,
                    ),
                    listFilters = ListFilters(
                        query = query,
                    ),
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "No filter",
                    query = "",
                    expectedResult = listOf(
                        aimerAtBudokanListItemModel,
                        kissAtScotiabankArenaListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by name",
                    query = "kis",
                    expectedResult = listOf(
                        kissAtScotiabankArenaListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by type",
                    query = "con",
                    expectedResult = listOf(
                        aimerAtBudokanListItemModel,
                        kissAtScotiabankArenaListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by begin date",
                    query = "2017",
                    expectedResult = listOf(
                        aimerAtBudokanListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by end date",
                    query = "2019",
                    expectedResult = listOf(
                        kissAtScotiabankArenaListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by time",
                    query = "18:0",
                    expectedResult = listOf(
                        aimerAtBudokanListItemModel,
                    ),
                ),
            ),
        )
    }

    @Test
    fun setUpEventsByCountryArea() = runTest {
        val entityId = canadaAreaMusicBrainzModel.id
        val entity = MusicBrainzEntity.AREA
        val events = listOf(
            tsoAtMasseyHallEventMusicBrainzModel,
            kissAtScotiabankArenaEventMusicBrainzModel,
        )
        val eventsListRepository = createEventsListRepository(
            events = events,
        )

        val browseMethod = BrowseMethod.ByEntity(
            entityId = entityId,
            entity = entity,
        )

        testFilter(
            pagingFlowProducer = { query ->
                eventsListRepository.observeEvents(
                    browseMethod = browseMethod,
                    listFilters = ListFilters(
                        query = query,
                    ),
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "no filter",
                    query = "",
                    expectedResult = listOf(
                        tsoAtMasseyHallListItemModel,
                        kissAtScotiabankArenaListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by name",
                    query = "tor",
                    expectedResult = listOf(
                        tsoAtMasseyHallListItemModel,
                    ),
                ),
            ),
        )
    }

    @Test
    fun setupEventsByPlace() = runTest {
        val entityId = budokanPlaceMusicBrainzModel.id
        val entity = MusicBrainzEntity.PLACE
        val events = listOf(
            kissAtBudokanEventMusicBrainzModel,
            aimerAtBudokanEventMusicBrainzModel,
        )
        val eventsListRepository = createEventsListRepository(
            events = events,
        )
        val browseMethod = BrowseMethod.ByEntity(
            entityId = entityId,
            entity = entity,
        )

        testFilter(
            pagingFlowProducer = { query ->
                eventsListRepository.observeEvents(
                    browseMethod = browseMethod,
                    listFilters = ListFilters(
                        query = query,
                    ),
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "no filter",
                    query = "",
                    expectedResult = listOf(
                        kissAtBudokanListItemModel,
                        aimerAtBudokanListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by name",
                    query = "ai",
                    expectedResult = listOf(
                        aimerAtBudokanListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by type",
                    query = "cert",
                    expectedResult = listOf(
                        kissAtBudokanListItemModel,
                        aimerAtBudokanListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by begin date",
                    query = "29",
                    expectedResult = listOf(
                        aimerAtBudokanListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by end date",
                    query = "77",
                    expectedResult = listOf(
                        kissAtBudokanListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by time",
                    query = ":00",
                    expectedResult = listOf(
                        aimerAtBudokanListItemModel,
                    ),
                ),
            ),
        )
    }

    @Test
    fun setupEventsByArea() = runTest {
        val entityId = kitanomaruAreaMusicBrainzModel.id
        val entity = MusicBrainzEntity.AREA
        val events = listOf(
            kissAtBudokanEventMusicBrainzModel,
            aimerAtBudokanEventMusicBrainzModel,
        )
        val eventsListRepository = createEventsListRepository(
            events = events,
        )
        val browseMethod = BrowseMethod.ByEntity(
            entityId = entityId,
            entity = entity,
        )

        testFilter(
            pagingFlowProducer = { query ->
                eventsListRepository.observeEvents(
                    browseMethod = browseMethod,
                    listFilters = ListFilters(
                        query = query,
                    ),
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "no filter",
                    query = "",
                    expectedResult = listOf(
                        kissAtBudokanListItemModel,
                        aimerAtBudokanListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by name",
                    query = "ai",
                    expectedResult = listOf(
                        aimerAtBudokanListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by type",
                    query = "cert",
                    expectedResult = listOf(
                        kissAtBudokanListItemModel,
                        aimerAtBudokanListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by begin date",
                    query = "29",
                    expectedResult = listOf(
                        aimerAtBudokanListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by end date",
                    query = "77",
                    expectedResult = listOf(
                        kissAtBudokanListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by time",
                    query = ":00",
                    expectedResult = listOf(
                        aimerAtBudokanListItemModel,
                    ),
                ),
            ),
        )
    }

    @Test
    fun setupEventsByArtist() = runTest {
        val entityId = kissArtistMusicBrainzModel.id
        val entity = MusicBrainzEntity.ARTIST
        val events = listOf(
            kissAtBudokanEventMusicBrainzModel,
            kissAtScotiabankArenaEventMusicBrainzModel,
        )
        val eventsListRepository = createEventsListRepository(
            events = events,
        )
        val browseMethod = BrowseMethod.ByEntity(
            entityId = entityId,
            entity = entity,
        )

        testFilter(
            pagingFlowProducer = { query ->
                eventsListRepository.observeEvents(
                    browseMethod = browseMethod,
                    listFilters = ListFilters(
                        query = query,
                    ),
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "no filter",
                    query = "",
                    expectedResult = listOf(
                        kissAtBudokanListItemModel,
                        kissAtScotiabankArenaListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by name",
                    query = "budo",
                    expectedResult = listOf(
                        kissAtBudokanListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by type",
                    query = "Con",
                    expectedResult = listOf(
                        kissAtBudokanListItemModel,
                        kissAtScotiabankArenaListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by begin date",
                    query = "20",
                    expectedResult = listOf(
                        kissAtScotiabankArenaListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by end date",
                    query = "04",
                    expectedResult = listOf(
                        kissAtBudokanListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by time",
                    query = "8:3",
                    expectedResult = listOf(
                        kissAtScotiabankArenaListItemModel,
                    ),
                ),
            ),
        )
    }

    private fun setUpEventsExceptCollection() = runTest {
        setupEventsByArea()
        setUpEventsByCountryArea()
        setupEventsByArtist()
        setupEventsByPlace()
    }

    @Test
    fun `events by entity (area, artist, place)`() = runTest {
        setUpEventsExceptCollection()
    }

    @Test
    fun `all events`() = runTest {
        setUpEventsExceptCollection()
        setupEventsByCollection()

        val eventsListRepository = createEventsListRepository(
            events = listOf(),
        )
        testFilter(
            pagingFlowProducer = { query ->
                eventsListRepository.observeEvents(
                    browseMethod = BrowseMethod.All,
                    listFilters = ListFilters(
                        query = query,
                    ),
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "No filter",
                    query = "",
                    expectedResult = listOf(
                        kissAtBudokanListItemModel,
                        tsoAtMasseyHallListItemModel,
                        aimerAtBudokanListItemModel,
                        kissAtScotiabankArenaListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by name",
                    query = "ai",
                    expectedResult = listOf(
                        aimerAtBudokanListItemModel,
                    ),
                ),
            ),
        )
    }

    @Test
    fun `refreshing events that belong to multiple entities does not delete the event`() = runTest {
        setupEventsByPlace()
        setupEventsByArea()

        val modifiedEvents = listOf(
            kissAtBudokanEventMusicBrainzModel.copy(
                id = "new-id-is-considered-a-different-event",
            ),
            aimerAtBudokanEventMusicBrainzModel.copy(
                disambiguation = "changes will be ignored if event is linked to multiple entities",
            ),
        )
        val eventsListRepository = createEventsListRepository(
            events = modifiedEvents,
        )

        // refresh
        eventsListRepository.observeEvents(
            browseMethod = BrowseMethod.ByEntity(
                entityId = kitanomaruAreaMusicBrainzModel.id,
                entity = MusicBrainzEntity.AREA,
            ),
            listFilters = ListFilters(),
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                listOf(
                    kissAtBudokanListItemModel.copy(
                        id = "new-id-is-considered-a-different-event",
                    ),
                    aimerAtBudokanListItemModel,
                ),
                this,
            )
        }

        // other entities remain unchanged
        eventsListRepository.observeEvents(
            browseMethod = BrowseMethod.ByEntity(
                entityId = budokanPlaceMusicBrainzModel.id,
                entity = MusicBrainzEntity.PLACE,
            ),
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    kissAtBudokanListItemModel,
                    aimerAtBudokanListItemModel,
                ),
                this,
            )
        }

        // both old and new version of event exists
        eventsListRepository.observeEvents(
            browseMethod = BrowseMethod.All,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    kissAtBudokanListItemModel,
                    kissAtBudokanListItemModel.copy(
                        id = "new-id-is-considered-a-different-event",
                    ),
                    aimerAtBudokanListItemModel,
                ),
                this,
            )
        }

        eventsListRepository.observeEvents(
            browseMethod = BrowseMethod.ByEntity(
                entityId = budokanPlaceMusicBrainzModel.id,
                entity = MusicBrainzEntity.PLACE,
            ),
            listFilters = ListFilters(),
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                listOf(
                    kissAtBudokanListItemModel.copy(
                        id = "new-id-is-considered-a-different-event",
                    ),
                    aimerAtBudokanListItemModel,
                ),
                this,
            )
        }

        // now only new version of event exists
        // however, the other event is never updated unless we go into it and refresh
        eventsListRepository.observeEvents(
            browseMethod = BrowseMethod.All,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    kissAtBudokanListItemModel.copy(
                        id = "new-id-is-considered-a-different-event",
                    ),
                    aimerAtBudokanListItemModel,
                ),
                this,
            )
        }

        // now visit the event and refresh it
        val eventRepository = createEventRepository(
            aimerAtBudokanEventMusicBrainzModel.copy(
                disambiguation = "changes will be ignored if event is linked to multiple entities",
            ),
        )
        eventRepository.lookupEvent(
            eventId = aimerAtBudokanEventMusicBrainzModel.id,
            forceRefresh = false,
        ).let { eventDetailsModel ->
            assertEquals(
                EventDetailsModel(
                    id = "34f8a930-beb2-441b-b0d7-03c84f92f1ea",
                    name = "Aimer Live in 武道館 ”blanc et noir\"",
                    type = "Concert",
                    lifeSpan = LifeSpanUiModel(
                        begin = "2017-08-29",
                        end = "2017-08-29",
                        ended = true,
                    ),
                    cancelled = false,
                    time = "18:00",
                ),
                eventDetailsModel,
            )
        }
        eventRepository.lookupEvent(
            eventId = aimerAtBudokanEventMusicBrainzModel.id,
            forceRefresh = true,
        ).let { eventDetailsModel ->
            assertEquals(
                EventDetailsModel(
                    id = "34f8a930-beb2-441b-b0d7-03c84f92f1ea",
                    name = "Aimer Live in 武道館 ”blanc et noir\"",
                    disambiguation = "changes will be ignored if event is linked to multiple entities",
                    type = "Concert",
                    lifeSpan = LifeSpanUiModel(
                        begin = "2017-08-29",
                        end = "2017-08-29",
                        ended = true,
                    ),
                    cancelled = false,
                    time = "18:00",
                ),
                eventDetailsModel,
            )
        }
    }
}
