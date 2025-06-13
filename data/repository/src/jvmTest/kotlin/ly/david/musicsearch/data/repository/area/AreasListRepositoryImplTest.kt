package ly.david.musicsearch.data.repository.area

import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.api.FakeBrowseApi
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.AreaDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseAreasResponse
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzNetworkModel
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.area.AreasListRepository
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class AreasListRepositoryImplTest : KoinTest {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val areaDao: AreaDao by inject()
    private val collectionDao: CollectionDao by inject()
    private val browseRemoteMetadataDao: BrowseRemoteMetadataDao by inject()
    private val collectionEntityDao: CollectionEntityDao by inject()
    private val aliasDao: AliasDao by inject()

    private fun createRepository(
        areas: List<AreaMusicBrainzNetworkModel>,
    ): AreasListRepository {
        return AreasListRepositoryImpl(
            browseRemoteMetadataDao = browseRemoteMetadataDao,
            collectionEntityDao = collectionEntityDao,
            areaDao = areaDao,
            aliasDao = aliasDao,
            browseApi = object : FakeBrowseApi() {
                override suspend fun browseAreasByCollection(
                    collectionId: String,
                    limit: Int,
                    offset: Int,
                    include: String,
                ): BrowseAreasResponse {
                    return BrowseAreasResponse(
                        count = 1,
                        offset = 0,
                        musicBrainzModels = areas,
                    )
                }
            },
        )
    }

    @Test
    fun `areas by collection, filter by type`() = runTest {
        val collectionId = "950cea33-433e-497f-93bb-a05a393a2c02"
        val areas = listOf(
            AreaMusicBrainzNetworkModel(
                id = "01428650-fbe2-4aab-aca2-a1d562a93caf",
                name = "Ireland",
                type = "Island",
                typeId = "06dd0ae4-8c74-30bb-b43d-95dcedf961de",
            ),
            AreaMusicBrainzNetworkModel(
                id = "390b05d4-11ec-3bce-a343-703a366b34a5",
                name = "Ireland",
                countryCodes = listOf("IE"),
                type = "Country",
                typeId = "06dd0ae4-8c74-30bb-b43d-95dcedf961de",
            ),
            AreaMusicBrainzNetworkModel(
                id = "99c3f001-64d3-4174-a302-fb14204117af",
                name = "Connaught",
                type = "Subdivision",
                typeId = "fd3d44c5-80a1-3842-9745-2c4972d35afa",
            ),
            AreaMusicBrainzNetworkModel(
                id = "3fca5006-e6c6-4935-b1f5-baa80df5a95c",
                name = "County Galway",
                type = "Subdivision",
                typeId = "fd3d44c5-80a1-3842-9745-2c4972d35afa",
            ),
            AreaMusicBrainzNetworkModel(
                id = "db52f295-91e6-41b0-8491-a3356cc1f815",
                name = "Galway",
                type = "City",
                typeId = "6fd8f29a-3d0a-32fc-980d-ea697b69da78",
            ),
            AreaMusicBrainzNetworkModel(
                id = "08ead5f6-a425-4a72-ac98-8a925d2fbd0d",
                name = "Salthill",
                type = "District",
                typeId = "84039871-5e47-38ca-a66a-45e512c8290f",
            ),
        )
        val sut = createRepository(
            areas = areas,
        )

        collectionDao.insertLocal(
            collection = CollectionListItemModel(
                id = collectionId,
                isRemote = false,
                name = "Areas I've been",
                entity = MusicBrainzEntity.AREA,
            ),
        )
        collectionEntityDao.insertAll(
            collectionId = collectionId,
            entityIds = areas.map { it.id },
        )

        val browseMethod = BrowseMethod.ByEntity(
            entityId = collectionId,
            entity = MusicBrainzEntity.COLLECTION,
        )

        sut.observeAreas(
            browseMethod = browseMethod,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    AreaListItemModel(
                        id = "99c3f001-64d3-4174-a302-fb14204117af",
                        name = "Connaught",
                        type = "Subdivision",
                    ),
                    AreaListItemModel(
                        id = "3fca5006-e6c6-4935-b1f5-baa80df5a95c",
                        name = "County Galway",
                        type = "Subdivision",
                    ),
                    AreaListItemModel(
                        id = "db52f295-91e6-41b0-8491-a3356cc1f815",
                        name = "Galway",
                        type = "City",
                    ),
                    AreaListItemModel(
                        id = "01428650-fbe2-4aab-aca2-a1d562a93caf",
                        name = "Ireland",
                        type = "Island",
                    ),
                    AreaListItemModel(
                        id = "390b05d4-11ec-3bce-a343-703a366b34a5",
                        name = "Ireland",
                        countryCodes = listOf("IE"),
                        type = "Country",
                    ),
                    AreaListItemModel(
                        id = "08ead5f6-a425-4a72-ac98-8a925d2fbd0d",
                        name = "Salthill",
                        type = "District",
                    ),
                ),
                this,
            )
        }

        sut.observeAreas(
            browseMethod = browseMethod,
            listFilters = ListFilters(
                query = "di",
            ),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    AreaListItemModel(
                        id = "99c3f001-64d3-4174-a302-fb14204117af",
                        name = "Connaught",
                        type = "Subdivision",
                    ),
                    AreaListItemModel(
                        id = "3fca5006-e6c6-4935-b1f5-baa80df5a95c",
                        name = "County Galway",
                        type = "Subdivision",
                    ),
                    AreaListItemModel(
                        id = "08ead5f6-a425-4a72-ac98-8a925d2fbd0d",
                        name = "Salthill",
                        type = "District",
                    ),
                ),
                this,
            )
        }
    }
}
