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

    private fun setUpLabelsCollection() = runTest {
        val collectionId = "950cea33-433e-497f-93bb-a05a393a2c02"
        val labels = listOf(
            virginMusicLabelMusicBrainzModel,
            elektraLabelMusicBrainzModel,
        )
        val sut = createRepository(
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

        sut.observeLabelsByEntity(
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
                    virginLabelListItemModel,
                    elektraLabelListItemModel,
                ),
                this,
            )
        }

        sut.observeLabelsByEntity(
            entityId = collectionId,
            entity = MusicBrainzEntity.COLLECTION,
            listFilters = ListFilters(
                query = "19",
            ),
        ).asSnapshot().run {
            assertEquals(
                1,
                size,
            )
            assertEquals(
                listOf(
                    elektraLabelListItemModel,
                ),
                this,
            )
        }
    }

    @Test
    fun `labels by collection, filter by label code`() = runTest {
        setUpLabelsCollection()
    }

    private fun setUpJapaneseLabels() = runTest {
        val entityId = japanAreaMusicBrainzModel.id
        val labels = listOf(
            flyingDogLabelMusicBrainzModel,
            virginMusicLabelMusicBrainzModel,
        )
        val sut = createRepository(
            labels = labels,
        )
        sut.observeLabelsByEntity(
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
                    flyingDogLabelListItemModel,
                    virginLabelListItemModel,
                ),
                this,
            )
        }
        sut.observeLabelsByEntity(
            entityId = entityId,
            entity = MusicBrainzEntity.AREA,
            listFilters = ListFilters(
                query = "do",
            ),
        ).asSnapshot().run {
            assertEquals(
                1,
                size,
            )
            assertEquals(
                listOf(flyingDogLabelListItemModel),
                this,
            )
        }
    }

    private fun setUpUKLabels() = runTest {
        val entityId = "8a754a16-0027-3a29-b6d7-2b40ea0481ed"
        val labels = listOf(
            elektraLabelMusicBrainzModel,
            elektraMusicGroupLabelMusicBrainzModel,
        )
        val sut = createRepository(
            labels = labels,
        )
        sut.observeLabelsByEntity(
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
                    elektraLabelListItemModel,
                    elektraMusicGroupLabelListItemModel,
                ),
                this,
            )
        }
        sut.observeLabelsByEntity(
            entityId = entityId,
            entity = MusicBrainzEntity.AREA,
            listFilters = ListFilters(
                query = "hold",
            ),
        ).asSnapshot().run {
            assertEquals(
                1,
                size,
            )
            assertEquals(
                listOf(
                    elektraMusicGroupLabelListItemModel,
                ),
                this,
            )
        }
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
        setUpLabelsCollection()

        val sut = createRepository(
            labels = listOf(),
        )
        sut.observeLabelsByEntity(
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
                    flyingDogLabelListItemModel,
                    virginLabelListItemModel,
                    elektraLabelListItemModel,
                    elektraMusicGroupLabelListItemModel,
                ),
                this,
            )
        }
        sut.observeLabelsByEntity(
            entityId = null,
            entity = null,
            listFilters = ListFilters(
                query = "192",
            ),
        ).asSnapshot().run {
            assertEquals(
                1,
                size,
            )
            assertEquals(
                listOf(
                    elektraLabelListItemModel,
                ),
                this,
            )
        }
    }
}
