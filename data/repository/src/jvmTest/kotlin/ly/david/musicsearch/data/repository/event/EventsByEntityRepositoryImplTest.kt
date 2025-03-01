package ly.david.musicsearch.data.repository.event

import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.aimerAtBudokanEventMusicBrainzModel
import ly.david.data.test.aimerAtBudokanListItemModel
import ly.david.data.test.api.FakeBrowseApi
import ly.david.data.test.kissAtBudokanEventMusicBrainzModel
import ly.david.data.test.kissAtBudokanListItemModel
import ly.david.data.test.kissAtScotiabankArenaEventMusicBrainzModel
import ly.david.data.test.kissAtScotiabankArenaListItemModel
import ly.david.data.test.tsoAtMasseyHallEventMusicBrainzModel
import ly.david.data.test.tsoAtMasseyHallListItemModel
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.EventDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseEventsResponse
import ly.david.musicsearch.data.musicbrainz.models.core.EventMusicBrainzModel
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.event.EventsByEntityRepository
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
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

    // region UI

    // endregion

    @Test
    fun `events by collection, filter by name`() = runTest {
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
}
