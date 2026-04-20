package ly.david.musicsearch.data.repository.url

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.RelationsMetadataDao
import ly.david.musicsearch.data.database.dao.UrlDao
import ly.david.musicsearch.data.musicbrainz.api.LookupApi
import ly.david.musicsearch.data.musicbrainz.models.UrlMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.UrlMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.relation.Direction
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.SerializableMusicBrainzEntity
import ly.david.musicsearch.data.repository.helpers.TestArtistRepository
import ly.david.musicsearch.data.repository.helpers.testDateTimeInThePast
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.error.ErrorResolution
import ly.david.musicsearch.shared.domain.error.ErrorType
import ly.david.musicsearch.shared.domain.error.HandledException
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class LookupUrlRepositoryImplTest : KoinTest, TestArtistRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    override val artistDao: ArtistDao by inject()
    override val areaDao: AreaDao by inject()
    override val browseRemoteMetadataDao: BrowseRemoteMetadataDao by inject()
    override val relationsMetadataDao: RelationsMetadataDao by inject()
    override val detailsMetadataDao: DetailsMetadataDao by inject()
    override val relationDao: RelationDao by inject()
    override val aliasDao: AliasDao by inject()

    private val lookupApi: LookupApi = mockk()
    private val urlDao: UrlDao by inject()
    override val coroutineDispatchers: CoroutineDispatchers by inject()

    private fun createRepository() = LookupUrlRepositoryImpl(
        lookupApi = lookupApi,
        urlDao = urlDao,
        coroutineDispatchers = coroutineDispatchers,
    )

    @Test(expected = HandledException::class)
    fun `remote lookup returns no results`() = runTest {
        coEvery { lookupApi.lookupUrl(any()) } throws HandledException(
            errorType = ErrorType.NotFound,
            errorResolution = ErrorResolution.None,
            userMessage = "",
        )
        val repository = createRepository()
        val entitiesLinkedToUrl = repository.getEntitiesLinkedToUrl(
            url = "https://example.com",
            searchLocalDatabase = false,
        )
        assertEquals(
            listOf<RelationListItemModel>(),
            entitiesLinkedToUrl,
        )
    }

    @Test
    fun `local lookup returns no results`() = runTest {
        coEvery { lookupApi.lookupUrl(any()) } throws HandledException(
            errorType = ErrorType.NotFound,
            errorResolution = ErrorResolution.None,
            userMessage = "",
        )
        val repository = createRepository()
        val entitiesLinkedToUrl = repository.getEntitiesLinkedToUrl(
            url = "https://example.com",
            searchLocalDatabase = true,
        )
        assertEquals(
            listOf<RelationListItemModel>(),
            entitiesLinkedToUrl,
        )
    }

    @Test
    fun `remote lookup with results`() {
        runTest {
            coEvery { lookupApi.lookupUrl(any()) } returns UrlMusicBrainzNetworkModel(
                relations = listOf(
                    RelationMusicBrainzModel(
                        release = ReleaseMusicBrainzNetworkModel(
                            id = "db0c3753-9394-4e15-ae35-dcdecd108132",
                            name = "盗作",
                        ),
                        targetType = SerializableMusicBrainzEntity.RELEASE,
                        type = "something",
                        typeId = "that won't show",
                        direction = Direction.BACKWARD,
                    ),
                    RelationMusicBrainzModel(
                        release = ReleaseMusicBrainzNetworkModel(
                            id = "ad46ead9-e62b-43fe-bb73-767f40881113",
                            name = "盗作",
                        ),
                        targetType = SerializableMusicBrainzEntity.RELEASE,
                        type = "something",
                        typeId = "that won't show",
                        direction = Direction.BACKWARD,
                    ),
                ),
            )
            val repository = createRepository()
            val entitiesLinkedToUrl = repository.getEntitiesLinkedToUrl(
                url = "",
                searchLocalDatabase = false,
            )
            assertEquals(
                listOf(
                    RelationListItemModel(
                        id = "db0c3753-9394-4e15-ae35-dcdecd108132",
                        linkedEntity = MusicBrainzEntityType.RELEASE,
                        linkedEntityId = "db0c3753-9394-4e15-ae35-dcdecd108132",
                        type = "",
                        name = "盗作",
                        visited = false,
                        disambiguation = "",
                    ),
                    RelationListItemModel(
                        id = "ad46ead9-e62b-43fe-bb73-767f40881113",
                        linkedEntity = MusicBrainzEntityType.RELEASE,
                        linkedEntityId = "ad46ead9-e62b-43fe-bb73-767f40881113",
                        type = "",
                        name = "盗作",
                        visited = false,
                        disambiguation = "",
                    ),
                ),
                entitiesLinkedToUrl,
            )
        }
    }

    @Test
    fun `local lookup with results`() = runTest {
        val artistId = "dfc6a151-3792-4695-8fda-f64723eaa788"
        coEvery { lookupApi.lookupUrl(any()) } returns UrlMusicBrainzNetworkModel(
            relations = listOf(
                RelationMusicBrainzModel(
                    artist = ArtistMusicBrainzNetworkModel(
                        id = artistId,
                        name = "ヨルシカ",
                    ),
                    targetType = SerializableMusicBrainzEntity.RELEASE,
                    type = "",
                    typeId = "",
                    direction = Direction.BACKWARD,
                ),
            ),
        )

        val artistRepository = createArtistRepository(
            artistMusicBrainzModel = ArtistMusicBrainzNetworkModel(
                id = artistId,
                name = "ヨルシカ",
                relations = listOf(
                    RelationMusicBrainzModel(
                        targetType = SerializableMusicBrainzEntity.URL,
                        type = "",
                        typeId = "",
                        direction = Direction.FORWARD,
                        url = UrlMusicBrainzModel(
                            id = "",
                            resource = "https://open.spotify.com/artist/4UK2Lzi6fBfUi9rpDt6cik",
                        ),
                    ),
                ),
            ),
        )
        artistRepository.lookupArtist(
            artistId = artistId,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        )

        val repository = createRepository()
        repository.getEntitiesLinkedToUrl(
            url = "https://open.spotify.com/artist",
            searchLocalDatabase = true,
        ).run {
            assertEquals(
                listOf<RelationListItemModel>(),
                this,
            )
        }
        repository.getEntitiesLinkedToUrl(
            url = "https://open.spotify.com/artist/4UK2Lzi6fBfUi9rpDt6cik",
            searchLocalDatabase = true,
        ).run {
            assertEquals(
                listOf(
                    RelationListItemModel(
                        id = artistId,
                        linkedEntity = MusicBrainzEntityType.ARTIST,
                        linkedEntityId = artistId,
                        type = "",
                        name = "ヨルシカ",
                        visited = true,
                        disambiguation = "",
                    ),
                ),
                this,
            )
        }
    }
}
