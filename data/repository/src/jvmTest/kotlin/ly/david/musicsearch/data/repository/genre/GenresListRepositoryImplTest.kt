package ly.david.musicsearch.data.repository.genre

import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.api.FakeBrowseApi
import ly.david.data.test.jPopGenreListItemModel
import ly.david.data.test.jPopGenreMusicBrainzModel
import ly.david.data.test.mathRockGenreListItemModel
import ly.david.data.test.mathRockGenreMusicBrainzModel
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.GenreDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseGenresResponse
import ly.david.musicsearch.data.musicbrainz.models.core.GenreMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.helpers.FilterTestCase
import ly.david.musicsearch.data.repository.helpers.testFilter
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.genre.GenresListRepository
import ly.david.musicsearch.shared.domain.list.ListFilters
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class GenresListRepositoryImplTest : KoinTest {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val genreDao: GenreDao by inject()
    private val collectionDao: CollectionDao by inject()
    private val browseRemoteMetadataDao: BrowseRemoteMetadataDao by inject()
    private val collectionEntityDao: CollectionEntityDao by inject()
    private val aliasDao: AliasDao by inject()

    private fun createRepository(
        genres: List<GenreMusicBrainzNetworkModel>,
    ): GenresListRepository {
        return GenresListRepositoryImpl(
            browseRemoteMetadataDao = browseRemoteMetadataDao,
            collectionEntityDao = collectionEntityDao,
            genreDao = genreDao,
            aliasDao = aliasDao,
            browseApi = object : FakeBrowseApi() {
                override suspend fun browseGenresByEntity(
                    entityId: String,
                    entity: MusicBrainzEntityType,
                    limit: Int,
                    offset: Int,
                ): BrowseGenresResponse {
                    return BrowseGenresResponse(
                        count = 0,
                        offset = 0,
                        musicBrainzModels = genres,
                    )
                }
            },
        )
    }

    @Test
    fun genresByCollection() = runTest {
        val collectionId = "950cea33-433e-497f-93bb-a05a393a2c02"
        val entity = MusicBrainzEntityType.COLLECTION
        val genres = listOf(
            mathRockGenreMusicBrainzModel,
            jPopGenreMusicBrainzModel,
        )
        val genresListRepository = createRepository(
            genres = genres,
        )

        collectionDao.insertLocal(
            collection = CollectionListItemModel(
                id = collectionId,
                isRemote = false,
                name = "Genres",
                entity = MusicBrainzEntityType.INSTRUMENT,
            ),
        )
        collectionEntityDao.addAllToCollection(
            collectionId = collectionId,
            entityIds = genres.map { it.id },
        )

        val browseMethod = BrowseMethod.ByEntity(
            entityId = collectionId,
            entityType = entity,
        )

        testFilter(
            pagingFlowProducer = { query ->
                genresListRepository.observeGenres(
                    browseMethod = browseMethod,
                    listFilters = ListFilters.Base(
                        query = query,
                    ),
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "no filter",
                    query = "",
                    expectedResult = listOf(
                        mathRockGenreListItemModel.copy(
                            collected = true,
                        ),
                        jPopGenreListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
                FilterTestCase(
                    description = "filter by name",
                    query = "rock",
                    expectedResult = listOf(
                        mathRockGenreListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
            ),
        )
    }

    @Test
    fun `all Genres`() = runTest {
        genresByCollection()

        val genresListRepository = createRepository(
            genres = listOf(),
        )

        testFilter(
            pagingFlowProducer = { query ->
                genresListRepository.observeGenres(
                    browseMethod = BrowseMethod.All,
                    listFilters = ListFilters.Base(
                        query = query,
                    ),
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "no filter",
                    query = "",
                    expectedResult = listOf(
                        mathRockGenreListItemModel.copy(
                            collected = true,
                        ),
                        jPopGenreListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
                FilterTestCase(
                    description = "by name",
                    query = "pop",
                    expectedResult = listOf(
                        jPopGenreListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
            ),
        )
    }
}
