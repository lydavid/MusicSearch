package ly.david.musicsearch.data.repository.artist

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.api.FakeBrowseApi
import ly.david.data.test.unitedKingdomAreaMusicBrainzModel
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseArtistsResponse
import ly.david.musicsearch.data.musicbrainz.models.common.LifeSpanMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzModel
import ly.david.musicsearch.data.repository.helpers.TestArtistRepository
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.artist.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.history.VisitedDao
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class ArtistRepositoryImplTest : KoinTest, TestArtistRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    override val artistDao: ArtistDao by inject()
    override val entityHasRelationsDao: EntityHasRelationsDao by inject()
    override val visitedDao: VisitedDao by inject()
    override val relationDao: RelationDao by inject()
    override val areaDao: AreaDao by inject()
    override val browseRemoteMetadataDao: BrowseRemoteMetadataDao by inject()
    private val collectionEntityDao: CollectionEntityDao by inject()

    @Test
    fun `lookup artist`() = runTest {
        val artistRepositoryImpl = createArtistRepository(
            artistMusicBrainzModel = ArtistMusicBrainzModel(
                id = "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
                name = "The Beatles",
                type = "Group",
                lifeSpan = LifeSpanMusicBrainzModel(
                    begin = "1960",
                    end = "1970-04-10",
                    ended = true,
                ),
                sortName = "Beatles, The",
                area = unitedKingdomAreaMusicBrainzModel,
                isnis = listOf("0000000121707484"),
            ),
        )

        val artistDetailsModel = artistRepositoryImpl.lookupArtistDetails(
            "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
            false,
        )
        assertEquals(
            ArtistDetailsModel(
                id = "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
                name = "The Beatles",
                type = "Group",
                lifeSpan = LifeSpanUiModel(
                    begin = "1960",
                    end = "1970-04-10",
                    ended = true,
                ),
                sortName = "Beatles, The",
                areaListItemModel = AreaListItemModel(
                    id = "8a754a16-0027-3a29-b6d7-2b40ea0481ed",
                    name = "United Kingdom",
                    countryCodes = listOf("GB"),
                ),
                isnis = listOf("0000000121707484"),
            ),
            artistDetailsModel,
        )
    }

    @Test
    fun `lookup artist - area without iso-3166-1 code`() = runTest {
        val artistRepositoryImpl = createArtistRepository(
            artistMusicBrainzModel = ArtistMusicBrainzModel(
                id = "5441c29d-3602-4898-b1a1-b77fa23b8e50",
                name = "David Bowie",
                type = "Person",
                gender = "Male",
                lifeSpan = LifeSpanMusicBrainzModel(
                    begin = "1947-01-08",
                    end = "2016-01-10",
                    ended = true,
                ),
                sortName = "Bowie, David",
                area = AreaMusicBrainzModel(
                    id = "9d5dd675-3cf4-4296-9e39-67865ebee758",
                    name = "England",
                    countrySubDivisionCodes = listOf("GB-ENG"),
                ),
                ipis = listOf("00003960406", "00015471209"),
                isnis = listOf("0000000114448576", "0000000458257298"),
            ),
        )

        val artistDetailsModel = artistRepositoryImpl.lookupArtistDetails(
            "5441c29d-3602-4898-b1a1-b77fa23b8e50",
            false,
        )
        assertEquals(
            ArtistDetailsModel(
                id = "5441c29d-3602-4898-b1a1-b77fa23b8e50",
                name = "David Bowie",
                type = "Person",
                gender = "Male",
                lifeSpan = LifeSpanUiModel(
                    begin = "1947-01-08",
                    end = "2016-01-10",
                    ended = true,
                ),
                sortName = "Bowie, David",
                areaListItemModel = AreaListItemModel(
                    id = "9d5dd675-3cf4-4296-9e39-67865ebee758",
                    name = "England",
                    countryCodes = listOf(),
                ),
                ipis = listOf("00003960406", "00015471209"),
                isnis = listOf("0000000114448576", "0000000458257298"),
            ),
            artistDetailsModel,
        )
    }

    @Test
    fun `browse first, then lookup should overwrite with more data`() = runTest {
        val artistsListRepositoryImpl = ArtistsListRepositoryImpl(
            browseRemoteMetadataDao = browseRemoteMetadataDao,
            collectionEntityDao = collectionEntityDao,
            artistDao = artistDao,
            browseApi = object : FakeBrowseApi() {
                override suspend fun browseArtistsByEntity(
                    entityId: String,
                    entity: MusicBrainzEntity,
                    limit: Int,
                    offset: Int,
                ): BrowseArtistsResponse {
                    return BrowseArtistsResponse(
                        count = 1,
                        offset = 0,
                        musicBrainzModels = listOf(
                            ArtistMusicBrainzModel(
                                id = "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
                                name = "The Beatles",
                                type = "Group",
                                lifeSpan = LifeSpanMusicBrainzModel(
                                    begin = "1960",
                                    end = "1970-04-10",
                                    ended = true,
                                ),
                                sortName = "Beatles, The",
                                area = unitedKingdomAreaMusicBrainzModel,
                                countryCode = "GB",
                                isnis = listOf("0000000121707484"),
                            ),
                        ),
                    )
                }
            },
        )
        val flow: Flow<PagingData<ArtistListItemModel>> = artistsListRepositoryImpl.observeArtists(
            browseMethod = BrowseMethod.ByEntity(
                entityId = "area-id",
                entity = MusicBrainzEntity.AREA
            ),
            listFilters = ListFilters(),
        )
        val artists: List<ArtistListItemModel> = flow.asSnapshot()
        assertEquals(
            1,
            artists.size,
        )
        val artistListItem = artists.first()
        assertEquals(
            ArtistListItemModel(
                id = "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
                name = "The Beatles",
                type = "Group",
                lifeSpan = LifeSpanUiModel(
                    begin = "1960",
                    end = "1970-04-10",
                    ended = true,
                ),
                sortName = "Beatles, The",
                countryCode = "GB",
            ),
            artistListItem,
        )

        val artistRepositoryImpl = createArtistRepository(
            artistMusicBrainzModel = ArtistMusicBrainzModel(
                id = "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
                name = "The Beatles",
                type = "Group",
                lifeSpan = LifeSpanMusicBrainzModel(
                    begin = "1960",
                    end = "1970-04-10",
                    ended = true,
                ),
                sortName = "Beatles, The",
                area = unitedKingdomAreaMusicBrainzModel,
                isnis = listOf("0000000121707484"),
            ),
        )
        val artistDetailsModel = artistRepositoryImpl.lookupArtistDetails(
            "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
            false,
        )
        assertEquals(
            ArtistDetailsModel(
                id = "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
                name = "The Beatles",
                type = "Group",
                lifeSpan = LifeSpanUiModel(
                    begin = "1960",
                    end = "1970-04-10",
                    ended = true,
                ),
                sortName = "Beatles, The",
                areaListItemModel = AreaListItemModel(
                    id = "8a754a16-0027-3a29-b6d7-2b40ea0481ed",
                    name = "United Kingdom",
                    countryCodes = listOf("GB"),
                ),
                isnis = listOf("0000000121707484"),
            ),
            artistDetailsModel,
        )
    }

    @Test
    fun `lookup is cached, and force refresh invalidates cache`() = runTest {
        val sparseArtistRepository = createArtistRepository(
            artistMusicBrainzModel = ArtistMusicBrainzModel(
                id = "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
                name = "The Beatles",
            ),
        )
        val sparseArtistDetailsModel = sparseArtistRepository.lookupArtistDetails(
            "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
            false,
        )
        assertEquals(
            ArtistDetailsModel(
                id = "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
                name = "The Beatles",
            ),
            sparseArtistDetailsModel,
        )

        val allDataArtistRepository = createArtistRepository(
            artistMusicBrainzModel = ArtistMusicBrainzModel(
                id = "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
                name = "The Beatles",
                type = "Group",
                sortName = "Beatles, The",
                lifeSpan = LifeSpanMusicBrainzModel(
                    begin = "1960",
                    end = "1970-04-10",
                    ended = true,
                ),
                area = unitedKingdomAreaMusicBrainzModel,
                isnis = listOf("0000000121707484"),
            ),
        )
        var allDataArtistDetailsModel = allDataArtistRepository.lookupArtistDetails(
            "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
            false,
        )
        assertEquals(
            ArtistDetailsModel(
                id = "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
                name = "The Beatles",
            ),
            allDataArtistDetailsModel,
        )
        allDataArtistDetailsModel = allDataArtistRepository.lookupArtistDetails(
            "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
            true,
        )
        assertEquals(
            ArtistDetailsModel(
                id = "b10bbbfc-cf9e-42e0-be17-e2c3e1d2600d",
                name = "The Beatles",
                type = "Group",
                lifeSpan = LifeSpanUiModel(
                    begin = "1960",
                    end = "1970-04-10",
                    ended = true,
                ),
                sortName = "Beatles, The",
                areaListItemModel = AreaListItemModel(
                    id = "8a754a16-0027-3a29-b6d7-2b40ea0481ed",
                    name = "United Kingdom",
                    countryCodes = listOf("GB"),
                ),
                isnis = listOf("0000000121707484"),
            ),
            allDataArtistDetailsModel,
        )
    }
}
