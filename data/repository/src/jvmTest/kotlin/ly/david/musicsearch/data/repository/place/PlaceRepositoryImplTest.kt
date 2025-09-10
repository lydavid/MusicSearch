package ly.david.musicsearch.data.repository.place

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.api.FakeBrowseApi
import ly.david.data.test.api.FakeLookupApi
import ly.david.data.test.budokanPlaceMusicBrainzModel
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.PlaceDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.RelationsMetadataDao
import ly.david.musicsearch.data.musicbrainz.api.BrowsePlacesResponse
import ly.david.musicsearch.data.musicbrainz.models.UrlMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.common.LifeSpanMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.CoordinatesMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.PlaceMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.relation.Direction
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.SerializableMusicBrainzEntity
import ly.david.musicsearch.data.repository.RelationRepositoryImpl
import ly.david.musicsearch.data.repository.area.AreaRepositoryImpl
import ly.david.musicsearch.data.repository.helpers.TestPlaceRepository
import ly.david.musicsearch.data.repository.helpers.testDateTimeInThePast
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.details.AreaDetailsModel
import ly.david.musicsearch.shared.domain.details.PlaceDetailsModel
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.PlaceListItemModel
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.place.CoordinatesUiModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class PlaceRepositoryImplTest : KoinTest, TestPlaceRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    override val relationsMetadataDao: RelationsMetadataDao by inject()
    override val detailsMetadataDao: DetailsMetadataDao by inject()
    override val relationDao: RelationDao by inject()
    override val placeDao: PlaceDao by inject()
    override val areaDao: AreaDao by inject()
    override val browseRemoteMetadataDao: BrowseRemoteMetadataDao by inject()
    override val collectionEntityDao: CollectionEntityDao by inject()
    override val aliasDao: AliasDao by inject()

    private fun createAreaRepositoryWithFakeNetworkData(
        musicBrainzModel: AreaMusicBrainzNetworkModel,
    ): AreaRepositoryImpl {
        val relationRepository = RelationRepositoryImpl(
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupArea(
                    areaId: String,
                    include: String?,
                ): AreaMusicBrainzNetworkModel {
                    return musicBrainzModel
                }
            },
            relationsMetadataDao = relationsMetadataDao,
            detailsMetadataDao = detailsMetadataDao,
            relationDao = relationDao,
        )
        return AreaRepositoryImpl(
            areaDao = areaDao,
            relationRepository = relationRepository,
            aliasDao = aliasDao,
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupArea(
                    areaId: String,
                    include: String?,
                ): AreaMusicBrainzNetworkModel {
                    return musicBrainzModel
                }
            },
        )
    }

    @Test
    fun `lookup is cached, and force refresh invalidates cache`() = runTest {
        val sparseRepository = createPlaceRepository(
            musicBrainzModel = PlaceMusicBrainzNetworkModel(
                id = "4d43b9d8-162d-4ac5-8068-dfb009722484",
                name = "日本武道館",
                address = "〒102-8321 東京都千代田区北の丸公園2-3",
            ),
        )
        val sparseDetailsModel = sparseRepository.lookupPlace(
            placeId = "4d43b9d8-162d-4ac5-8068-dfb009722484",
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        )
        assertEquals(
            PlaceDetailsModel(
                id = "4d43b9d8-162d-4ac5-8068-dfb009722484",
                name = "日本武道館",
                address = "〒102-8321 東京都千代田区北の丸公園2-3",
                lastUpdated = testDateTimeInThePast,
            ),
            sparseDetailsModel,
        )

        val allDataRepository = createPlaceRepository(
            musicBrainzModel = PlaceMusicBrainzNetworkModel(
                id = "4d43b9d8-162d-4ac5-8068-dfb009722484",
                name = "日本武道館",
                address = "〒102-8321 東京都千代田区北の丸公園2-3",
                type = "Indoor arena",
                typeId = "a77c11f6-82fa-3cc0-9041-ac60e5f6e024",
                lifeSpan = LifeSpanMusicBrainzModel(
                    begin = "1964-10-03",
                ),
                coordinates = CoordinatesMusicBrainzModel(
                    longitude = 139.75,
                    latitude = 35.69333,
                ),
                area = AreaMusicBrainzNetworkModel(
                    id = "e24c0f02-9b5a-4f4f-9fe0-f8b3e67874f8",
                    name = "Kitanomaru Kōen",
                ),
                relations = listOf(
                    RelationMusicBrainzModel(
                        type = "discogs",
                        typeId = "1c140ac8-8dc2-449e-92cb-52c90d525640",
                        direction = Direction.FORWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            resource = "https://www.discogs.com/label/268904",
                            id = "e893b81b-a678-4989-858f-c83a30243b7b",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "image",
                        typeId = "68a4537c-f2a6-49b8-81c5-82a62b0976b7",
                        direction = Direction.FORWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            resource = "https://commons.wikimedia.org/wiki/File:Nippon_Budokan_2010.jpg",
                            id = "66fa274b-74d5-47c4-85aa-d168fff294ea",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "official homepage",
                        typeId = "696b79da-7e45-40e6-a9d4-b31438eb7e5d",
                        direction = Direction.FORWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            resource = "https://www.nipponbudokan.or.jp/",
                            id = "4adeee43-1b70-4144-8dfb-96713e868b33",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "other databases",
                        typeId = "87a0a644-0a69-46c0-9e48-0656b8240d89",
                        direction = Direction.FORWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            resource = "https://www.generasia.com/wiki/Nippon_Budokan",
                            id = "ab71b5f3-9c59-4595-abdc-6b047db1eb20",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "other databases",
                        typeId = "87a0a644-0a69-46c0-9e48-0656b8240d89",
                        direction = Direction.FORWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            resource = "https://www.livefans.jp/venues/4699",
                            id = "860a6bef-cee3-406e-a19e-54938a456926",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "setlistfm",
                        typeId = "751e8fb1-ed8d-4a94-b71b-a38065054f5d",
                        direction = Direction.FORWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            resource = "https://www.setlist.fm/venue/nippon-budokan-tokyo-japan-3d61d43.html",
                            id = "d1d588f8-abb9-4f12-b421-b2e9a2acf800",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "songkick",
                        typeId = "3eb58d3e-6f00-36a8-a115-3dad616b7391",
                        direction = Direction.FORWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            resource = "https://www.songkick.com/venues/33448",
                            id = "95cb03e2-8126-417d-82c1-84ef0274b6f8",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "wikidata",
                        typeId = "e6826618-b410-4b8d-b3b5-52e29eac5e1f",
                        direction = Direction.FORWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            resource = "https://www.wikidata.org/wiki/Q386246",
                            id = "4674bf4a-5ae8-4154-a223-4c26338622bd",
                        ),
                    ),
                ),
            ),
        )
        var allDataArtistDetailsModel = allDataRepository.lookupPlace(
            placeId = "4d43b9d8-162d-4ac5-8068-dfb009722484",
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        )
        assertEquals(
            PlaceDetailsModel(
                id = "4d43b9d8-162d-4ac5-8068-dfb009722484",
                name = "日本武道館",
                address = "〒102-8321 東京都千代田区北の丸公園2-3",
                lastUpdated = testDateTimeInThePast,
            ),
            allDataArtistDetailsModel,
        )
        allDataArtistDetailsModel = allDataRepository.lookupPlace(
            placeId = "4d43b9d8-162d-4ac5-8068-dfb009722484",
            forceRefresh = true,
            lastUpdated = testDateTimeInThePast,
        )
        assertEquals(
            PlaceDetailsModel(
                id = "4d43b9d8-162d-4ac5-8068-dfb009722484",
                name = "日本武道館",
                address = "〒102-8321 東京都千代田区北の丸公園2-3",
                type = "Indoor arena",
                lifeSpan = LifeSpanUiModel(
                    begin = "1964-10-03",
                ),
                coordinates = CoordinatesUiModel(
                    longitude = 139.75,
                    latitude = 35.69333,
                ),
                area = AreaListItemModel(
                    id = "e24c0f02-9b5a-4f4f-9fe0-f8b3e67874f8",
                    name = "Kitanomaru Kōen",
                ),
                lastUpdated = testDateTimeInThePast,
                urls = persistentListOf(
                    RelationListItemModel(
                        id = "e893b81b-a678-4989-858f-c83a30243b7b_8",
                        linkedEntityId = "e893b81b-a678-4989-858f-c83a30243b7b",
                        type = "Discogs",
                        name = "https://www.discogs.com/label/268904",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntityType.URL,
                        visited = true,
                        isForwardDirection = true,
                    ),
                    RelationListItemModel(
                        id = "95cb03e2-8126-417d-82c1-84ef0274b6f8_14",
                        linkedEntityId = "95cb03e2-8126-417d-82c1-84ef0274b6f8",
                        type = "Songkick",
                        name = "https://www.songkick.com/venues/33448",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntityType.URL,
                        visited = true,
                        isForwardDirection = true,
                    ),
                    RelationListItemModel(
                        id = "4674bf4a-5ae8-4154-a223-4c26338622bd_15",
                        linkedEntityId = "4674bf4a-5ae8-4154-a223-4c26338622bd",
                        type = "Wikidata",
                        name = "https://www.wikidata.org/wiki/Q386246",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntityType.URL,
                        visited = true,
                        isForwardDirection = true,
                    ),
                    RelationListItemModel(
                        id = "4adeee43-1b70-4144-8dfb-96713e868b33_10",
                        linkedEntityId = "4adeee43-1b70-4144-8dfb-96713e868b33",
                        type = "official homepages",
                        name = "https://www.nipponbudokan.or.jp/",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntityType.URL,
                        visited = true,
                        isForwardDirection = true,
                    ),
                    RelationListItemModel(
                        id = "ab71b5f3-9c59-4595-abdc-6b047db1eb20_11",
                        linkedEntityId = "ab71b5f3-9c59-4595-abdc-6b047db1eb20",
                        type = "other databases",
                        name = "https://www.generasia.com/wiki/Nippon_Budokan",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntityType.URL,
                        visited = true,
                        isForwardDirection = true,
                    ),
                    RelationListItemModel(
                        id = "860a6bef-cee3-406e-a19e-54938a456926_12",
                        linkedEntityId = "860a6bef-cee3-406e-a19e-54938a456926",
                        type = "other databases",
                        name = "https://www.livefans.jp/venues/4699",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntityType.URL,
                        visited = true,
                        isForwardDirection = true,
                    ),
                    RelationListItemModel(
                        id = "66fa274b-74d5-47c4-85aa-d168fff294ea_9",
                        linkedEntityId = "66fa274b-74d5-47c4-85aa-d168fff294ea",
                        type = "picture",
                        name = "https://commons.wikimedia.org/wiki/File:Nippon_Budokan_2010.jpg",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntityType.URL,
                        visited = true,
                        isForwardDirection = true,
                    ),
                    RelationListItemModel(
                        id = "d1d588f8-abb9-4f12-b421-b2e9a2acf800_13",
                        linkedEntityId = "d1d588f8-abb9-4f12-b421-b2e9a2acf800",
                        type = "setlist.fm",
                        name = "https://www.setlist.fm/venue/nippon-budokan-tokyo-japan-3d61d43.html",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntityType.URL,
                        visited = true,
                        isForwardDirection = true,
                    ),
                ),
            ),
            allDataArtistDetailsModel,
        )
    }

    @Test
    fun `place will use the most specific area`() = runTest {
        val countryId = "2db42837-c832-3c27-b4a3-08198f75693c"
        val districtId = "e24c0f02-9b5a-4f4f-9fe0-f8b3e67874f8"

        val placeId = budokanPlaceMusicBrainzModel.id

        // Lookup a country
        val countryAreaRepository = createAreaRepositoryWithFakeNetworkData(
            musicBrainzModel = AreaMusicBrainzNetworkModel(
                id = countryId,
                name = "Japan",
                type = "Country",
                countryCodes = listOf("JP"),
            ),
        )
        countryAreaRepository.lookupArea(
            areaId = countryId,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        )

        // Browse places in the country
        val placesListRepositoryImpl = PlacesListRepositoryImpl(
            browseRemoteMetadataDao = browseRemoteMetadataDao,
            collectionEntityDao = collectionEntityDao,
            placeDao = placeDao,
            aliasDao = aliasDao,
            browseApi = object : FakeBrowseApi() {
                override suspend fun browsePlacesByEntity(
                    entityId: String,
                    entity: MusicBrainzEntityType,
                    limit: Int,
                    offset: Int,
                    include: String,
                ): BrowsePlacesResponse {
                    return BrowsePlacesResponse(
                        count = 1,
                        offset = 0,
                        musicBrainzModels = listOf(
                            budokanPlaceMusicBrainzModel,
                        ),
                    )
                }
            },
        )
        val flow: Flow<PagingData<PlaceListItemModel>> = placesListRepositoryImpl.observePlaces(
            browseMethod = BrowseMethod.ByEntity(
                entityId = countryId,
                entity = MusicBrainzEntityType.AREA,
            ),
        )
        val places = flow.asSnapshot()
        assertEquals(
            listOf(
                PlaceListItemModel(
                    id = placeId,
                    name = "日本武道館",
                    address = "〒102-8321 東京都千代田区北の丸公園2-3",
                    type = "Indoor arena",
                    lifeSpan = LifeSpanUiModel(
                        begin = "1964-10-03",
                        ended = false,
                    ),
                    coordinates = CoordinatesUiModel(
                        longitude = 139.75,
                        latitude = 35.69333,
                    ),
                ),
            ),
            places,
        )

        // Lookup a place whose area is a more specific area in the country
        val placeRepository = createPlaceRepository(
            musicBrainzModel = budokanPlaceMusicBrainzModel,
        )
        var placeDetailsModel = placeRepository.lookupPlace(
            placeId = placeId,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        )
        assertEquals(
            PlaceDetailsModel(
                id = placeId,
                name = "日本武道館",
                address = "〒102-8321 東京都千代田区北の丸公園2-3",
                type = "Indoor arena",
                lifeSpan = LifeSpanUiModel(
                    begin = "1964-10-03",
                    ended = false,
                ),
                coordinates = CoordinatesUiModel(
                    longitude = 139.75,
                    latitude = 35.69333,
                ),
                area = AreaListItemModel(
                    id = districtId,
                    name = "Kitanomaru Kōen",
                    sortName = "Kitanomaru Kōen",
                ),
                lastUpdated = testDateTimeInThePast,
            ),
            placeDetailsModel,
        )
        placeDetailsModel = placeRepository.lookupPlace(
            placeId = placeId,
            forceRefresh = true,
            lastUpdated = testDateTimeInThePast,
        )
        assertEquals(
            PlaceDetailsModel(
                id = placeId,
                name = "日本武道館",
                address = "〒102-8321 東京都千代田区北の丸公園2-3",
                type = "Indoor arena",
                lifeSpan = LifeSpanUiModel(
                    begin = "1964-10-03",
                    ended = false,
                ),
                coordinates = CoordinatesUiModel(
                    longitude = 139.75,
                    latitude = 35.69333,
                ),
                area = AreaListItemModel(
                    id = districtId,
                    name = "Kitanomaru Kōen",
                    sortName = "Kitanomaru Kōen",
                ),
                lastUpdated = testDateTimeInThePast,
            ),
            placeDetailsModel,
        )

        // Lookup the more specific area
        val districtAreaRepository = createAreaRepositoryWithFakeNetworkData(
            musicBrainzModel = AreaMusicBrainzNetworkModel(
                id = districtId,
                name = "Kitanomaru Kōen",
                sortName = "Kitanomaru Kōen",
                type = "District",
            ),
        )
        val district = districtAreaRepository.lookupArea(
            areaId = districtId,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        )
        assertEquals(
            AreaDetailsModel(
                id = districtId,
                name = "Kitanomaru Kōen",
                sortName = "",
                type = "District",
                lastUpdated = testDateTimeInThePast,
            ),
            district,
        )

        // Return back to the place
        placeDetailsModel = placeRepository.lookupPlace(
            placeId = placeId,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        )
        assertEquals(
            PlaceDetailsModel(
                id = placeId,
                name = "日本武道館",
                address = "〒102-8321 東京都千代田区北の丸公園2-3",
                type = "Indoor arena",
                lifeSpan = LifeSpanUiModel(
                    begin = "1964-10-03",
                    ended = false,
                ),
                coordinates = CoordinatesUiModel(
                    longitude = 139.75,
                    latitude = 35.69333,
                ),
                area = AreaListItemModel(
                    id = districtId,
                    name = "Kitanomaru Kōen",
                    sortName = "Kitanomaru Kōen",
                    type = "District",
                    visited = true,
                ),
                lastUpdated = testDateTimeInThePast,
            ),
            placeDetailsModel,
        )
        placeDetailsModel = placeRepository.lookupPlace(
            placeId = placeId,
            forceRefresh = true,
            lastUpdated = testDateTimeInThePast,
        )
        assertEquals(
            PlaceDetailsModel(
                id = placeId,
                name = "日本武道館",
                address = "〒102-8321 東京都千代田区北の丸公園2-3",
                type = "Indoor arena",
                lifeSpan = LifeSpanUiModel(
                    begin = "1964-10-03",
                    ended = false,
                ),
                coordinates = CoordinatesUiModel(
                    longitude = 139.75,
                    latitude = 35.69333,
                ),
                area = AreaListItemModel(
                    id = districtId,
                    name = "Kitanomaru Kōen",
                    sortName = "Kitanomaru Kōen",
                    type = "District",
                    visited = true,
                ),
                lastUpdated = testDateTimeInThePast,
            ),
            placeDetailsModel,
        )
    }
}
