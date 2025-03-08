package ly.david.musicsearch.data.repository.place

import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.api.FakeBrowseApi
import ly.david.data.test.budokanPlaceListItemModel
import ly.david.data.test.budokanPlaceMusicBrainzModel
import ly.david.data.test.tokyoInternationForumHallAPlaceListItemModel
import ly.david.data.test.tokyoInternationForumHallAPlaceMusicBrainzModel
import ly.david.data.test.tokyoInternationForumPlaceListItemModel
import ly.david.data.test.tokyoInternationForumPlaceMusicBrainzModel
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.PlaceDao
import ly.david.musicsearch.data.musicbrainz.api.BrowsePlacesResponse
import ly.david.musicsearch.data.musicbrainz.models.core.PlaceMusicBrainzModel
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.listitem.PlaceListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.place.PlacesByEntityRepository
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class PlacesByEntityRepositoryImplTest : KoinTest {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val placeDao: PlaceDao by inject()
    private val collectionDao: CollectionDao by inject()
    private val browseEntityCountDao: BrowseEntityCountDao by inject()
    private val collectionEntityDao: CollectionEntityDao by inject()

    private fun createRepository(
        places: List<PlaceMusicBrainzModel>,
    ): PlacesByEntityRepository {
        return PlacesByEntityRepositoryImpl(
            browseEntityCountDao = browseEntityCountDao,
            collectionEntityDao = collectionEntityDao,
            placeDao = placeDao,
            browseApi = object : FakeBrowseApi() {
                override suspend fun browsePlacesByCollection(
                    collectionId: String,
                    limit: Int,
                    offset: Int,
                ): BrowsePlacesResponse {
                    return BrowsePlacesResponse(
                        count = 1,
                        offset = 0,
                        musicBrainzModels = places,
                    )
                }

                override suspend fun browsePlacesByArea(
                    areaId: String,
                    limit: Int,
                    offset: Int,
                ): BrowsePlacesResponse {
                    return BrowsePlacesResponse(
                        count = 1,
                        offset = 0,
                        musicBrainzModels = places,
                    )
                }
            },
        )
    }

    private fun setUpPlaceCollection() = runTest {
        val collectionId = "950cea33-433e-497f-93bb-a05a393a2c02"
        val places = listOf(
            budokanPlaceMusicBrainzModel,
            tokyoInternationForumHallAPlaceMusicBrainzModel,
        )
        val sut = createRepository(
            places = places,
        )

        collectionDao.insertLocal(
            collection = CollectionListItemModel(
                id = collectionId,
                isRemote = false,
                name = "places",
                entity = MusicBrainzEntity.PLACE,
            ),
        )
        collectionEntityDao.insertAll(
            collectionId = collectionId,
            entityIds = places.map { it.id },
        )

        sut.observePlacesByEntity(
            entityId = collectionId,
            entity = MusicBrainzEntity.COLLECTION,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    budokanPlaceListItemModel,
                    tokyoInternationForumHallAPlaceListItemModel,
                ),
                this,
            )
        }

        sut.observePlacesByEntity(
            entityId = collectionId,
            entity = MusicBrainzEntity.COLLECTION,
            listFilters = ListFilters(
                query = "are",
            ),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    budokanPlaceListItemModel,
                ),
                this,
            )
        }
    }

    @Test
    fun `places by collection, filter by type`() = runTest {
        setUpPlaceCollection()
    }

    private fun setUpKitanomaruPlaces() = runTest {
        val entityId = "e24c0f02-9b5a-4f4f-9fe0-f8b3e67874f8"
        val places = listOf(
            budokanPlaceMusicBrainzModel,
        )
        val sut = createRepository(
            places = places,
        )
        sut.observePlacesByEntity(
            entityId = entityId,
            entity = MusicBrainzEntity.AREA,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    budokanPlaceListItemModel,
                ),
                this,
            )
        }
        sut.observePlacesByEntity(
            entityId = entityId,
            entity = MusicBrainzEntity.AREA,
            listFilters = ListFilters(
                query = "b",
            ),
        ).asSnapshot().run {
            assertEquals(
                emptyList<PlaceListItemModel>(),
                this,
            )
        }
    }

    private fun setUpMarunouchiPlaces() = runTest {
        val entityId = "41cd0808-6e0a-4ec6-ab02-14add4db58ae"
        val places = listOf(
            tokyoInternationForumPlaceMusicBrainzModel,
            tokyoInternationForumHallAPlaceMusicBrainzModel,
        )
        val sut = createRepository(
            places = places,
        )
        sut.observePlacesByEntity(
            entityId = entityId,
            entity = MusicBrainzEntity.AREA,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    tokyoInternationForumPlaceListItemModel,
                    tokyoInternationForumHallAPlaceListItemModel,
                ),
                this,
            )
        }
        sut.observePlacesByEntity(
            entityId = entityId,
            entity = MusicBrainzEntity.AREA,
            listFilters = ListFilters(
                query = "com",
            ),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    tokyoInternationForumPlaceListItemModel,
                ),
                this,
            )
        }
    }

    private fun setUpPlaces() = runTest {
        setUpKitanomaruPlaces()
        setUpMarunouchiPlaces()
    }

    @Test
    fun `places by entity (area)`() = runTest {
        setUpPlaces()
    }

    @Test
    fun `all places`() = runTest {
        setUpPlaces()
        setUpPlaceCollection()

        val sut = createRepository(
            places = listOf(),
        )
        sut.observePlacesByEntity(
            entityId = null,
            entity = null,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    budokanPlaceListItemModel,
                    tokyoInternationForumPlaceListItemModel,
                    tokyoInternationForumHallAPlaceListItemModel,
                ),
                this,
            )
        }
        sut.observePlacesByEntity(
            entityId = null,
            entity = null,
            listFilters = ListFilters(
                query = "ve",
            ),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    tokyoInternationForumPlaceListItemModel,
                    tokyoInternationForumHallAPlaceListItemModel,
                ),
                this,
            )
        }
    }
}
