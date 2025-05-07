package ly.david.musicsearch.data.repository.artist

import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.api.FakeBrowseApi
import ly.david.data.test.bandoriCoverCollection8ReleaseMusicBrainzModel
import ly.david.data.test.itouKanakoArtistListItemModel
import ly.david.data.test.itouKanakoArtistMusicBrainzModel
import ly.david.data.test.japanAreaMusicBrainzModel
import ly.david.data.test.roseliaArtistListItemModel
import ly.david.data.test.roseliaArtistMusicBrainzModel
import ly.david.data.test.variousArtistsArtistListItemModel
import ly.david.data.test.variousArtistsArtistMusicBrainzModel
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseArtistsResponse
import ly.david.musicsearch.data.musicbrainz.models.common.LifeSpanMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzModel
import ly.david.musicsearch.data.repository.helpers.FilterTestCase
import ly.david.musicsearch.data.repository.helpers.TestArtistRepository
import ly.david.musicsearch.data.repository.helpers.testFilter
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.artist.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.artist.ArtistsListRepository
import ly.david.musicsearch.shared.domain.history.VisitedDao
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class ArtistsListRepositoryImplTest : KoinTest, TestArtistRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    override val artistDao: ArtistDao by inject()
    override val browseRemoteMetadataDao: BrowseRemoteMetadataDao by inject()
    override val areaDao: AreaDao by inject()
    override val entityHasRelationsDao: EntityHasRelationsDao by inject()
    override val relationDao: RelationDao by inject()
    override val visitedDao: VisitedDao by inject()
    private val collectionDao: CollectionDao by inject()
    private val collectionEntityDao: CollectionEntityDao by inject()

    private fun createArtistsListRepository(
        artists: List<ArtistMusicBrainzModel>,
    ): ArtistsListRepository {
        return ArtistsListRepositoryImpl(
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
                        musicBrainzModels = artists,
                    )
                }
            },
        )
    }

    private val atarayoMusicBrainzModel = ArtistMusicBrainzModel(
        id = "a5083194-56ab-46cd-a235-77a397723e93",
        name = "あたらよ",
        sortName = "Atarayo",
        disambiguation = "",
        type = "Group",
        typeId = "e431f5f6-b5d2-343d-8b36-72607fffb74b",
        isnis = listOf("0000000504027857"),
        countryCode = "JP",
        lifeSpan = LifeSpanMusicBrainzModel(
            begin = "2019-09",
            end = null,
            ended = false,
        ),
    )
    private val bumpOfChickenMusicBrainzModel = ArtistMusicBrainzModel(
        id = "0f718079-e5ea-4cfb-b512-b2d04da66901",
        name = "BUMP OF CHICKEN",
        sortName = "BUMP OF CHICKEN",
        disambiguation = "",
        type = "Group",
        typeId = "e431f5f6-b5d2-343d-8b36-72607fffb74b",
        isnis = listOf(),
        countryCode = "JP",
        lifeSpan = LifeSpanMusicBrainzModel(
            begin = "1994",
            end = null,
            ended = false,
        ),
    )
    private val arcadeFireMusicBrainzModel = ArtistMusicBrainzModel(
        id = "52074ba6-e495-4ef3-9bb4-0703888a9f68",
        name = "Arcade Fire",
        sortName = "Arcade Fire",
        type = "Group",
        countryCode = "CA",
        lifeSpan = LifeSpanMusicBrainzModel(begin = "2001"),
    )
    private val theWeekndMusicBrainzModel = ArtistMusicBrainzModel(
        id = "c8b03190-306c-4120-bb0b-6f2ebfc06ea9",
        name = "The Weeknd",
        sortName = "Weeknd, The",
        disambiguation = "Canadian R&B singer",
        type = "Person",
        gender = "Male",
        countryCode = "CA",
        lifeSpan = LifeSpanMusicBrainzModel(begin = "1990-02-16"),
    )

    private val atarayoListItemModel = ArtistListItemModel(
        id = "a5083194-56ab-46cd-a235-77a397723e93",
        name = "あたらよ",
        sortName = "Atarayo",
        disambiguation = "",
        type = "Group",
        countryCode = "JP",
        lifeSpan = LifeSpanUiModel(
            begin = "2019-09",
            end = null,
            ended = false,
        ),
    )
    private val bumpOfChickenListItemModel = ArtistListItemModel(
        id = "0f718079-e5ea-4cfb-b512-b2d04da66901",
        name = "BUMP OF CHICKEN",
        sortName = "BUMP OF CHICKEN",
        disambiguation = "",
        type = "Group",
        countryCode = "JP",
        lifeSpan = LifeSpanUiModel(
            begin = "1994",
            end = null,
            ended = false,
        ),
    )
    private val arcadeFireListItemModel = ArtistListItemModel(
        id = "52074ba6-e495-4ef3-9bb4-0703888a9f68",
        name = "Arcade Fire",
        sortName = "Arcade Fire",
        type = "Group",
        countryCode = "CA",
        lifeSpan = LifeSpanUiModel(begin = "2001"),
    )
    private val theWeekndListItemModel = ArtistListItemModel(
        id = "c8b03190-306c-4120-bb0b-6f2ebfc06ea9",
        name = "The Weeknd",
        sortName = "Weeknd, The",
        disambiguation = "Canadian R&B singer",
        type = "Person",
        gender = "Male",
        countryCode = "CA",
        lifeSpan = LifeSpanUiModel(begin = "1990-02-16"),
    )

    @Test
    fun `artists by collection, filter by name`() = runTest {
        val collectionId = "950cea33-433e-497f-93bb-a05a393a2c02"
        val artists = listOf(
            atarayoMusicBrainzModel,
            bumpOfChickenMusicBrainzModel,
        )
        val sut = createArtistsListRepository(
            artists = artists,
        )

        collectionDao.insertLocal(
            collection = CollectionListItemModel(
                id = collectionId,
                isRemote = false,
                name = "Who I've been listening to recently",
                entity = MusicBrainzEntity.ARTIST,
            ),
        )
        collectionEntityDao.insertAll(
            collectionId = collectionId,
            entityIds = artists.map { it.id },
        )

        val browseMethod = BrowseMethod.ByEntity(
            entityId = collectionId,
            entity = MusicBrainzEntity.COLLECTION,
        )

        sut.observeArtists(
            browseMethod = browseMethod,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                2,
                size,
            )
            assertEquals(
                listOf(
                    bumpOfChickenListItemModel,
                    atarayoListItemModel,
                ),
                this,
            )
        }

        sut.observeArtists(
            browseMethod = browseMethod,
            listFilters = ListFilters(
                query = "a",
            ),
        ).asSnapshot().run {
            assertEquals(
                1,
                size,
            )
            assertEquals(
                listOf(
                    atarayoListItemModel,
                ),
                this,
            )
        }
    }

    private fun setUpCanadianArtists() = runTest {
        val entityId = "71bbafaa-e825-3e15-8ca9-017dcad1748b"
        val artists = listOf(
            arcadeFireMusicBrainzModel,
            theWeekndMusicBrainzModel,
        )
        val sut = createArtistsListRepository(
            artists = artists,
        )
        val browseMethod = BrowseMethod.ByEntity(
            entityId = entityId,
            entity = MusicBrainzEntity.AREA,
        )
        sut.observeArtists(
            browseMethod = browseMethod,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                2,
                size,
            )
            assertEquals(
                listOf(
                    arcadeFireListItemModel,
                    theWeekndListItemModel,
                ),
                this,
            )
        }
        sut.observeArtists(
            browseMethod = browseMethod,
            listFilters = ListFilters(
                query = "a",
            ),
        ).asSnapshot().run {
            assertEquals(
                2,
                size,
            )
            assertEquals(
                listOf(
                    arcadeFireListItemModel,
                    theWeekndListItemModel,
                ),
                this,
            )
        }
    }

    private fun setUpJapaneseArtists() = runTest {
        val entityId = japanAreaMusicBrainzModel.id
        val artists = listOf(
            atarayoMusicBrainzModel,
            bumpOfChickenMusicBrainzModel,
            roseliaArtistMusicBrainzModel,
            itouKanakoArtistMusicBrainzModel,
        )
        val artistsListRepository = createArtistsListRepository(
            artists = artists,
        )
        val browseMethod = BrowseMethod.ByEntity(
            entityId = entityId,
            entity = MusicBrainzEntity.AREA,
        )
        artistsListRepository.observeArtists(
            browseMethod = browseMethod,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    bumpOfChickenListItemModel,
                    atarayoListItemModel,
                    roseliaArtistListItemModel,
                    itouKanakoArtistListItemModel,
                ),
                this,
            )
        }
        artistsListRepository.observeArtists(
            browseMethod = browseMethod,
            listFilters = ListFilters(
                query = "a",
            ),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    atarayoListItemModel,
                    roseliaArtistListItemModel,
                    itouKanakoArtistListItemModel,
                ),
                this,
            )
        }
    }

    private fun setUpArtistsByArea() = runTest {
        setUpCanadianArtists()
        setUpJapaneseArtists()
    }

    @Test
    fun `artists by entity (area)`() = runTest {
        setUpArtistsByArea()
    }

    @Test
    fun setUpArtistsByRelease() = runTest {
        val entityId = bandoriCoverCollection8ReleaseMusicBrainzModel.id
        val artists = listOf(
            variousArtistsArtistMusicBrainzModel,
            roseliaArtistMusicBrainzModel,
            itouKanakoArtistMusicBrainzModel,
        )
        val artistsListRepository = createArtistsListRepository(
            artists = artists,
        )
        val browseMethod = BrowseMethod.ByEntity(
            entityId = entityId,
            entity = MusicBrainzEntity.RELEASE,
        )
        testFilter(
            pagingFlowProducer = { query ->
                artistsListRepository.observeArtists(
                    browseMethod = browseMethod,
                    listFilters = ListFilters(query = query),
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "no filter",
                    query = "",
                    expectedResult = listOf(
                        variousArtistsArtistListItemModel,
                        roseliaArtistListItemModel,
                        itouKanakoArtistListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by type",
                    query = "other",
                    expectedResult = listOf(
                        variousArtistsArtistListItemModel,
                    ),
                ),
            ),
        )
    }

    @Test
    fun `all artists`() = runTest {
        setUpArtistsByArea()
        setUpArtistsByRelease()

        val artistsListRepository = createArtistsListRepository(
            artists = listOf(),
        )
        testFilter(
            pagingFlowProducer = { query ->
                artistsListRepository.observeArtists(
                    browseMethod = BrowseMethod.All,
                    listFilters = ListFilters(query = query),
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "no filter",
                    query = "",
                    expectedResult = listOf(
                        variousArtistsArtistListItemModel,
                        itouKanakoArtistListItemModel,
                        theWeekndListItemModel,
                        bumpOfChickenListItemModel,
                        arcadeFireListItemModel,
                        roseliaArtistListItemModel,
                        atarayoListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by name",
                    query = "a",
                    expectedResult = listOf(
                        variousArtistsArtistListItemModel,
                        itouKanakoArtistListItemModel,
                        theWeekndListItemModel,
                        arcadeFireListItemModel,
                        roseliaArtistListItemModel,
                        atarayoListItemModel,
                    ),
                ),
            ),
        )
    }

    @Test
    fun `refreshing artists does not delete the artist`() = runTest {
        setUpArtistsByRelease()
        setUpJapaneseArtists()

        val modifiedArtistsByArea = listOf(
            roseliaArtistMusicBrainzModel.copy(
                disambiguation = "some change that won't show up",
            ),
            itouKanakoArtistMusicBrainzModel,
            bumpOfChickenMusicBrainzModel.copy(
                disambiguation = "this won't either",
            ),
        )
        val artistsListRepository = createArtistsListRepository(
            artists = modifiedArtistsByArea,
        )

        // refresh
        artistsListRepository.observeArtists(
            browseMethod = BrowseMethod.ByEntity(
                entityId = japanAreaMusicBrainzModel.id,
                entity = MusicBrainzEntity.AREA,
            ),
            listFilters = ListFilters(),
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                listOf(
                    bumpOfChickenListItemModel,
                    roseliaArtistListItemModel,
                    itouKanakoArtistListItemModel,
                ),
                this,
            )
        }

        // other entities remain unchanged
        artistsListRepository.observeArtists(
            browseMethod = BrowseMethod.ByEntity(
                entityId = bandoriCoverCollection8ReleaseMusicBrainzModel.id,
                entity = MusicBrainzEntity.RELEASE,
            ),
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    variousArtistsArtistListItemModel,
                    roseliaArtistListItemModel,
                    itouKanakoArtistListItemModel,
                ),
                this,
            )
        }

        // now visit the artist
        val artistRepository = createArtistRepository(
            roseliaArtistMusicBrainzModel.copy(
                disambiguation = "some change that won't show up",
            ),
        )
        // the first lookup will replace existing data
        artistRepository.lookupArtistDetails(
            artistId = roseliaArtistMusicBrainzModel.id,
            forceRefresh = false,
        ).run {
            assertEquals(
                ArtistDetailsModel(
                    id = "adea3c3d-a84d-4f9e-ac0b-1ef71a8947a5",
                    name = "Roselia",
                    sortName = "Roselia",
                    type = "Group",
                    disambiguation = "some change that won't show up",
                    lifeSpan = LifeSpanUiModel(
                        begin = "2016-09-15",
                    ),
                ),
                this,
            )
        }
        artistRepository.lookupArtistDetails(
            artistId = roseliaArtistMusicBrainzModel.id,
            forceRefresh = true,
        ).run {
            assertEquals(
                ArtistDetailsModel(
                    id = "adea3c3d-a84d-4f9e-ac0b-1ef71a8947a5",
                    name = "Roselia",
                    sortName = "Roselia",
                    type = "Group",
                    disambiguation = "some change that won't show up",
                    lifeSpan = LifeSpanUiModel(
                        begin = "2016-09-15",
                    ),
                ),
                this,
            )
        }
    }
}
