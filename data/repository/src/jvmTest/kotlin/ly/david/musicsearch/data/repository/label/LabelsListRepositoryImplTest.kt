package ly.david.musicsearch.data.repository.label

import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.api.FakeBrowseApi
import ly.david.data.test.elektraLabelListItemModel
import ly.david.data.test.elektraLabelMusicBrainzModel
import ly.david.data.test.elektraMusicGroupLabelListItemModel
import ly.david.data.test.elektraMusicGroupLabelMusicBrainzModel
import ly.david.data.test.flyingDogLabelListItemModel
import ly.david.data.test.flyingDogLabelMusicBrainzModel
import ly.david.data.test.japanAreaMusicBrainzModel
import ly.david.data.test.virginLabelListItemModel
import ly.david.data.test.virginMusicLabelMusicBrainzModel
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseLabelsResponse
import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.helpers.FilterTestCase
import ly.david.musicsearch.data.repository.helpers.testFilter
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.label.LabelsListRepository
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class LabelsListRepositoryImplTest : KoinTest {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val labelDao: LabelDao by inject()
    private val collectionDao: CollectionDao by inject()
    private val browseRemoteMetadataDao: BrowseRemoteMetadataDao by inject()
    private val collectionEntityDao: CollectionEntityDao by inject()
    private val aliasDao: AliasDao by inject()

    private fun createRepository(
        labels: List<LabelMusicBrainzNetworkModel>,
    ): LabelsListRepository {
        return LabelsListRepositoryImpl(
            browseRemoteMetadataDao = browseRemoteMetadataDao,
            collectionEntityDao = collectionEntityDao,
            labelDao = labelDao,
            aliasDao = aliasDao,
            browseApi = object : FakeBrowseApi() {
                override suspend fun browseLabelsByEntity(
                    entityId: String,
                    entity: MusicBrainzEntityType,
                    limit: Int,
                    offset: Int,
                    include: String,
                ): BrowseLabelsResponse {
                    return BrowseLabelsResponse(
                        count = 0,
                        offset = 0,
                        musicBrainzModels = labels,
                    )
                }
            },
        )
    }

    @Test
    fun setUpLabelsByCollection() = runTest {
        val collectionId = "950cea33-433e-497f-93bb-a05a393a2c02"
        val entity = MusicBrainzEntityType.COLLECTION
        val labels = listOf(
            virginMusicLabelMusicBrainzModel,
            elektraLabelMusicBrainzModel,
        )
        val labelsListRepository = createRepository(
            labels = labels,
        )

        collectionDao.insertLocal(
            collection = CollectionListItemModel(
                id = collectionId,
                isRemote = false,
                name = "labels",
                entity = MusicBrainzEntityType.LABEL,
            ),
        )
        collectionEntityDao.addAllToCollection(
            collectionId = collectionId,
            entityIds = labels.map { it.id },
        )

        val browseMethod = BrowseMethod.ByEntity(
            entityId = collectionId,
            entity = entity,
        )

        testFilter(
            pagingFlowProducer = { query ->
                labelsListRepository.observeLabels(
                    browseMethod = browseMethod,
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
                        virginLabelListItemModel.copy(
                            collected = true,
                        ),
                        elektraLabelListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
                FilterTestCase(
                    description = "filter by label code",
                    query = "19",
                    expectedResult = listOf(
                        elektraLabelListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
            ),
        )
    }

    @Test
    fun `labels by collection, filter by label code`() = runTest {
        setUpLabelsByCollection()
    }

    @Test
    fun setUpJapaneseLabels() = runTest {
        val entityId = japanAreaMusicBrainzModel.id
        val entity = MusicBrainzEntityType.AREA
        val labels = listOf(
            flyingDogLabelMusicBrainzModel,
            virginMusicLabelMusicBrainzModel,
        )
        val labelsListRepository = createRepository(
            labels = labels,
        )
        val browseMethod = BrowseMethod.ByEntity(
            entityId = entityId,
            entity = entity,
        )
        labelsListRepository.observeLabels(
            browseMethod = browseMethod,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    flyingDogLabelListItemModel,
                    virginLabelListItemModel,
                ),
                this,
            )
        }
        labelsListRepository.observeLabels(
            browseMethod = browseMethod,
            listFilters = ListFilters(
                query = "do",
            ),
        ).asSnapshot().run {
            assertEquals(
                listOf(flyingDogLabelListItemModel),
                this,
            )
        }
    }

    @Test
    fun setUpUKLabels() = runTest {
        val entityId = "8a754a16-0027-3a29-b6d7-2b40ea0481ed"
        val entity = MusicBrainzEntityType.AREA
        val labels = listOf(
            elektraLabelMusicBrainzModel,
            elektraMusicGroupLabelMusicBrainzModel,
        )
        val labelsListRepository = createRepository(
            labels = labels,
        )
        val browseMethod = BrowseMethod.ByEntity(
            entityId = entityId,
            entity = entity,
        )
        testFilter(
            pagingFlowProducer = { query ->
                labelsListRepository.observeLabels(
                    browseMethod = browseMethod,
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
                        elektraLabelListItemModel,
                        elektraMusicGroupLabelListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by type",
                    query = "Hold",
                    expectedResult = listOf(
                        elektraMusicGroupLabelListItemModel,
                    ),
                ),
            ),
        )
    }

    private fun setUpLabels() = runTest {
        setUpJapaneseLabels()
        setUpUKLabels()
    }

    @Test
    fun `labels by entity (area)`() = runTest {
        setUpLabels()
    }

    @Test
    fun `all labels`() = runTest {
        setUpLabels()
        setUpLabelsByCollection()

        val labelsListRepository = createRepository(
            labels = listOf(),
        )

        testFilter(
            pagingFlowProducer = { query ->
                labelsListRepository.observeLabels(
                    browseMethod = BrowseMethod.All,
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
                        flyingDogLabelListItemModel,
                        virginLabelListItemModel.copy(
                            collected = true,
                        ),
                        elektraLabelListItemModel.copy(
                            collected = true,
                        ),
                        elektraMusicGroupLabelListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by label code",
                    query = "19",
                    expectedResult = listOf(
                        elektraLabelListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
            ),
        )
    }

    // TODO: test deleting labels that belong to an area and collection after merging collection entity table
}
