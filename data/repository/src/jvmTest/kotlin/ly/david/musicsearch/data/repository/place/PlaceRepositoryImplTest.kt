package ly.david.musicsearch.data.repository.place

import kotlinx.coroutines.test.runTest
import ly.david.data.test.api.FakeLookupApi
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.AreaPlaceDao
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.data.database.dao.VisitedDao
import ly.david.musicsearch.data.database.dao.PlaceDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.musicbrainz.models.UrlMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.common.LifeSpanMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.CoordinatesMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.PlaceMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.Direction
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.SerializableMusicBrainzEntity
import ly.david.musicsearch.data.repository.KoinTestRule
import ly.david.musicsearch.data.repository.RelationRepositoryImpl
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.place.CoordinatesUiModel
import ly.david.musicsearch.shared.domain.place.PlaceDetailsModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class PlaceRepositoryImplTest : KoinTest {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val entityHasRelationsDao: EntityHasRelationsDao by inject()
    private val visitedDao: VisitedDao by inject()
    private val relationDao: RelationDao by inject()
    private val placeDao: PlaceDao by inject()
    private val areaPlaceDao: AreaPlaceDao by inject()
    private val areaDao: AreaDao by inject()

    private fun createRepositoryWithFakeNetworkData(
        musicBrainzModel: PlaceMusicBrainzModel,
    ): PlaceRepositoryImpl {
        val relationRepository = RelationRepositoryImpl(
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupPlace(
                    placeId: String,
                    include: String?,
                ): PlaceMusicBrainzModel {
                    return musicBrainzModel
                }
            },
            entityHasRelationsDao = entityHasRelationsDao,
            visitedDao = visitedDao,
            relationDao = relationDao,
        )
        return PlaceRepositoryImpl(
            placeDao = placeDao,
            areaPlaceDao = areaPlaceDao,
            areaDao = areaDao,
            relationRepository = relationRepository,
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupPlace(
                    placeId: String,
                    include: String?,
                ): PlaceMusicBrainzModel {
                    return musicBrainzModel
                }
            },
        )
    }

    @Test
    fun `lookup is cached, and force refresh invalidates cache`() = runTest {
        val sparseRepository = createRepositoryWithFakeNetworkData(
            musicBrainzModel = PlaceMusicBrainzModel(
                id = "4d43b9d8-162d-4ac5-8068-dfb009722484",
                name = "日本武道館",
                address = "〒102-8321 東京都千代田区北の丸公園2-3",
            ),
        )
        val sparseDetailsModel = sparseRepository.lookupPlace(
            placeId = "4d43b9d8-162d-4ac5-8068-dfb009722484",
            forceRefresh = false,
        )
        assertEquals(
            PlaceDetailsModel(
                id = "4d43b9d8-162d-4ac5-8068-dfb009722484",
                name = "日本武道館",
                address = "〒102-8321 東京都千代田区北の丸公園2-3",
            ),
            sparseDetailsModel,
        )

        val allDataRepository = createRepositoryWithFakeNetworkData(
            musicBrainzModel = PlaceMusicBrainzModel(
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
                area = AreaMusicBrainzModel(
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
        )
        assertEquals(
            PlaceDetailsModel(
                id = "4d43b9d8-162d-4ac5-8068-dfb009722484",
                name = "日本武道館",
                address = "〒102-8321 東京都千代田区北の丸公園2-3",
            ),
            allDataArtistDetailsModel,
        )
        allDataArtistDetailsModel = allDataRepository.lookupPlace(
            placeId = "4d43b9d8-162d-4ac5-8068-dfb009722484",
            forceRefresh = true,
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
                urls = listOf(
                    RelationListItemModel(
                        id = "e893b81b-a678-4989-858f-c83a30243b7b_0",
                        linkedEntityId = "e893b81b-a678-4989-858f-c83a30243b7b",
                        label = "Discogs",
                        name = "https://www.discogs.com/label/268904",
                        disambiguation = null,
                        attributes = "",
                        additionalInfo = null,
                        linkedEntity = MusicBrainzEntity.URL,
                    ),
                    RelationListItemModel(
                        id = "95cb03e2-8126-417d-82c1-84ef0274b6f8_6",
                        linkedEntityId = "95cb03e2-8126-417d-82c1-84ef0274b6f8",
                        label = "Songkick",
                        name = "https://www.songkick.com/venues/33448",
                        disambiguation = null,
                        attributes = "",
                        additionalInfo = null,
                        linkedEntity = MusicBrainzEntity.URL,
                    ),
                    RelationListItemModel(
                        id = "4674bf4a-5ae8-4154-a223-4c26338622bd_7",
                        linkedEntityId = "4674bf4a-5ae8-4154-a223-4c26338622bd",
                        label = "Wikidata",
                        name = "https://www.wikidata.org/wiki/Q386246",
                        disambiguation = null,
                        attributes = "",
                        additionalInfo = null,
                        linkedEntity = MusicBrainzEntity.URL,
                    ),
                    RelationListItemModel(
                        id = "4adeee43-1b70-4144-8dfb-96713e868b33_2",
                        linkedEntityId = "4adeee43-1b70-4144-8dfb-96713e868b33",
                        label = "official homepages",
                        name = "https://www.nipponbudokan.or.jp/",
                        disambiguation = null,
                        attributes = "",
                        additionalInfo = null,
                        linkedEntity = MusicBrainzEntity.URL,
                    ),
                    RelationListItemModel(
                        id = "ab71b5f3-9c59-4595-abdc-6b047db1eb20_3",
                        linkedEntityId = "ab71b5f3-9c59-4595-abdc-6b047db1eb20",
                        label = "other databases",
                        name = "https://www.generasia.com/wiki/Nippon_Budokan",
                        disambiguation = null,
                        attributes = "",
                        additionalInfo = null,
                        linkedEntity = MusicBrainzEntity.URL,
                    ),
                    RelationListItemModel(
                        id = "860a6bef-cee3-406e-a19e-54938a456926_4",
                        linkedEntityId = "860a6bef-cee3-406e-a19e-54938a456926",
                        label = "other databases",
                        name = "https://www.livefans.jp/venues/4699",
                        disambiguation = null,
                        attributes = "",
                        additionalInfo = null,
                        linkedEntity = MusicBrainzEntity.URL,
                    ),
                    RelationListItemModel(
                        id = "66fa274b-74d5-47c4-85aa-d168fff294ea_1",
                        linkedEntityId = "66fa274b-74d5-47c4-85aa-d168fff294ea",
                        label = "picture",
                        name = "https://commons.wikimedia.org/wiki/File:Nippon_Budokan_2010.jpg",
                        disambiguation = null,
                        attributes = "",
                        additionalInfo = null,
                        linkedEntity = MusicBrainzEntity.URL,
                    ),
                    RelationListItemModel(
                        id = "d1d588f8-abb9-4f12-b421-b2e9a2acf800_5",
                        linkedEntityId = "d1d588f8-abb9-4f12-b421-b2e9a2acf800",
                        label = "setlist.fm",
                        name = "https://www.setlist.fm/venue/nippon-budokan-tokyo-japan-3d61d43.html",
                        disambiguation = null,
                        attributes = "",
                        additionalInfo = null,
                        linkedEntity = MusicBrainzEntity.URL,
                    ),
                ),
            ),
            allDataArtistDetailsModel,
        )
    }
}
