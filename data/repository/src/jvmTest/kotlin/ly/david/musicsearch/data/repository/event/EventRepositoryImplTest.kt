package ly.david.musicsearch.data.repository.event

import kotlinx.coroutines.test.runTest
import ly.david.data.test.api.FakeLookupApi
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.shared.domain.history.VisitedDao
import ly.david.musicsearch.data.database.dao.EventDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.musicbrainz.models.UrlMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.common.LifeSpanMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.EventMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.Direction
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.SerializableMusicBrainzEntity
import ly.david.data.test.KoinTestRule
import ly.david.musicsearch.data.repository.RelationRepositoryImpl
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.event.EventDetailsModel
import ly.david.musicsearch.shared.domain.event.EventRepository
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class EventRepositoryImplTest : KoinTest {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val entityHasRelationsDao: EntityHasRelationsDao by inject()
    private val visitedDao: VisitedDao by inject()
    private val relationDao: RelationDao by inject()
    private val eventDao: EventDao by inject()

    private fun createRepository(
        musicBrainzModel: EventMusicBrainzModel,
    ): EventRepository {
        val relationRepository = RelationRepositoryImpl(
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupEvent(
                    eventId: String,
                    include: String?,
                ): EventMusicBrainzModel {
                    return musicBrainzModel
                }
            },
            entityHasRelationsDao = entityHasRelationsDao,
            visitedDao = visitedDao,
            relationDao = relationDao,
        )
        return EventRepositoryImpl(
            eventDao = eventDao,
            relationRepository = relationRepository,
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupEvent(
                    eventId: String,
                    include: String?,
                ): EventMusicBrainzModel {
                    return musicBrainzModel
                }
            },
        )
    }

    @Test
    fun `lookup is cached, and force refresh invalidates cache`() = runTest {
        val sparseRepository = createRepository(
            musicBrainzModel = EventMusicBrainzModel(
                id = "c1fd93a7-d48d-49e1-b87e-55d4e81e9f86",
                name = "The Eras Tour: Toronto (night 1)",
            ),
        )
        val sparseDetailsModel = sparseRepository.lookupEvent(
            eventId = "c1fd93a7-d48d-49e1-b87e-55d4e81e9f86",
            forceRefresh = false,
        )
        assertEquals(
            EventDetailsModel(
                id = "c1fd93a7-d48d-49e1-b87e-55d4e81e9f86",
                name = "The Eras Tour: Toronto (night 1)",
            ),
            sparseDetailsModel,
        )

        val allDataRepository = createRepository(
            musicBrainzModel = EventMusicBrainzModel(
                id = "c1fd93a7-d48d-49e1-b87e-55d4e81e9f86",
                name = "The Eras Tour: Toronto (night 1)",
                lifeSpan = LifeSpanMusicBrainzModel(
                    begin = "2024-11-14",
                ),
                time = "19:00",
                relations = listOf(
                    RelationMusicBrainzModel(
                        type = "ticketing",
                        typeId = "bf0f91b9-d97e-4a7b-9114-f1db1e0b61de",
                        direction = Direction.FORWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            resource = "https://www.ticketmaster.ca/event/10005F01FAA14AE2",
                            id = "674fd59c-9986-4561-a255-a2959d14a5fe",
                        ),
                    ),
                ),
            ),
        )
        var allDataArtistDetailsModel = allDataRepository.lookupEvent(
            eventId = "c1fd93a7-d48d-49e1-b87e-55d4e81e9f86",
            forceRefresh = false,
        )
        assertEquals(
            EventDetailsModel(
                id = "c1fd93a7-d48d-49e1-b87e-55d4e81e9f86",
                name = "The Eras Tour: Toronto (night 1)",
            ),
            allDataArtistDetailsModel,
        )
        allDataArtistDetailsModel = allDataRepository.lookupEvent(
            eventId = "c1fd93a7-d48d-49e1-b87e-55d4e81e9f86",
            forceRefresh = true,
        )
        assertEquals(
            EventDetailsModel(
                id = "c1fd93a7-d48d-49e1-b87e-55d4e81e9f86",
                name = "The Eras Tour: Toronto (night 1)",
                lifeSpan = LifeSpanUiModel(
                    begin = "2024-11-14",
                ),
                time = "19:00",
                urls = listOf(
                    RelationListItemModel(
                        id = "674fd59c-9986-4561-a255-a2959d14a5fe_1",
                        linkedEntityId = "674fd59c-9986-4561-a255-a2959d14a5fe",
                        label = "ticketing",
                        name = "https://www.ticketmaster.ca/event/10005F01FAA14AE2",
                        disambiguation = null,
                        attributes = "",
                        additionalInfo = null,
                        linkedEntity = MusicBrainzEntity.URL,
                        visited = true,
                        isForwardDirection = true,
                    ),
                ),
            ),
            allDataArtistDetailsModel,
        )
    }
}
