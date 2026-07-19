package ly.david.musicsearch.data.repository.instrument

import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.api.FakeBrowseApi
import ly.david.data.test.fiddleInstrumentListItemModel
import ly.david.data.test.fiddleInstrumentMusicBrainzModel
import ly.david.data.test.violinInstrumentListItemModel
import ly.david.data.test.violinInstrumentMusicBrainzModel
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.InstrumentDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseInstrumentsResponse
import ly.david.musicsearch.data.musicbrainz.models.core.InstrumentMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.helpers.FilterTestCase
import ly.david.musicsearch.data.repository.helpers.FiltersTestCase
import ly.david.musicsearch.data.repository.helpers.testFilter
import ly.david.musicsearch.data.repository.helpers.testFilters
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.instrument.InstrumentsListRepository
import ly.david.musicsearch.shared.domain.list.ListFilters
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.sort.InstrumentSortOption
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class InstrumentsListRepositoryImplTest : KoinTest {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val instrumentDao: InstrumentDao by inject()
    private val collectionDao: CollectionDao by inject()
    private val browseRemoteMetadataDao: BrowseRemoteMetadataDao by inject()
    private val collectionEntityDao: CollectionEntityDao by inject()
    private val aliasDao: AliasDao by inject()

    private fun createRepository(
        instruments: List<InstrumentMusicBrainzNetworkModel>,
    ): InstrumentsListRepository {
        return InstrumentsListRepositoryImpl(
            browseRemoteMetadataDao = browseRemoteMetadataDao,
            collectionEntityDao = collectionEntityDao,
            instrumentDao = instrumentDao,
            aliasDao = aliasDao,
            browseApi = object : FakeBrowseApi() {
                override suspend fun browseInstrumentsByCollection(
                    collectionId: String,
                    limit: Int,
                    offset: Int,
                    include: String,
                ): BrowseInstrumentsResponse {
                    return BrowseInstrumentsResponse(
                        count = 0,
                        offset = 0,
                        musicBrainzModels = instruments,
                    )
                }
            },
        )
    }

    @Test
    fun instrumentsByCollection() = runTest {
        val collectionId = "950cea33-433e-497f-93bb-a05a393a2c02"
        val entity = MusicBrainzEntityType.COLLECTION
        val instruments = listOf(
            fiddleInstrumentMusicBrainzModel,
            violinInstrumentMusicBrainzModel,
        )
        val instrumentsListRepository = createRepository(
            instruments = instruments,
        )

        collectionDao.insertLocal(
            collection = CollectionListItemModel(
                id = collectionId,
                isRemote = false,
                name = "Instruments",
                entity = MusicBrainzEntityType.INSTRUMENT,
            ),
        )
        collectionEntityDao.addAllToCollection(
            collectionId = collectionId,
            entityIds = instruments.map { it.id },
        )

        val browseMethod = BrowseMethod.ByEntity(
            entityId = collectionId,
            entityType = entity,
        )

        testFilter(
            pagingFlowProducer = { query ->
                instrumentsListRepository.observeInstruments(
                    browseMethod = browseMethod,
                    listFilters = ListFilters.Instruments(
                        query = query,
                    ),
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "no filter",
                    query = "",
                    expectedResult = listOf(
                        fiddleInstrumentListItemModel.copy(
                            collected = true,
                        ),
                        violinInstrumentListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
                FilterTestCase(
                    description = "filter by description",
                    query = "bow",
                    expectedResult = listOf(
                        fiddleInstrumentListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
                FilterTestCase(
                    description = "by type",
                    query = "string",
                    expectedResult = listOf(
                        violinInstrumentListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
            ),
        )
    }

    @Test
    fun `all Instruments`() = runTest {
        instrumentsByCollection()

        val instrumentsListRepository = createRepository(
            instruments = listOf(),
        )

        testFilters(
            pagingFlowProducer = { listFilters ->
                instrumentsListRepository.observeInstruments(
                    browseMethod = BrowseMethod.All,
                    listFilters = listFilters,
                )
            },
            testCases = listOf(
                FiltersTestCase(
                    description = "no filter",
                    listFilters = ListFilters.Instruments(),
                    expectedResult = listOf(
                        fiddleInstrumentListItemModel.copy(
                            collected = true,
                        ),
                        violinInstrumentListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
                FiltersTestCase(
                    description = "sort name desc",
                    listFilters = ListFilters.Instruments(
                        sortOption = InstrumentSortOption.NameDescending,
                    ),
                    expectedResult = listOf(
                        violinInstrumentListItemModel.copy(
                            collected = true,
                        ),
                        fiddleInstrumentListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
            ),
        )
    }
}
