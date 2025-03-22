package ly.david.musicsearch.data.repository.work

import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.api.FakeBrowseApi
import ly.david.data.test.davidBowieArtistMusicBrainzModel
import ly.david.data.test.dontStopMeNowWorkListItemModel
import ly.david.data.test.dontStopMeNowWorkMusicBrainzModel
import ly.david.data.test.hackingToTheGateWorkListItemModel
import ly.david.data.test.hackingToTheGateWorkMusicBrainzModel
import ly.david.data.test.queenArtistMusicBrainzModel
import ly.david.data.test.skycladObserverWorkListItemModel
import ly.david.data.test.skycladObserverWorkMusicBrainzModel
import ly.david.data.test.starmanWorkListItemModel
import ly.david.data.test.starmanWorkMusicBrainzModel
import ly.david.data.test.underPressureWorkListItemModel
import ly.david.data.test.underPressureWorkMusicBrainzModel
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.WorkAttributeDao
import ly.david.musicsearch.data.database.dao.WorkDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseWorksResponse
import ly.david.musicsearch.data.musicbrainz.models.core.WorkMusicBrainzModel
import ly.david.musicsearch.data.repository.helpers.FilterTestCase
import ly.david.musicsearch.data.repository.helpers.TestWorkRepository
import ly.david.musicsearch.data.repository.helpers.testFilter
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.history.VisitedDao
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.work.WorkAttributeUiModel
import ly.david.musicsearch.shared.domain.work.WorkDetailsModel
import ly.david.musicsearch.shared.domain.work.WorksByEntityRepository
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class WorksByEntityRepositoryImplTest : KoinTest, TestWorkRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    override val workDao: WorkDao by inject()
    override val workAttributeDao: WorkAttributeDao by inject()
    override val entityHasRelationsDao: EntityHasRelationsDao by inject()
    override val visitedDao: VisitedDao by inject()
    override val relationDao: RelationDao by inject()
    private val collectionDao: CollectionDao by inject()
    private val browseEntityCountDao: BrowseEntityCountDao by inject()
    private val collectionEntityDao: CollectionEntityDao by inject()

    private val collectionId = "950cea33-433e-497f-93bb-a05a393a2c02"

    private fun createWorksByEntityRepository(
        works: List<WorkMusicBrainzModel>,
    ): WorksByEntityRepository {
        return WorksByEntityRepositoryImpl(
            browseEntityCountDao = browseEntityCountDao,
            collectionEntityDao = collectionEntityDao,
            workDao = workDao,
            browseApi = object : FakeBrowseApi() {
                override suspend fun browseWorksByEntity(
                    entityId: String,
                    entity: MusicBrainzEntity,
                    limit: Int,
                    offset: Int,
                ): BrowseWorksResponse {
                    return BrowseWorksResponse(
                        count = 1,
                        offset = 0,
                        musicBrainzModels = works,
                    )
                }
            },
        )
    }

    @Test
    fun setupWorksByCollection() = runTest {
        val works = listOf(
            hackingToTheGateWorkMusicBrainzModel,
            underPressureWorkMusicBrainzModel,
            skycladObserverWorkMusicBrainzModel,
        )
        val worksByEntityRepository = createWorksByEntityRepository(
            works = works,
        )

        collectionDao.insertLocal(
            collection = CollectionListItemModel(
                id = collectionId,
                isRemote = false,
                name = "works",
                entity = MusicBrainzEntity.WORK,
            ),
        )
        collectionEntityDao.insertAll(
            collectionId = collectionId,
            entityIds = works.map { it.id },
        )

        testFilter(
            pagingFlowProducer = { query ->
                worksByEntityRepository.observeWorksByEntity(
                    entityId = collectionId,
                    entity = MusicBrainzEntity.COLLECTION,
                    listFilters = ListFilters(
                        query = query,
                    ),
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "No filter",
                    query = "",
                    expectedResult = listOf(
                        underPressureWorkListItemModel,
                        hackingToTheGateWorkListItemModel,
                        skycladObserverWorkListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by name",
                    query = "und",
                    expectedResult = listOf(
                        underPressureWorkListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by type",
                    query = "so",
                    expectedResult = listOf(
                        underPressureWorkListItemModel,
                        hackingToTheGateWorkListItemModel,
                        skycladObserverWorkListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by language",
                    query = "en",
                    expectedResult = listOf(
                        underPressureWorkListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by iswc",
                    query = "8.9",
                    expectedResult = listOf(
                        hackingToTheGateWorkListItemModel,
                    ),
                ),
            ),
        )
    }

    @Test
    fun setupWorksByDavidBowie() = runTest {
        val entityId = davidBowieArtistMusicBrainzModel.id
        val entity = MusicBrainzEntity.ARTIST
        val works = listOf(
            starmanWorkMusicBrainzModel,
            underPressureWorkMusicBrainzModel,
        )
        val worksByEntityRepository = createWorksByEntityRepository(
            works = works,
        )

        testFilter(
            pagingFlowProducer = { query ->
                worksByEntityRepository.observeWorksByEntity(
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
                        starmanWorkListItemModel,
                        underPressureWorkListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by name",
                    query = "star",
                    expectedResult = listOf(
                        starmanWorkListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by type",
                    query = "song",
                    expectedResult = listOf(
                        starmanWorkListItemModel,
                        underPressureWorkListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by language",
                    query = "eng",
                    expectedResult = listOf(
                        starmanWorkListItemModel,
                        underPressureWorkListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by iswc",
                    query = "-8",
                    expectedResult = listOf(
                        underPressureWorkListItemModel,
                    ),
                ),
            ),
        )
    }

    @Test
    fun setupWorksByQueen() = runTest {
        val entityId = queenArtistMusicBrainzModel.id
        val entity = MusicBrainzEntity.ARTIST
        val works = listOf(
            dontStopMeNowWorkMusicBrainzModel,
            underPressureWorkMusicBrainzModel,
        )
        val worksByEntityRepository = createWorksByEntityRepository(
            works = works,
        )

        testFilter(
            pagingFlowProducer = { query ->
                worksByEntityRepository.observeWorksByEntity(
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
                        underPressureWorkListItemModel,
                        dontStopMeNowWorkListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by name",
                    query = "don",
                    expectedResult = listOf(
                        dontStopMeNowWorkListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by type",
                    query = "song",
                    expectedResult = listOf(
                        underPressureWorkListItemModel,
                        dontStopMeNowWorkListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by language",
                    query = "eng",
                    expectedResult = listOf(
                        underPressureWorkListItemModel,
                        dontStopMeNowWorkListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by iswc",
                    query = "6-0",
                    expectedResult = listOf(
                        underPressureWorkListItemModel,
                    ),
                ),
            ),
        )
    }

    @Test
    fun `all works`() = runTest {
        setupWorksByDavidBowie()
        setupWorksByCollection()
        setupWorksByQueen()

        val worksByEntityRepository = createWorksByEntityRepository(
            works = listOf(),
        )
        testFilter(
            pagingFlowProducer = { query ->
                worksByEntityRepository.observeWorksByEntity(
                    entityId = null,
                    entity = null,
                    listFilters = ListFilters(
                        query = query,
                    ),
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "No filter",
                    query = "",
                    expectedResult = listOf(
                        starmanWorkListItemModel,
                        underPressureWorkListItemModel,
                        hackingToTheGateWorkListItemModel,
                        skycladObserverWorkListItemModel,
                        dontStopMeNowWorkListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by language",
                    query = "jpn",
                    expectedResult = listOf(
                        hackingToTheGateWorkListItemModel,
                        skycladObserverWorkListItemModel,
                    ),
                ),
            ),
        )
    }

    // TODO: assert works by collection
    @Test
    fun `refreshing works that belong to multiple entities does not delete the work`() = runTest {
        setupWorksByDavidBowie()
        setupWorksByQueen()

        val modifiedWorks = listOf(
            starmanWorkMusicBrainzModel.copy(
                id = "new-id-is-considered-a-different-work",
            ),
            underPressureWorkMusicBrainzModel.copy(
                disambiguation = "changes will be ignored if work is linked to multiple entities",
            ),
        )
        val worksByEntityRepository = createWorksByEntityRepository(
            works = modifiedWorks,
        )

        // refresh
        worksByEntityRepository.observeWorksByEntity(
            entityId = davidBowieArtistMusicBrainzModel.id,
            entity = MusicBrainzEntity.ARTIST,
            listFilters = ListFilters(),
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                listOf(
                    underPressureWorkListItemModel,
                    starmanWorkListItemModel.copy(
                        id = "new-id-is-considered-a-different-work",
                    ),
                ),
                this,
            )
        }

        // other entities remain unchanged
        worksByEntityRepository.observeWorksByEntity(
            entityId = queenArtistMusicBrainzModel.id,
            entity = MusicBrainzEntity.ARTIST,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    underPressureWorkListItemModel,
                    dontStopMeNowWorkListItemModel,
                ),
                this,
            )
        }

        worksByEntityRepository.observeWorksByEntity(
            entityId = null,
            entity = null,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    underPressureWorkListItemModel,
                    dontStopMeNowWorkListItemModel,
                    starmanWorkListItemModel.copy(
                        id = "new-id-is-considered-a-different-work",
                    ),
                ),
                this,
            )
        }

        // now visit the work and refresh it
        val workRepository = createWorkRepository(
            underPressureWorkMusicBrainzModel.copy(
                disambiguation = "changes will be ignored if work is linked to multiple entities",
            ),
        )
        workRepository.lookupWork(
            workId = underPressureWorkMusicBrainzModel.id,
            forceRefresh = false,
        ).let { workDetailsModel ->
            assertEquals(
                WorkDetailsModel(
                    id = "4e6a04c3-6897-391d-8e8c-1da7a6dce1ca",
                    name = "Under Pressure",
                    type = "Song",
                    language = "eng",
                    iswcs = listOf(
                        "T-010.475.727-8",
                        "T-011.226.466-0",
                    ),
                    attributes = listOf(
                        WorkAttributeUiModel(
                            value = "2182263",
                            type = "ACAM ID",
                            typeId = "955305a2-58ec-4c64-94f7-7fb9b209416c",
                        ),
                        WorkAttributeUiModel(
                            value = "2406479",
                            type = "ACAM ID",
                            typeId = "955305a2-58ec-4c64-94f7-7fb9b209416c",
                        ),
                    ),
                ),
                workDetailsModel,
            )
        }
        workRepository.lookupWork(
            workId = underPressureWorkMusicBrainzModel.id,
            forceRefresh = true,
        ).let { workDetailsModel ->
            assertEquals(
                WorkDetailsModel(
                    id = "4e6a04c3-6897-391d-8e8c-1da7a6dce1ca",
                    disambiguation = "changes will be ignored if work is linked to multiple entities",
                    name = "Under Pressure",
                    type = "Song",
                    language = "eng",
                    iswcs = listOf(
                        "T-010.475.727-8",
                        "T-011.226.466-0",
                    ),
                    attributes = listOf(
                        WorkAttributeUiModel(
                            value = "2182263",
                            type = "ACAM ID",
                            typeId = "955305a2-58ec-4c64-94f7-7fb9b209416c",
                        ),
                        WorkAttributeUiModel(
                            value = "2406479",
                            type = "ACAM ID",
                            typeId = "955305a2-58ec-4c64-94f7-7fb9b209416c",
                        ),
                    ),
                ),
                workDetailsModel,
            )
        }
    }
}
