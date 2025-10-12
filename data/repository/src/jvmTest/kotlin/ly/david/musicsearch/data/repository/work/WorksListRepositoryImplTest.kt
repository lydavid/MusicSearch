package ly.david.musicsearch.data.repository.work

import androidx.paging.testing.asSnapshot
import kotlinx.collections.immutable.persistentListOf
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
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.RelationsMetadataDao
import ly.david.musicsearch.data.database.dao.WorkAttributeDao
import ly.david.musicsearch.data.database.dao.WorkDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseWorksResponse
import ly.david.musicsearch.data.musicbrainz.models.core.WorkMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.helpers.FilterTestCase
import ly.david.musicsearch.data.repository.helpers.TestWorkRepository
import ly.david.musicsearch.data.repository.helpers.testDateTimeInThePast
import ly.david.musicsearch.data.repository.helpers.testFilter
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.WorkDetailsModel
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.work.WorkAttributeUiModel
import ly.david.musicsearch.shared.domain.work.WorksListRepository
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class WorksListRepositoryImplTest : KoinTest, TestWorkRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    override val workDao: WorkDao by inject()
    override val workAttributeDao: WorkAttributeDao by inject()
    override val relationsMetadataDao: RelationsMetadataDao by inject()
    override val detailsMetadataDao: DetailsMetadataDao by inject()
    override val relationDao: RelationDao by inject()
    override val aliasDao: AliasDao by inject()
    private val collectionDao: CollectionDao by inject()
    private val browseRemoteMetadataDao: BrowseRemoteMetadataDao by inject()
    private val collectionEntityDao: CollectionEntityDao by inject()
    override val coroutineDispatchers: CoroutineDispatchers by inject()

    private val collectionId = "950cea33-433e-497f-93bb-a05a393a2c02"

    private fun createWorksListRepository(
        works: List<WorkMusicBrainzNetworkModel>,
    ): WorksListRepository {
        return WorksListRepositoryImpl(
            browseRemoteMetadataDao = browseRemoteMetadataDao,
            collectionEntityDao = collectionEntityDao,
            workDao = workDao,
            aliasDao = aliasDao,
            browseApi = object : FakeBrowseApi() {
                override suspend fun browseWorksByEntity(
                    entityId: String,
                    entity: MusicBrainzEntityType,
                    limit: Int,
                    offset: Int,
                    include: String,
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
        val worksListRepository = createWorksListRepository(
            works = works,
        )

        collectionDao.insertLocal(
            collection = CollectionListItemModel(
                id = collectionId,
                isRemote = false,
                name = "works",
                entity = MusicBrainzEntityType.WORK,
            ),
        )
        collectionEntityDao.addAllToCollection(
            collectionId = collectionId,
            entityIds = works.map { it.id },
        )

        testFilter(
            pagingFlowProducer = { query ->
                worksListRepository.observeWorks(
                    browseMethod = BrowseMethod.ByEntity(
                        entityId = collectionId,
                        entityType = MusicBrainzEntityType.COLLECTION,
                    ),
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
                        underPressureWorkListItemModel.copy(
                            collected = true,
                        ),
                        hackingToTheGateWorkListItemModel.copy(
                            collected = true,
                        ),
                        skycladObserverWorkListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
                FilterTestCase(
                    description = "filter by name",
                    query = "und",
                    expectedResult = listOf(
                        underPressureWorkListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
                FilterTestCase(
                    description = "filter by type",
                    query = "so",
                    expectedResult = listOf(
                        underPressureWorkListItemModel.copy(
                            collected = true,
                        ),
                        hackingToTheGateWorkListItemModel.copy(
                            collected = true,
                        ),
                        skycladObserverWorkListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
                FilterTestCase(
                    description = "filter by language",
                    query = "en",
                    expectedResult = listOf(
                        underPressureWorkListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
                FilterTestCase(
                    description = "filter by iswc",
                    query = "8.9",
                    expectedResult = listOf(
                        hackingToTheGateWorkListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
            ),
        )
    }

    @Test
    fun setupWorksByDavidBowie() = runTest {
        val entityId = davidBowieArtistMusicBrainzModel.id
        val entity = MusicBrainzEntityType.ARTIST
        val works = listOf(
            starmanWorkMusicBrainzModel,
            underPressureWorkMusicBrainzModel,
        )
        val worksListRepository = createWorksListRepository(
            works = works,
        )

        testFilter(
            pagingFlowProducer = { query ->
                worksListRepository.observeWorks(
                    browseMethod = BrowseMethod.ByEntity(
                        entityId = entityId,
                        entityType = entity,
                    ),
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
        val entity = MusicBrainzEntityType.ARTIST
        val works = listOf(
            dontStopMeNowWorkMusicBrainzModel,
            underPressureWorkMusicBrainzModel,
        )
        val worksListRepository = createWorksListRepository(
            works = works,
        )

        testFilter(
            pagingFlowProducer = { query ->
                worksListRepository.observeWorks(
                    browseMethod = BrowseMethod.ByEntity(
                        entityId = entityId,
                        entityType = entity,
                    ),
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
        setupWorksByQueen()
        setupWorksByCollection()

        val worksListRepository = createWorksListRepository(
            works = listOf(),
        )
        testFilter(
            pagingFlowProducer = { query ->
                worksListRepository.observeWorks(
                    browseMethod = BrowseMethod.All,
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
                        underPressureWorkListItemModel.copy(
                            collected = true,
                        ),
                        dontStopMeNowWorkListItemModel,
                        hackingToTheGateWorkListItemModel.copy(
                            collected = true,
                        ),
                        skycladObserverWorkListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
                FilterTestCase(
                    description = "filter by language",
                    query = "jpn",
                    expectedResult = listOf(
                        hackingToTheGateWorkListItemModel.copy(
                            collected = true,
                        ),
                        skycladObserverWorkListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
            ),
        )
    }

    // Works were the first entity type to upsert in both list items and details
    // which works nicely to allow refreshing data in lists without deleting the item, which may mess up paging on
    // other screens.
    @Test
    fun `refreshing works does not delete the work`() = runTest {
        setupWorksByDavidBowie()
        setupWorksByQueen()
        setupWorksByCollection()

        val modifiedWorks = listOf(
            starmanWorkMusicBrainzModel.copy(
                id = "new-id-is-considered-a-different-work",
            ),
            underPressureWorkMusicBrainzModel.copy(
                disambiguation = "changes will still show up",
            ),
        )
        val worksListRepository = createWorksListRepository(
            works = modifiedWorks,
        )

        // refresh
        worksListRepository.observeWorks(
            browseMethod = BrowseMethod.ByEntity(
                entityId = davidBowieArtistMusicBrainzModel.id,
                entityType = MusicBrainzEntityType.ARTIST,
            ),
            listFilters = ListFilters(),
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                listOf(
                    underPressureWorkListItemModel.copy(
                        disambiguation = "changes will still show up",
                        collected = true,
                    ),
                    starmanWorkListItemModel.copy(
                        id = "new-id-is-considered-a-different-work",
                    ),
                ),
                this,
            )
        }

        worksListRepository.observeWorks(
            browseMethod = BrowseMethod.ByEntity(
                entityId = queenArtistMusicBrainzModel.id,
                entityType = MusicBrainzEntityType.ARTIST,
            ),
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    underPressureWorkListItemModel.copy(
                        disambiguation = "changes will still show up",
                        collected = true,
                    ),
                    dontStopMeNowWorkListItemModel,
                ),
                this,
            )
        }
        worksListRepository.observeWorks(
            browseMethod = BrowseMethod.ByEntity(
                entityId = collectionId,
                entityType = MusicBrainzEntityType.COLLECTION,
            ),
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    underPressureWorkListItemModel.copy(
                        disambiguation = "changes will still show up",
                        collected = true,
                    ),
                    hackingToTheGateWorkListItemModel.copy(
                        collected = true,
                    ),
                    skycladObserverWorkListItemModel.copy(
                        collected = true,
                    ),
                ),
                this,
            )
        }
        worksListRepository.observeWorks(
            browseMethod = BrowseMethod.All,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    starmanWorkListItemModel,
                    underPressureWorkListItemModel.copy(
                        disambiguation = "changes will still show up",
                        collected = true,
                    ),
                    dontStopMeNowWorkListItemModel,
                    hackingToTheGateWorkListItemModel.copy(
                        collected = true,
                    ),
                    skycladObserverWorkListItemModel.copy(
                        collected = true,
                    ),
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
                disambiguation = "some change",
            ),
        )
        // because we have never visited this page, the first visit will load from network
        workRepository.lookupWork(
            workId = underPressureWorkMusicBrainzModel.id,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        ).let { workDetailsModel ->
            assertEquals(
                WorkDetailsModel(
                    id = "4e6a04c3-6897-391d-8e8c-1da7a6dce1ca",
                    name = "Under Pressure",
                    disambiguation = "some change",
                    type = "Song",
                    languages = persistentListOf("eng"),
                    iswcs = persistentListOf(
                        "T-010.475.727-8",
                        "T-011.226.466-0",
                    ),
                    attributes = persistentListOf(
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
                    lastUpdated = testDateTimeInThePast,
                ),
                workDetailsModel,
            )
        }
        workRepository.lookupWork(
            workId = underPressureWorkMusicBrainzModel.id,
            forceRefresh = true,
            lastUpdated = testDateTimeInThePast,
        ).let { workDetailsModel ->
            assertEquals(
                WorkDetailsModel(
                    id = "4e6a04c3-6897-391d-8e8c-1da7a6dce1ca",
                    disambiguation = "some change",
                    name = "Under Pressure",
                    type = "Song",
                    languages = persistentListOf("eng"),
                    iswcs = persistentListOf(
                        "T-010.475.727-8",
                        "T-011.226.466-0",
                    ),
                    attributes = persistentListOf(
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
                    lastUpdated = testDateTimeInThePast,
                ),
                workDetailsModel,
            )
        }
    }
}
