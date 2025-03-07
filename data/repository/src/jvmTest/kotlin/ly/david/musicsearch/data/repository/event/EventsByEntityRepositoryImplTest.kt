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
import ly.david.musicsearch.data.repository.helpers.TestEventRepository
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.event.EventDetailsModel
import ly.david.musicsearch.shared.domain.event.EventsByEntityRepository
import ly.david.musicsearch.shared.domain.history.VisitedDao
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.listitem.EventListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class EventsByEntityRepositoryImplTest : KoinTest, TestEventRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    override val eventDao: EventDao by inject()
    override val entityHasRelationsDao: EntityHasRelationsDao by inject()
    override val visitedDao: VisitedDao by inject()
    override val relationDao: RelationDao by inject()
    private val collectionDao: CollectionDao by inject()
    private val browseEntityCountDao: BrowseEntityCountDao by inject()
    private val collectionEntityDao: CollectionEntityDao by inject()

    private fun createRepository(
        events: List<EventMusicBrainzModel>,
    ): EventsByEntityRepository {
        return EventsByEntityRepositoryImpl(
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

    private fun EventsByEntityRepository.testFilter(
        entityId: String,
        entity: MusicBrainzEntity?,
        query: String,
        expectedResult: List<EventListItemModel>,
    ) = runTest {
        observeEventsByEntity(
            entityId = entityId,
            entity = entity,
            listFilters = ListFilters(
                query = query,
            ),
        ).asSnapshot().run {
            assertEquals(
                expectedResult,
                this,
            )
        }
    }

    @Test
    fun setupEventsByCollection() = runTest {
        val collectionId = "950cea33-433e-497f-93bb-a05a393a2c02"
        val events = listOf(
            kissAtScotiabankArenaEventMusicBrainzModel,
            aimerAtBudokanEventMusicBrainzModel,
        )
        val sut = createRepository(
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

        sut.observeEventsByEntity(
            entityId = collectionId,
            entity = MusicBrainzEntity.COLLECTION,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    aimerAtBudokanListItemModel,
                    kissAtScotiabankArenaListItemModel,
                ),
                this,
            )
        }

        // filter by name
        sut.testFilter(
            entityId = collectionId,
            entity = MusicBrainzEntity.COLLECTION,
            query = "kis",
            expectedResult = listOf(
                kissAtScotiabankArenaListItemModel,
            ),
        )

        // filter by type
        sut.testFilter(
            entityId = collectionId,
            entity = MusicBrainzEntity.COLLECTION,
            query = "con",
            expectedResult = listOf(
                aimerAtBudokanListItemModel,
                kissAtScotiabankArenaListItemModel,
            ),
        )

        // filter by begin date
        sut.testFilter(
            entityId = collectionId,
            entity = MusicBrainzEntity.COLLECTION,
            query = "2017",
            expectedResult = listOf(
                aimerAtBudokanListItemModel,
            ),
        )

        // filter by end date
        sut.testFilter(
            entityId = collectionId,
            entity = MusicBrainzEntity.COLLECTION,
            query = "2019",
            expectedResult = listOf(
                kissAtScotiabankArenaListItemModel,
            ),
        )

        // filter by time
        sut.testFilter(
            entityId = collectionId,
            entity = MusicBrainzEntity.COLLECTION,
            query = "18:0",
            expectedResult = listOf(
                aimerAtBudokanListItemModel,
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
        val sut = createRepository(
            events = events,
        )

        sut.testFilter(
            entityId = entityId,
            entity = entity,
            query = "",
            expectedResult = listOf(
                tsoAtMasseyHallListItemModel,
                kissAtScotiabankArenaListItemModel,
            ),
        )

        // filter by name
        sut.testFilter(
            entityId = entityId,
            entity = MusicBrainzEntity.AREA,
            query = "tor",
            expectedResult = listOf(
                tsoAtMasseyHallListItemModel,
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
        val sut = createRepository(
            events = events,
        )

        sut.testFilter(
            entityId = entityId,
            entity = entity,
            query = "",
            expectedResult = listOf(
                kissAtBudokanListItemModel,
                aimerAtBudokanListItemModel,
            ),
        )

        // filter by name
        sut.testFilter(
            entityId = entityId,
            entity = entity,
            query = "ai",
            expectedResult = listOf(
                aimerAtBudokanListItemModel,
            ),
        )

        // filter by type
        sut.testFilter(
            entityId = entityId,
            entity = entity,
            query = "cert",
            expectedResult = listOf(
                kissAtBudokanListItemModel,
                aimerAtBudokanListItemModel,
            ),
        )

        // filter by begin date
        sut.testFilter(
            entityId = entityId,
            entity = entity,
            query = "29",
            expectedResult = listOf(
                aimerAtBudokanListItemModel,

            ),
        )

        // filter by end date
        sut.testFilter(
            entityId = entityId,
            entity = entity,
            query = "77",
            expectedResult = listOf(
                kissAtBudokanListItemModel,
            ),
        )

        // filter by time
        sut.testFilter(
            entityId = entityId,
            entity = entity,
            query = ":00",
            expectedResult = listOf(
                aimerAtBudokanListItemModel,
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
        val sut = createRepository(
            events = events,
        )

        sut.testFilter(
            entityId = entityId,
            entity = entity,
            query = "",
            expectedResult = listOf(
                kissAtBudokanListItemModel,
                aimerAtBudokanListItemModel,
            ),
        )

        // filter by name
        sut.testFilter(
            entityId = entityId,
            entity = entity,
            query = "ai",
            expectedResult = listOf(
                aimerAtBudokanListItemModel,
            ),
        )

        // filter by type
        sut.testFilter(
            entityId = entityId,
            entity = entity,
            query = "cert",
            expectedResult = listOf(
                kissAtBudokanListItemModel,
                aimerAtBudokanListItemModel,
            ),
        )

        // filter by begin date
        sut.testFilter(
            entityId = entityId,
            entity = entity,
            query = "29",
            expectedResult = listOf(
                aimerAtBudokanListItemModel,

            ),
        )

        // filter by end date
        sut.testFilter(
            entityId = entityId,
            entity = entity,
            query = "77",
            expectedResult = listOf(
                kissAtBudokanListItemModel,
            ),
        )

        // filter by time
        sut.testFilter(
            entityId = entityId,
            entity = entity,
            query = ":00",
            expectedResult = listOf(
                aimerAtBudokanListItemModel,
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
        val sut = createRepository(
            events = events,
        )

        sut.testFilter(
            entityId = entityId,
            entity = entity,
            query = "",
            expectedResult = listOf(
                kissAtBudokanListItemModel,
                kissAtScotiabankArenaListItemModel,
            ),
        )

        // filter by name
        sut.testFilter(
            entityId = entityId,
            entity = entity,
            query = "budo",
            expectedResult = listOf(
                kissAtBudokanListItemModel,
            ),
        )

        // filter by type
        sut.testFilter(
            entityId = entityId,
            entity = entity,
            query = "Con",
            expectedResult = listOf(
                kissAtBudokanListItemModel,
                kissAtScotiabankArenaListItemModel,
            ),
        )

        // filter by begin date
        sut.testFilter(
            entityId = entityId,
            entity = entity,
            query = "20",
            expectedResult = listOf(
                kissAtScotiabankArenaListItemModel,
            ),
        )

        // filter by end date
        sut.testFilter(
            entityId = entityId,
            entity = entity,
            query = "04",
            expectedResult = listOf(
                kissAtBudokanListItemModel,
            ),
        )

        // filter by time
        sut.testFilter(
            entityId = entityId,
            entity = entity,
            query = "8:3",
            expectedResult = listOf(
                kissAtScotiabankArenaListItemModel,
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

        val sut = createRepository(
            events = listOf(),
        )
        sut.observeEventsByEntity(
            entityId = null,
            entity = null,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                4,
                size,
            )
            assertEquals(
                listOf(
                    kissAtBudokanListItemModel,
                    tsoAtMasseyHallListItemModel,
                    aimerAtBudokanListItemModel,
                    kissAtScotiabankArenaListItemModel,
                ),
                this,
            )
        }
        sut.observeEventsByEntity(
            entityId = null,
            entity = null,
            listFilters = ListFilters(
                query = "ai",
            ),
        ).asSnapshot().run {
            assertEquals(
                1,
                size,
            )
            assertEquals(
                listOf(
                    aimerAtBudokanListItemModel,
                ),
                this,
            )
        }
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
        val sut = createRepository(
            events = modifiedEvents,
        )

        // refresh
        sut.observeEventsByEntity(
            entityId = kitanomaruAreaMusicBrainzModel.id,
            entity = MusicBrainzEntity.AREA,
            listFilters = ListFilters(),
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                2,
                size,
            )
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
        sut.observeEventsByEntity(
            entityId = budokanPlaceMusicBrainzModel.id,
            entity = MusicBrainzEntity.PLACE,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                2,
                size,
            )
            assertEquals(
                listOf(
                    kissAtBudokanListItemModel,
                    aimerAtBudokanListItemModel,
                ),
                this,
            )
        }

        // both old and new version of event exists
        sut.observeEventsByEntity(
            entityId = null,
            entity = null,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                3,
                size,
            )
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

        sut.observeEventsByEntity(
            entityId = budokanPlaceMusicBrainzModel.id,
            entity = MusicBrainzEntity.PLACE,
            listFilters = ListFilters(),
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                2,
                size,
            )
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
        sut.observeEventsByEntity(
            entityId = null,
            entity = null,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                2,
                size,
            )
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
