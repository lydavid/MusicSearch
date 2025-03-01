package ly.david.musicsearch.data.repository.artist

import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.api.FakeBrowseApi
import ly.david.musicsearch.data.database.dao.ArtistDao
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseArtistsResponse
import ly.david.musicsearch.data.musicbrainz.models.common.LifeSpanMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzModel
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.artist.ArtistsByEntityRepository
import ly.david.musicsearch.shared.domain.listitem.ArtistListItemModel
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class ArtistsByEntityRepositoryImplTest : KoinTest {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val artistDao: ArtistDao by inject()
    private val collectionDao: CollectionDao by inject()
    private val browseEntityCountDao: BrowseEntityCountDao by inject()
    private val collectionEntityDao: CollectionEntityDao by inject()

    private fun createRepository(
        artists: List<ArtistMusicBrainzModel>,
    ): ArtistsByEntityRepository {
        return ArtistsByEntityRepositoryImpl(
            browseEntityCountDao = browseEntityCountDao,
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
        val sut = createRepository(
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

        sut.observeArtistsByEntity(
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
                    bumpOfChickenListItemModel,
                    atarayoListItemModel,
                ),
                this,
            )
        }

        sut.observeArtistsByEntity(
            entityId = collectionId,
            entity = MusicBrainzEntity.COLLECTION,
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
        val sut = createRepository(
            artists = artists,
        )
        sut.observeArtistsByEntity(
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
                    arcadeFireListItemModel,
                    theWeekndListItemModel,
                ),
                this,
            )
        }
        sut.observeArtistsByEntity(
            entityId = entityId,
            entity = MusicBrainzEntity.AREA,
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
        val entityId = "2db42837-c832-3c27-b4a3-08198f75693c"
        val artists = listOf(
            atarayoMusicBrainzModel,
            bumpOfChickenMusicBrainzModel,
        )
        val sut = createRepository(
            artists = artists,
        )
        sut.observeArtistsByEntity(
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
                    bumpOfChickenListItemModel,
                    atarayoListItemModel,
                ),
                this,
            )
        }
        sut.observeArtistsByEntity(
            entityId = entityId,
            entity = MusicBrainzEntity.AREA,
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

    private fun setUpArtists() = runTest {
        setUpCanadianArtists()
        setUpJapaneseArtists()
    }

    @Test
    fun `artists by entity (area)`() = runTest {
        setUpArtists()
    }

    @Test
    fun `all artists`() = runTest {
        setUpArtists()

        val sut = createRepository(
            artists = listOf(),
        )
        sut.observeArtistsByEntity(
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
                    theWeekndListItemModel,
                    bumpOfChickenListItemModel,
                    arcadeFireListItemModel,
                    atarayoListItemModel,
                ),
                this,
            )
        }
        sut.observeArtistsByEntity(
            entityId = null,
            entity = null,
            listFilters = ListFilters(
                query = "a",
            ),
        ).asSnapshot().run {
            assertEquals(
                3,
                size,
            )
            assertEquals(
                listOf(
                    theWeekndListItemModel,
                    arcadeFireListItemModel,
                    atarayoListItemModel,
                ),
                this,
            )
        }
    }
}
