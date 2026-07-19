package ly.david.musicsearch.data.repository.series

import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.api.FakeBrowseApi
import ly.david.data.test.comiketSeriesListItemModel
import ly.david.data.test.comiketSeriesMusicBrainzModel
import ly.david.data.test.worksOfBeethovenSeriesListItemModel
import ly.david.data.test.worksOfBeethovenSeriesMusicBrainzModel
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.SeriesDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseSeriesResponse
import ly.david.musicsearch.data.musicbrainz.models.core.SeriesMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.helpers.FilterTestCase
import ly.david.musicsearch.data.repository.helpers.FiltersTestCase
import ly.david.musicsearch.data.repository.helpers.testFilter
import ly.david.musicsearch.data.repository.helpers.testFilters
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.list.ListFilters
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.series.SeriesListRepository
import ly.david.musicsearch.shared.domain.sort.SeriesSortOption
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class SeriesListRepositoryImplTest : KoinTest {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val seriesDao: SeriesDao by inject()
    private val collectionDao: CollectionDao by inject()
    private val browseRemoteMetadataDao: BrowseRemoteMetadataDao by inject()
    private val collectionEntityDao: CollectionEntityDao by inject()
    private val aliasDao: AliasDao by inject()

    private fun createRepository(
        seriess: List<SeriesMusicBrainzNetworkModel>,
    ): SeriesListRepository {
        return SeriesListRepositoryImpl(
            browseRemoteMetadataDao = browseRemoteMetadataDao,
            collectionEntityDao = collectionEntityDao,
            seriesDao = seriesDao,
            aliasDao = aliasDao,
            browseApi = object : FakeBrowseApi() {
                override suspend fun browseSeriesByCollection(
                    collectionId: String,
                    limit: Int,
                    offset: Int,
                    include: String,
                ): BrowseSeriesResponse {
                    return BrowseSeriesResponse(
                        count = 0,
                        offset = 0,
                        musicBrainzModels = seriess,
                    )
                }
            },
        )
    }

    @Test
    fun seriessByCollection() = runTest {
        val collectionId = "950cea33-433e-497f-93bb-a05a393a2c02"
        val entity = MusicBrainzEntityType.COLLECTION
        val seriess = listOf(
            worksOfBeethovenSeriesMusicBrainzModel,
            comiketSeriesMusicBrainzModel,
        )
        val seriessListRepository = createRepository(
            seriess = seriess,
        )

        collectionDao.insertLocal(
            collection = CollectionListItemModel(
                id = collectionId,
                isRemote = false,
                name = "Series",
                entity = MusicBrainzEntityType.INSTRUMENT,
            ),
        )
        collectionEntityDao.addAllToCollection(
            collectionId = collectionId,
            entityIds = seriess.map { it.id },
        )

        val browseMethod = BrowseMethod.ByEntity(
            entityId = collectionId,
            entityType = entity,
        )

        testFilter(
            pagingFlowProducer = { query ->
                seriessListRepository.observeSeries(
                    browseMethod = browseMethod,
                    listFilters = ListFilters.Series(
                        query = query,
                    ),
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "no filter",
                    query = "",
                    expectedResult = listOf(
                        worksOfBeethovenSeriesListItemModel.copy(
                            collected = true,
                        ),
                        comiketSeriesListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
                FilterTestCase(
                    description = "by type",
                    query = "cat",
                    expectedResult = listOf(
                        worksOfBeethovenSeriesListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
                FilterTestCase(
                    description = "by alias",
                    query = "comic",
                    expectedResult = listOf(
                        comiketSeriesListItemModel.copy(
                            collected = true,
                            // JP alias filtered out
                            aliases = persistentListOf(
                                BasicAlias(
                                    name = "Comic Market",
                                    isPrimary = true,
                                    locale = "en",
                                ),
                            )
                        ),
                    ),
                ),
                FilterTestCase(
                    description = "by name",
                    query = "コ",
                    expectedResult = listOf(
                        comiketSeriesListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
            ),
        )
    }

    @Test
    fun `all Series`() = runTest {
        seriessByCollection()

        val seriessListRepository = createRepository(
            seriess = listOf(),
        )

        testFilters(
            pagingFlowProducer = { listFilters ->
                seriessListRepository.observeSeries(
                    browseMethod = BrowseMethod.All,
                    listFilters = listFilters,
                )
            },
            testCases = listOf(
                FiltersTestCase(
                    description = "no filter",
                    listFilters = ListFilters.Series(),
                    expectedResult = listOf(
                        worksOfBeethovenSeriesListItemModel.copy(
                            collected = true,
                        ),
                        comiketSeriesListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
                FiltersTestCase(
                    description = "sort name desc",
                    listFilters = ListFilters.Series(
                        sortOption = SeriesSortOption.NameDescending,
                    ),
                    expectedResult = listOf(
                        comiketSeriesListItemModel.copy(
                            collected = true,
                        ),
                        worksOfBeethovenSeriesListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
            ),
        )
    }
}
