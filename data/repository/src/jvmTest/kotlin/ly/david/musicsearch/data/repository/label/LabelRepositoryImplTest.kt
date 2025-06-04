package ly.david.musicsearch.data.repository.label

import kotlinx.coroutines.test.runTest
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.musicbrainz.models.UrlMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.common.LifeSpanMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.AreaMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.relation.Direction
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.SerializableMusicBrainzEntity
import ly.david.data.test.KoinTestRule
import ly.david.musicsearch.data.repository.helpers.TestLabelRepository
import ly.david.musicsearch.shared.domain.LifeSpanUiModel
import ly.david.musicsearch.shared.domain.label.LabelDetailsModel
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class LabelRepositoryImplTest : KoinTest, TestLabelRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    override val entityHasRelationsDao: EntityHasRelationsDao by inject()
    override val visitedDao: DetailsMetadataDao by inject()
    override val relationDao: RelationDao by inject()
    override val labelDao: LabelDao by inject()

    @Test
    fun `lookup is cached, and force refresh invalidates cache`() = runTest {
        val sparseRepository = createLabelRepository(
            musicBrainzModel = LabelMusicBrainzNetworkModel(
                id = "7aaa37fe-2def-3476-b359-80245850062d",
                name = "UNIVERSAL J",
            ),
        )
        val sparseDetailsModel = sparseRepository.lookupLabel(
            labelId = "7aaa37fe-2def-3476-b359-80245850062d",
            forceRefresh = false,
        )
        assertEquals(
            LabelDetailsModel(
                id = "7aaa37fe-2def-3476-b359-80245850062d",
                name = "UNIVERSAL J",
            ),
            sparseDetailsModel,
        )

        val allDataRepository = createLabelRepository(
            musicBrainzModel = LabelMusicBrainzNetworkModel(
                id = "7aaa37fe-2def-3476-b359-80245850062d",
                name = "UNIVERSAL J",
                disambiguation = "Japanese label division, 2002–2022",
                type = "Original Production",
                typeId = "7aaa37fe-2def-3476-b359-80245850062d",
                lifeSpan = LifeSpanMusicBrainzModel(
                    begin = "2002-06",
                    end = "2023-02",
                    ended = true,
                ),
                area = AreaMusicBrainzNetworkModel(
                    id = "2db42837-c832-3c27-b4a3-08198f75693c",
                    name = "Japan",
                    sortName = "Japan",
                    countryCodes = listOf("JP"),
                ),
                relations = listOf(
                    RelationMusicBrainzModel(
                        type = "discogs",
                        typeId = "5b987f87-25bc-4a2d-b3f1-3618795b8207",
                        direction = Direction.FORWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            resource = "https://www.discogs.com/label/57296",
                            id = "2741a789-1c8a-453a-a8d3-fb8a5cc6f090",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "social network",
                        typeId = "5d217d99-bc05-4a76-836d-c91eec4ba818",
                        direction = Direction.FORWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            resource = "https://twitter.com/UNIVERSAL_J",
                            id = "f58627cb-4d36-475e-a11b-82cd4be31c13",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "vgmdb",
                        typeId = "8a2d3e55-d291-4b99-87a0-c59c6b121762",
                        direction = Direction.FORWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            resource = "https://vgmdb.net/org/2391",
                            id = "ff7c8fd7-b8d4-4b79-b1f7-b562210ade1a",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "vgmdb",
                        typeId = "8a2d3e55-d291-4b99-87a0-c59c6b121762",
                        direction = Direction.FORWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            resource = "https://vgmdb.net/org/533",
                            id = "44117bb6-9193-402b-a216-16b34555ca08",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "wikidata",
                        typeId = "75d87e83-d927-4580-ba63-44dc76256f98",
                        direction = Direction.FORWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            resource = "https://www.wikidata.org/wiki/Q112654575",
                            id = "24284c01-4cbd-488f-a042-f15dbc852633",
                        ),
                    ),
                ),
            ),
        )
        var allDataArtistDetailsModel = allDataRepository.lookupLabel(
            labelId = "7aaa37fe-2def-3476-b359-80245850062d",
            forceRefresh = false,
        )
        assertEquals(
            LabelDetailsModel(
                id = "7aaa37fe-2def-3476-b359-80245850062d",
                name = "UNIVERSAL J",
            ),
            allDataArtistDetailsModel,
        )
        allDataArtistDetailsModel = allDataRepository.lookupLabel(
            labelId = "7aaa37fe-2def-3476-b359-80245850062d",
            forceRefresh = true,
        )
        assertEquals(
            LabelDetailsModel(
                id = "7aaa37fe-2def-3476-b359-80245850062d",
                name = "UNIVERSAL J",
                disambiguation = "Japanese label division, 2002–2022",
                type = "Original Production",
                lifeSpan = LifeSpanUiModel(
                    begin = "2002-06",
                    end = "2023-02",
                    ended = true,
                ),
                urls = listOf(
                    RelationListItemModel(
                        id = "2741a789-1c8a-453a-a8d3-fb8a5cc6f090_5",
                        linkedEntityId = "2741a789-1c8a-453a-a8d3-fb8a5cc6f090",
                        label = "Discogs",
                        name = "https://www.discogs.com/label/57296",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntity.URL,
                        visited = true,
                        isForwardDirection = true,
                    ),
                    RelationListItemModel(
                        id = "ff7c8fd7-b8d4-4b79-b1f7-b562210ade1a_7",
                        linkedEntityId = "ff7c8fd7-b8d4-4b79-b1f7-b562210ade1a",
                        label = "VGMdb",
                        name = "https://vgmdb.net/org/2391",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntity.URL,
                        visited = true,
                        isForwardDirection = true,
                    ),
                    RelationListItemModel(
                        id = "44117bb6-9193-402b-a216-16b34555ca08_8",
                        linkedEntityId = "44117bb6-9193-402b-a216-16b34555ca08",
                        label = "VGMdb",
                        name = "https://vgmdb.net/org/533",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntity.URL,
                        visited = true,
                        isForwardDirection = true,
                    ),
                    RelationListItemModel(
                        id = "24284c01-4cbd-488f-a042-f15dbc852633_9",
                        linkedEntityId = "24284c01-4cbd-488f-a042-f15dbc852633",
                        label = "Wikidata",
                        name = "https://www.wikidata.org/wiki/Q112654575",
                        disambiguation = null,
                        attributes = "",
                        linkedEntity = MusicBrainzEntity.URL,
                        visited = true,
                        isForwardDirection = true,
                    ),
                    RelationListItemModel(
                        id = "f58627cb-4d36-475e-a11b-82cd4be31c13_6",
                        linkedEntityId = "f58627cb-4d36-475e-a11b-82cd4be31c13",
                        label = "social networking",
                        name = "https://twitter.com/UNIVERSAL_J",
                        disambiguation = null,
                        attributes = "",
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
