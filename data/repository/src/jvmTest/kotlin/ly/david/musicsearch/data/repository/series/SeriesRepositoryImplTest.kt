package ly.david.musicsearch.data.repository.series

import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.test.runTest
import ly.david.data.test.api.FakeLookupApi
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.SeriesDao
import ly.david.musicsearch.data.musicbrainz.models.UrlMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.SeriesMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.AttributeValue
import ly.david.musicsearch.data.musicbrainz.models.relation.Direction
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.SerializableMusicBrainzEntity
import ly.david.musicsearch.data.repository.KoinTestRule
import ly.david.musicsearch.data.repository.RelationRepositoryImpl
import ly.david.musicsearch.shared.domain.history.VisitedDao
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.series.SeriesDetailsModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class SeriesRepositoryImplTest : KoinTest {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val entityHasRelationsDao: EntityHasRelationsDao by inject()
    private val visitedDao: VisitedDao by inject()
    private val relationDao: RelationDao by inject()
    private val seriesDao: SeriesDao by inject()

    private fun createFakeRelationRepository(musicBrainzModel: SeriesMusicBrainzModel): RelationRepositoryImpl {
        val relationRepository = RelationRepositoryImpl(
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupSeries(
                    seriesId: String,
                    include: String?,
                ): SeriesMusicBrainzModel {
                    return musicBrainzModel
                }
            },
            entityHasRelationsDao = entityHasRelationsDao,
            visitedDao = visitedDao,
            relationDao = relationDao,
        )
        return relationRepository
    }

    private fun createRepositoryWithFakeNetworkData(
        musicBrainzModel: SeriesMusicBrainzModel,
    ): SeriesRepositoryImpl {
        val relationRepository = createFakeRelationRepository(musicBrainzModel)
        return SeriesRepositoryImpl(
            seriesDao = seriesDao,
            relationRepository = relationRepository,
            lookupApi = object : FakeLookupApi() {
                override suspend fun lookupSeries(
                    seriesId: String,
                    include: String?,
                ): SeriesMusicBrainzModel {
                    return musicBrainzModel
                }
            },
        )
    }

    @Test
    fun `lookup is cached, and force refresh invalidates cache`() = runTest {
        val sparseRepository = createRepositoryWithFakeNetworkData(
            musicBrainzModel = SeriesMusicBrainzModel(
                id = "bb3d9d84-75b8-4e67-8ad7-dcc38f764bf3",
                name = "Rolling Stone: 500 Greatest Albums of All Time: 2023 edition",
            ),
        )
        val sparseDetailsModel = sparseRepository.lookupSeries(
            seriesId = "bb3d9d84-75b8-4e67-8ad7-dcc38f764bf3",
            forceRefresh = false,
        )
        assertEquals(
            SeriesDetailsModel(
                id = "bb3d9d84-75b8-4e67-8ad7-dcc38f764bf3",
                name = "Rolling Stone: 500 Greatest Albums of All Time: 2023 edition",
            ),
            sparseDetailsModel,
        )

        val allDataRepository = createRepositoryWithFakeNetworkData(
            musicBrainzModel = SeriesMusicBrainzModel(
                id = "bb3d9d84-75b8-4e67-8ad7-dcc38f764bf3",
                name = "Rolling Stone: 500 Greatest Albums of All Time: 2023 edition",
                type = "Release group series",
                relations = listOf(
                    RelationMusicBrainzModel(
                        type = "official homepage",
                        typeId = "b79eb9a5-46df-492d-b107-1f1fea71b0eb",
                        direction = Direction.FORWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            id = "e4a5db48-cae3-404f-921d-0f1c3947f874",
                            resource = "https://www.rollingstone.com/music/music-lists/best-albums-of-all-time-1062063/",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "wikidata",
                        typeId = "a1eecd98-f2f2-420b-ba8e-e5bc61697869",
                        direction = Direction.FORWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            id = "61036cd9-8819-4f56-8739-d7f9bd16d675",
                            resource = "https://www.wikidata.org/wiki/Q240550",
                        ),
                    ),
                ),
            ),
        )
        var allDataDetailsModel = allDataRepository.lookupSeries(
            seriesId = "bb3d9d84-75b8-4e67-8ad7-dcc38f764bf3",
            forceRefresh = false,
        )
        assertEquals(
            SeriesDetailsModel(
                id = "bb3d9d84-75b8-4e67-8ad7-dcc38f764bf3",
                name = "Rolling Stone: 500 Greatest Albums of All Time: 2023 edition",
            ),
            allDataDetailsModel,
        )
        allDataDetailsModel = allDataRepository.lookupSeries(
            seriesId = "bb3d9d84-75b8-4e67-8ad7-dcc38f764bf3",
            forceRefresh = true,
        )
        assertEquals(
            SeriesDetailsModel(
                id = "bb3d9d84-75b8-4e67-8ad7-dcc38f764bf3",
                name = "Rolling Stone: 500 Greatest Albums of All Time: 2023 edition",
                type = "Release group series",
                urls = listOf(
                    RelationListItemModel(
                        id = "61036cd9-8819-4f56-8739-d7f9bd16d675_3",
                        linkedEntityId = "61036cd9-8819-4f56-8739-d7f9bd16d675",
                        label = "Wikidata",
                        name = "https://www.wikidata.org/wiki/Q240550",
                        disambiguation = null,
                        attributes = "",
                        additionalInfo = null,
                        linkedEntity = MusicBrainzEntity.URL,
                        visited = true,
                        isForwardDirection = true,
                    ),
                    RelationListItemModel(
                        id = "e4a5db48-cae3-404f-921d-0f1c3947f874_2",
                        linkedEntityId = "e4a5db48-cae3-404f-921d-0f1c3947f874",
                        label = "official homepages",
                        name = "https://www.rollingstone.com/music/music-lists/best-albums-of-all-time-1062063/",
                        disambiguation = null,
                        attributes = "",
                        additionalInfo = null,
                        linkedEntity = MusicBrainzEntity.URL,
                        visited = true,
                        isForwardDirection = true,
                    ),
                ),
            ),
            allDataDetailsModel,
        )
    }

    @Test
    fun `is ordered by ordering-key if it exists`() = runTest {
        val seriesRepositoryImpl = createFakeRelationRepository(
            musicBrainzModel = SeriesMusicBrainzModel(
                id = "eca82a1b-1efa-4d6b-9278-e278523267f8",
                name = "東方Project",
                type = "Release group series",
                relations = listOf(
                    RelationMusicBrainzModel(
                        type = "publishes series",
                        typeId = "1cd0342c-69a1-4f97-8471-46748f8ecde1",
                        direction = Direction.BACKWARD,
                        targetType = SerializableMusicBrainzEntity.LABEL,
                        label = LabelMusicBrainzModel(
                            id = "bad6d0fa-938e-45a2-95fd-b37ea37b783c",
                            name = "上海アリス幻樂団",
                            type = "Original Production",
                            disambiguation = "dōjin game developer",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "publishes series",
                        typeId = "1cd0342c-69a1-4f97-8471-46748f8ecde1",
                        direction = Direction.BACKWARD,
                        targetType = SerializableMusicBrainzEntity.LABEL,
                        label = LabelMusicBrainzModel(
                            id = "bad6d0fa-938e-45a2-95fd-b37ea37b783c",
                            name = "黄昏フロンティア",
                            type = "Original Production",
                            disambiguation = "dōjin game developer",
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "part of",
                        typeId = "01018437-91d8-36b9-bf89-3f885d53b5bd",
                        direction = Direction.BACKWARD,
                        targetType = SerializableMusicBrainzEntity.RELEASE_GROUP,
                        releaseGroup = ReleaseGroupMusicBrainzModel(
                            id = "b22e3f3e-6c90-3df9-915f-12d8f86c240b",
                            name = "東方封魔録 〜 Story of Eastern Wonderland",
                            primaryType = null,
                            primaryTypeId = null,
                            secondaryTypes = emptyList(),
                            secondaryTypeIds = emptyList(),
                            firstReleaseDate = "1997-08-15",
                            disambiguation = "",
                        ),
                        attributes = listOf("number"),
                        attributeValues = AttributeValue(
                            number = "2",
                        ),
                        orderingKey = 2,
                    ),
                    RelationMusicBrainzModel(
                        type = "part of",
                        typeId = "01018437-91d8-36b9-bf89-3f885d53b5bd",
                        direction = Direction.BACKWARD,
                        targetType = SerializableMusicBrainzEntity.RELEASE_GROUP,
                        releaseGroup = ReleaseGroupMusicBrainzModel(
                            id = "5d286f5b-7cc3-3f78-b1cf-a24d496af34b",
                            name = "東方靈異伝 〜 Highly Responsive to Prayers",
                            primaryType = null,
                            primaryTypeId = null,
                            secondaryTypes = emptyList(),
                            secondaryTypeIds = emptyList(),
                            firstReleaseDate = "1996-11",
                            disambiguation = "",
                        ),
                        attributes = listOf("number"),
                        attributeValues = AttributeValue(
                            number = "1",
                        ),
                        orderingKey = 1,
                    ),
                    RelationMusicBrainzModel(
                        type = "subseries",
                        typeId = "a3af4c16-de83-4d63-b9b8-77e074c9babe",
                        direction = Direction.FORWARD,
                        targetType = SerializableMusicBrainzEntity.SERIES,
                        series = SeriesMusicBrainzModel(
                            id = "fbca86fc-1509-40d6-b985-f50e45796187",
                            name = "ZUN's Music Collection",
                            type = "Release group series",
                            typeId = "4c1c4949-7b6c-3a2d-9d54-a50a27e4fa77",
                        ),
                    ),
                ),
            ),
        )

        val flow = seriesRepositoryImpl.observeEntityRelationshipsExcludingUrls(
            entity = MusicBrainzEntity.SERIES,
            entityId = "eca82a1b-1efa-4d6b-9278-e278523267f8",
            query = "",
        )
        val seriesRelationships = flow.asSnapshot()
        assertEquals(
            listOf(
                RelationListItemModel(
                    id = "bad6d0fa-938e-45a2-95fd-b37ea37b783c_5",
                    linkedEntityId = "bad6d0fa-938e-45a2-95fd-b37ea37b783c",
                    label = "publishing label",
                    name = "上海アリス幻樂団",
                    disambiguation = "dōjin game developer",
                    attributes = "",
                    additionalInfo = null,
                    linkedEntity = MusicBrainzEntity.LABEL,
                    visited = false,
                    isForwardDirection = false,
                ),
                RelationListItemModel(
                    id = "bad6d0fa-938e-45a2-95fd-b37ea37b783c_6",
                    linkedEntityId = "bad6d0fa-938e-45a2-95fd-b37ea37b783c",
                    label = "publishing label",
                    name = "黄昏フロンティア",
                    disambiguation = "dōjin game developer",
                    attributes = "",
                    additionalInfo = null,
                    linkedEntity = MusicBrainzEntity.LABEL,
                    visited = false,
                    isForwardDirection = false,
                ),
                RelationListItemModel(
                    id = "5d286f5b-7cc3-3f78-b1cf-a24d496af34b_1",
                    linkedEntityId = "5d286f5b-7cc3-3f78-b1cf-a24d496af34b",
                    label = "has parts",
                    name = "東方靈異伝 〜 Highly Responsive to Prayers",
                    disambiguation = "",
                    attributes = "number: 1",
                    additionalInfo = "",
                    linkedEntity = MusicBrainzEntity.RELEASE_GROUP,
                    visited = false,
                    isForwardDirection = false,
                ),
                RelationListItemModel(
                    id = "b22e3f3e-6c90-3df9-915f-12d8f86c240b_2",
                    linkedEntityId = "b22e3f3e-6c90-3df9-915f-12d8f86c240b",
                    label = "has parts",
                    name = "東方封魔録 〜 Story of Eastern Wonderland",
                    disambiguation = "",
                    attributes = "number: 2",
                    additionalInfo = "",
                    linkedEntity = MusicBrainzEntity.RELEASE_GROUP,
                    visited = false,
                    isForwardDirection = false,
                ),
                RelationListItemModel(
                    id = "fbca86fc-1509-40d6-b985-f50e45796187_9",
                    linkedEntityId = "fbca86fc-1509-40d6-b985-f50e45796187",
                    label = "subseries",
                    name = "ZUN's Music Collection",
                    disambiguation = null,
                    attributes = "",
                    additionalInfo = null,
                    linkedEntity = MusicBrainzEntity.SERIES,
                    visited = false,
                    isForwardDirection = true,
                ),
            ),
            seriesRelationships,
        )
    }
}
