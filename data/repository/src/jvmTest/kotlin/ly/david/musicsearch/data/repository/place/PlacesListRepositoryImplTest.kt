package ly.david.musicsearch.data.repository.place

import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.api.FakeBrowseApi
import ly.david.data.test.budokanPlaceListItemModel
import ly.david.data.test.budokanPlaceMusicBrainzModel
import ly.david.data.test.chiyodaAreaMusicBrainzModel
import ly.david.data.test.kitanomaruAreaMusicBrainzModel
import ly.david.data.test.marunouchiAreaListItemModel
import ly.david.data.test.marunouchiAreaMusicBrainzModel
import ly.david.data.test.tokyoInternationForumHallAPlaceListItemModel
import ly.david.data.test.tokyoInternationForumHallAPlaceMusicBrainzModel
import ly.david.data.test.tokyoInternationForumPlaceListItemModel
import ly.david.data.test.tokyoInternationForumPlaceMusicBrainzModel
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.data.database.dao.PlaceDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.musicbrainz.api.BrowsePlacesResponse
import ly.david.musicsearch.data.musicbrainz.models.core.PlaceMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.helpers.FilterTestCase
import ly.david.musicsearch.data.repository.helpers.TestPlaceRepository
import ly.david.musicsearch.data.repository.helpers.testFilter
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.listitem.PlaceListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.place.CoordinatesUiModel
import ly.david.musicsearch.shared.domain.details.PlaceDetailsModel
import ly.david.musicsearch.shared.domain.place.PlacesListRepository
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class PlacesListRepositoryImplTest : KoinTest, TestPlaceRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()
    override val placeDao: PlaceDao by inject()
    override val areaDao: AreaDao by inject()
    override val entityHasRelationsDao: EntityHasRelationsDao by inject()
    override val visitedDao: DetailsMetadataDao by inject()
    override val relationDao: RelationDao by inject()
    private val collectionDao: CollectionDao by inject()
    override val browseRemoteMetadataDao: BrowseRemoteMetadataDao by inject()
    override val collectionEntityDao: CollectionEntityDao by inject()

    private fun createPlacesListRepository(
        places: List<PlaceMusicBrainzNetworkModel>,
    ): PlacesListRepository {
        return PlacesListRepositoryImpl(
            browseRemoteMetadataDao = browseRemoteMetadataDao,
            collectionEntityDao = collectionEntityDao,
            placeDao = placeDao,
            browseApi = object : FakeBrowseApi() {
                override suspend fun browsePlacesByEntity(
                    entityId: String,
                    entity: MusicBrainzEntity,
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
        val placesListRepository = createPlacesListRepository(
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

        val browseMethod = BrowseMethod.ByEntity(
            entityId = collectionId,
            entity = MusicBrainzEntity.COLLECTION,
        )

        placesListRepository.observePlaces(
            browseMethod = browseMethod,
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

        placesListRepository.observePlaces(
            browseMethod = browseMethod,
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
        val entityId = kitanomaruAreaMusicBrainzModel.id
        val entity = MusicBrainzEntity.AREA
        val places = listOf(
            budokanPlaceMusicBrainzModel,
        )
        val sut = createPlacesListRepository(
            places = places,
        )
        val browseMethod = BrowseMethod.ByEntity(
            entityId = entityId,
            entity = entity,
        )
        sut.observePlaces(
            browseMethod = browseMethod,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    budokanPlaceListItemModel,
                ),
                this,
            )
        }
        sut.observePlaces(
            browseMethod = browseMethod,
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
        val places = listOf(
            tokyoInternationForumPlaceMusicBrainzModel,
            tokyoInternationForumHallAPlaceMusicBrainzModel,
        )
        val placesListRepository = createPlacesListRepository(
            places = places,
        )
        testFilter(
            pagingFlowProducer = { query ->
                placesListRepository.observePlaces(
                    browseMethod = BrowseMethod.ByEntity(
                        entityId = marunouchiAreaMusicBrainzModel.id,
                        entity = MusicBrainzEntity.AREA,
                    ),
                    listFilters = ListFilters(
                        query = query,
                    ),
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "no filter",
                    query = "",
                    expectedResult = listOf(
                        tokyoInternationForumPlaceListItemModel,
                        tokyoInternationForumHallAPlaceListItemModel,

                    ),
                ),
                FilterTestCase(
                    description = "filter by disambiguation",
                    query = "com",
                    expectedResult = listOf(
                        tokyoInternationForumPlaceListItemModel,
                    ),
                ),

            ),
        )
    }

    private fun setUpChiyodaPlaces() = runTest {
        val places = listOf(
            tokyoInternationForumPlaceMusicBrainzModel,
            tokyoInternationForumHallAPlaceMusicBrainzModel,
        )
        val placesListRepository = createPlacesListRepository(
            places = places,
        )
        testFilter(
            pagingFlowProducer = { query ->
                placesListRepository.observePlaces(
                    browseMethod = BrowseMethod.ByEntity(
                        entityId = chiyodaAreaMusicBrainzModel.id,
                        entity = MusicBrainzEntity.AREA,
                    ),
                    listFilters = ListFilters(
                        query = query,
                    ),
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "no filter",
                    query = "",
                    expectedResult = listOf(
                        tokyoInternationForumPlaceListItemModel,
                        tokyoInternationForumHallAPlaceListItemModel,

                    ),
                ),
                FilterTestCase(
                    description = "filter by disambiguation",
                    query = "com",
                    expectedResult = listOf(
                        tokyoInternationForumPlaceListItemModel,
                    ),
                ),

            ),
        )
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

        val sut = createPlacesListRepository(
            places = listOf(),
        )
        sut.observePlaces(
            browseMethod = BrowseMethod.All,
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
        sut.observePlaces(
            browseMethod = BrowseMethod.All,
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

    @Test
    fun `refreshing places does not delete the place`() = runTest {
        setUpMarunouchiPlaces()
        setUpChiyodaPlaces()

        val modifiedPlaces = listOf(
            tokyoInternationForumPlaceMusicBrainzModel.copy(
                address = "some new address",
            ),
            tokyoInternationForumHallAPlaceMusicBrainzModel,
        )
        val placesListRepository = createPlacesListRepository(
            places = modifiedPlaces,
        )

        // refresh
        placesListRepository.observePlaces(
            browseMethod = BrowseMethod.ByEntity(
                entityId = marunouchiAreaMusicBrainzModel.id,
                entity = MusicBrainzEntity.AREA,
            ),
            listFilters = ListFilters(),
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                listOf(
                    tokyoInternationForumPlaceListItemModel,
                    tokyoInternationForumHallAPlaceListItemModel,
                ),
                this,
            )
        }

        // other entities remain unchanged
        placesListRepository.observePlaces(
            browseMethod = BrowseMethod.ByEntity(
                entityId = chiyodaAreaMusicBrainzModel.id,
                entity = MusicBrainzEntity.AREA,
            ),
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

        // now visit the place and refresh it
        val placeRepository = createPlaceRepository(
            tokyoInternationForumPlaceMusicBrainzModel.copy(
                address = "some new address",
            ),
        )
        // the first lookup will replace existing data
        placeRepository.lookupPlace(
            placeId = tokyoInternationForumPlaceMusicBrainzModel.id,
            forceRefresh = false,
        ).run {
            assertEquals(
                PlaceDetailsModel(
                    id = "623d61ce-d422-4d3a-b6bb-c02cd64c715d",
                    name = "東京国際フォーラム",
                    disambiguation = "complex; use ONLY if no more specific venue info is available!",
                    address = "〒100-0005 東京都千代田区丸の内三丁目5番1号",
                    type = "Other",
                    lifeSpan = LifeSpanUiModel(
                        begin = "1997-01-10",
                        ended = false,
                    ),
                    coordinates = CoordinatesUiModel(
                        longitude = 139.7642,
                        latitude = 35.676925,
                    ),
                    area = marunouchiAreaListItemModel.copy(
                        type = null,
                    ),
                ),
                this,
            )
        }
        placeRepository.lookupPlace(
            placeId = tokyoInternationForumPlaceMusicBrainzModel.id,
            forceRefresh = true,
        ).run {
            assertEquals(
                PlaceDetailsModel(
                    id = "623d61ce-d422-4d3a-b6bb-c02cd64c715d",
                    name = "東京国際フォーラム",
                    disambiguation = "complex; use ONLY if no more specific venue info is available!",
                    address = "some new address",
                    type = "Other",
                    lifeSpan = LifeSpanUiModel(
                        begin = "1997-01-10",
                        ended = false,
                    ),
                    coordinates = CoordinatesUiModel(
                        longitude = 139.7642,
                        latitude = 35.676925,
                    ),
                    area = marunouchiAreaListItemModel.copy(
                        type = null,
                    ),
                ),
                this,
            )
        }
    }
}
