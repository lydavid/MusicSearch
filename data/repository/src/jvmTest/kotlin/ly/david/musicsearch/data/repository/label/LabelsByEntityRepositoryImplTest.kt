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
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.LabelDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseLabelsResponse
import ly.david.musicsearch.data.musicbrainz.models.core.LabelMusicBrainzModel
import ly.david.musicsearch.data.repository.helpers.FilterTestCase
import ly.david.musicsearch.data.repository.helpers.testFilter
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.label.LabelsByEntityRepository
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class LabelsByEntityRepositoryImplTest : KoinTest {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val labelDao: LabelDao by inject()
    private val collectionDao: CollectionDao by inject()
    private val browseEntityCountDao: BrowseEntityCountDao by inject()
    private val collectionEntityDao: CollectionEntityDao by inject()

    private fun createRepository(
        labels: List<LabelMusicBrainzModel>,
    ): LabelsByEntityRepository {
        return LabelsByEntityRepositoryImpl(
            browseEntityCountDao = browseEntityCountDao,
            collectionEntityDao = collectionEntityDao,
            labelDao = labelDao,
            browseApi = object : FakeBrowseApi() {
                override suspend fun browseLabelsByEntity(
                    entityId: String,
                    entity: MusicBrainzEntity,
                    limit: Int,
                    offset: Int,
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
        val entity = MusicBrainzEntity.COLLECTION
        val labels = listOf(
            virginMusicLabelMusicBrainzModel,
            elektraLabelMusicBrainzModel,
        )
        val labelsByEntityRepository = createRepository(
            labels = labels,
        )

        collectionDao.insertLocal(
            collection = CollectionListItemModel(
                id = collectionId,
                isRemote = false,
                name = "labels",
                entity = MusicBrainzEntity.LABEL,
            ),
        )
        collectionEntityDao.insertAll(
            collectionId = collectionId,
            entityIds = labels.map { it.id },
        )

        testFilter(
            pagingFlowProducer = { query ->
                labelsByEntityRepository.observeLabelsByEntity(
                    entityId = collectionId,
                    entity = entity,
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
                        virginLabelListItemModel,
                        elektraLabelListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by label code",
                    query = "19",
                    expectedResult = listOf(
                        elektraLabelListItemModel,
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
        val labels = listOf(
            flyingDogLabelMusicBrainzModel,
            virginMusicLabelMusicBrainzModel,
        )
        val labelsByEntityRepository = createRepository(
            labels = labels,
        )
        labelsByEntityRepository.observeLabelsByEntity(
            entityId = entityId,
            entity = MusicBrainzEntity.AREA,
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
        labelsByEntityRepository.observeLabelsByEntity(
            entityId = entityId,
            entity = MusicBrainzEntity.AREA,
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
        val entity = MusicBrainzEntity.AREA
        val labels = listOf(
            elektraLabelMusicBrainzModel,
            elektraMusicGroupLabelMusicBrainzModel,
        )
        val labelsByEntityRepository = createRepository(
            labels = labels,
        )

        testFilter(
            pagingFlowProducer = { query ->
                labelsByEntityRepository.observeLabelsByEntity(
                    entityId = entityId,
                    entity = entity,
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

        val labelsByEntityRepository = createRepository(
            labels = listOf(),
        )

        testFilter(
            pagingFlowProducer = { query ->
                labelsByEntityRepository.observeLabelsByEntity(
                    entityId = null,
                    entity = null,
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
                        virginLabelListItemModel,
                        elektraLabelListItemModel,
                        elektraMusicGroupLabelListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by label code",
                    query = "19",
                    expectedResult = listOf(
                        elektraLabelListItemModel,
                    ),
                ),
            ),
        )
    }
}
