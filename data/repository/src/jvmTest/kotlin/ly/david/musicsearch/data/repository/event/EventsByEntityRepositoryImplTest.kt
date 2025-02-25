package ly.david.musicsearch.data.repository.event

import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.api.FakeBrowseApi
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.EventDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseEventsResponse
import ly.david.musicsearch.data.musicbrainz.models.common.LifeSpanMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.EventMusicBrainzModel
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.event.EventsByEntityRepository
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.listitem.EventListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class EventsByEntityRepositoryImplTest : KoinTest {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val eventDao: EventDao by inject()
    private val collectionDao: CollectionDao by inject()
    private val browseEntityCountDao: BrowseEntityCountDao by inject()
    private val collectionEntityDao: CollectionEntityDao by inject()

    private fun createRepositoryWithFakeNetworkData(
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

    // region MB
    private val tsoAtMasseyHallMusicBrainzModel = EventMusicBrainzModel(
        id = "e636f98e-c214-4131-8ca9-2efd7c85197b",
        name = "Toronto Symphony Orchestra at Massey Hall",
        disambiguation = "",
        type = "Concert",
        lifeSpan = LifeSpanMusicBrainzModel(
            begin = "1981-09-12",
            end = "1981-09-12",
            ended = true,
        ),
        cancelled = false,
    )
    private val kissAtScotiabankArenaMusicBrainzModel = EventMusicBrainzModel(
        id = "1fc7c24a-92f1-4b6b-86ff-1b59a0cd4d53",
        name = "KISS at Scotiabank Arena",
        disambiguation = "",
        type = "Concert",
        lifeSpan = LifeSpanMusicBrainzModel(
            begin = "2019-03-20",
            end = "2019-03-20",
            ended = true,
        ),
        cancelled = false,
        time = "18:30",
    )
    private val kissAtBudokanMusicBrainzModel = EventMusicBrainzModel(
        id = "17e85876-40ea-423b-867c-80af2f0cdfe3",
        name = "KISS at Nippon Budokan",
        type = "Concert",
        lifeSpan = LifeSpanMusicBrainzModel(
            begin = "1977-04-01",
            end = "1977-04-01",
            ended = true,
        ),
        cancelled = false,
    )
    private val aimerAtBudokanMusicBrainzModel = EventMusicBrainzModel(
        id = "34f8a930-beb2-441b-b0d7-03c84f92f1ea",
        name = "Aimer Live in 武道館 ”blanc et noir\"",
        type = "Concert",
        lifeSpan = LifeSpanMusicBrainzModel(
            begin = "2017-08-29",
            end = "2017-08-29",
            ended = true,
        ),
        cancelled = false,
        time = "18:00",
    )
    // endregion

    // region UI
    private val tsoAtMasseyHallListItemModel = EventListItemModel(
        id = "e636f98e-c214-4131-8ca9-2efd7c85197b",
        name = "Toronto Symphony Orchestra at Massey Hall",
        disambiguation = "",
        type = "Concert",
        lifeSpan = LifeSpanUiModel(
            begin = "1981-09-12",
            end = "1981-09-12",
            ended = true,
        ),
        cancelled = false,
    )
    private val kissAtScotiabankArenaListItemModel = EventListItemModel(
        id = "1fc7c24a-92f1-4b6b-86ff-1b59a0cd4d53",
        name = "KISS at Scotiabank Arena",
        disambiguation = "",
        type = "Concert",
        lifeSpan = LifeSpanUiModel(
            begin = "2019-03-20",
            end = "2019-03-20",
            ended = true,
        ),
        cancelled = false,
        time = "18:30",
    )
    private val kissAtBudokanListItemModel = EventListItemModel(
        id = "17e85876-40ea-423b-867c-80af2f0cdfe3",
        name = "KISS at Nippon Budokan",
        type = "Concert",
        lifeSpan = LifeSpanUiModel(
            begin = "1977-04-01",
            end = "1977-04-01",
            ended = true,
        ),
        cancelled = false,
    )
    private val aimerAtBudokanListItemModel = EventListItemModel(
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
    )
    // endregion

    @Test
    fun `events by collection, filter by name`() = runTest {
        val collectionId = "950cea33-433e-497f-93bb-a05a393a2c02"
        val events = listOf(
            kissAtScotiabankArenaMusicBrainzModel,
            kissAtBudokanMusicBrainzModel,
        )
        val sut = createRepositoryWithFakeNetworkData(
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

    private fun setUpCanadianEvents() = runTest {
        val entityId = "71bbafaa-e825-3e15-8ca9-017dcad1748b"
        val events = listOf(
            tsoAtMasseyHallMusicBrainzModel,
            kissAtScotiabankArenaMusicBrainzModel,
        )
        val sut = createRepositoryWithFakeNetworkData(
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
            kissAtBudokanMusicBrainzModel,
            aimerAtBudokanMusicBrainzModel,
        )
        val sut = createRepositoryWithFakeNetworkData(
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

        val sut = createRepositoryWithFakeNetworkData(
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
}
