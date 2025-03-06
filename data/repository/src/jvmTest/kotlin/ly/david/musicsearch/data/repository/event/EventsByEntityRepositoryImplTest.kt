package ly.david.musicsearch.data.repository.event

import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.aimerAtBudokanEventMusicBrainzModel
import ly.david.data.test.aimerAtBudokanListItemModel
import ly.david.data.test.api.FakeBrowseApi
import ly.david.data.test.budokanPlaceMusicBrainzModel
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

    private fun setUpCollection() = runTest {
        val collectionId = "950cea33-433e-497f-93bb-a05a393a2c02"
        val events = listOf(
            kissAtScotiabankArenaEventMusicBrainzModel,
            kissAtBudokanEventMusicBrainzModel,
        )
        val sut = createRepository(
            events = events,
        )

        collectionDao.insertLocal(
            collection = CollectionListItemModel(
                id = collectionId,
                isRemote = false,
                name = "KISS concerts",
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
                2,
                size,
            )
            assertEquals(
                listOf(
                    kissAtBudokanListItemModel,
                    kissAtScotiabankArenaListItemModel,
                ),
                this,
            )
        }

        sut.observeEventsByEntity(
            entityId = collectionId,
            entity = MusicBrainzEntity.COLLECTION,
            listFilters = ListFilters(
                query = "are",
            ),
        ).asSnapshot().run {
            assertEquals(
                1,
                size,
            )
            assertEquals(
                listOf(
                    kissAtScotiabankArenaListItemModel,
                ),
                this,
            )
        }
    }

    @Test
    fun `events by collection, filter by name`() = runTest {
        setUpCollection()
    }

    private fun setUpCanadianEvents() = runTest {
        val entityId = "71bbafaa-e825-3e15-8ca9-017dcad1748b"
        val events = listOf(
            tsoAtMasseyHallEventMusicBrainzModel,
            kissAtScotiabankArenaEventMusicBrainzModel,
        )
        val sut = createRepository(
            events = events,
        )
        sut.observeEventsByEntity(
            entityId = entityId,
            entity = MusicBrainzEntity.AREA,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                2,
                size,
            )
            assertEquals(
                listOf(
                    tsoAtMasseyHallListItemModel,
                    kissAtScotiabankArenaListItemModel,
                ),
                this,
            )
        }
        sut.observeEventsByEntity(
            entityId = entityId,
            entity = MusicBrainzEntity.AREA,
            listFilters = ListFilters(
                query = "tor",
            ),
        ).asSnapshot().run {
            assertEquals(
                1,
                size,
            )
            assertEquals(
                listOf(
                    tsoAtMasseyHallListItemModel,
                ),
                this,
            )
        }
    }

    private fun setUpBudokanEvents() = runTest {
        val entityId = "4d43b9d8-162d-4ac5-8068-dfb009722484"
        val events = listOf(
            kissAtBudokanEventMusicBrainzModel,
            aimerAtBudokanEventMusicBrainzModel,
        )
        val sut = createRepository(
            events = events,
        )
        sut.observeEventsByEntity(
            entityId = entityId,
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
        sut.observeEventsByEntity(
            entityId = entityId,
            entity = MusicBrainzEntity.PLACE,
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

    private fun setupKitanomaruEvents() = runTest {
        val entityId = kitanomaruAreaMusicBrainzModel.id
        val events = listOf(
            kissAtBudokanEventMusicBrainzModel,
            aimerAtBudokanEventMusicBrainzModel,
        )
        val sut = createRepository(
            events = events,
        )
        sut.observeEventsByEntity(
            entityId = entityId,
            entity = MusicBrainzEntity.AREA,
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
        sut.observeEventsByEntity(
            entityId = entityId,
            entity = MusicBrainzEntity.AREA,
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

    private fun setUpEvents() = runTest {
        setUpCanadianEvents()
        setUpBudokanEvents()
    }

    @Test
    fun `events by entity (area, place)`() = runTest {
        setUpEvents()
    }

    @Test
    fun `all events`() = runTest {
        setUpEvents()
        setUpCollection()

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
        setUpBudokanEvents()
        setupKitanomaruEvents()

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
