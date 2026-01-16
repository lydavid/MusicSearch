package ly.david.musicsearch.data.repository.work

import androidx.paging.testing.asSnapshot
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.api.FakeBrowseApi
import ly.david.data.test.cruelAngelThesisWorkListItemModel
import ly.david.data.test.cruelAngelThesisWorkMusicBrainzModel
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
import ly.david.musicsearch.data.listenbrainz.api.ListenBrainzArtist
import ly.david.musicsearch.data.listenbrainz.api.ListenBrainzListen
import ly.david.musicsearch.data.listenbrainz.api.ListensResponse
import ly.david.musicsearch.data.listenbrainz.api.MbidMapping
import ly.david.musicsearch.data.listenbrainz.api.Payload
import ly.david.musicsearch.data.listenbrainz.api.TrackMetadata
import ly.david.musicsearch.data.musicbrainz.api.BrowseWorksResponse
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.WorkMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.relation.Direction
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.SerializableMusicBrainzEntity
import ly.david.musicsearch.data.repository.helpers.FilterTestCase
import ly.david.musicsearch.data.repository.helpers.NoOpListenBrainzAuthStore
import ly.david.musicsearch.data.repository.helpers.TEST_USERNAME
import ly.david.musicsearch.data.repository.helpers.TestListensListRepository
import ly.david.musicsearch.data.repository.helpers.TestWorkRepository
import ly.david.musicsearch.data.repository.helpers.testDateTimeInThePast
import ly.david.musicsearch.data.repository.helpers.testFilter
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.WorkDetailsModel
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.listen.ListenDao
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.listitem.WorkListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.work.WorkAttributeUiModel
import ly.david.musicsearch.shared.domain.work.WorksListRepository
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class WorksListRepositoryImplTest : KoinTest, TestWorkRepository, TestListensListRepository {

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
    override val listenDao: ListenDao by inject()

    private val collectionId = "950cea33-433e-497f-93bb-a05a393a2c02"

    private fun createWorksListRepository(
        works: List<WorkMusicBrainzNetworkModel>,
        fakeBrowseUsername: String = "",
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
            listenBrainzAuthStore = object : NoOpListenBrainzAuthStore() {
                override val browseUsername: Flow<String>
                    get() = flowOf(fakeBrowseUsername)
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

    @Test
    fun `aliases does not multiply listen count`() = runTest {
        val entityId = "my-collection-id"
        val entity = MusicBrainzEntityType.COLLECTION
        val browseMethod = BrowseMethod.ByEntity(
            entityId = entityId,
            entityType = entity,
        )

        collectionDao.insertLocal(
            collection = CollectionListItemModel(
                id = entityId,
                isRemote = false,
                name = "Works",
                entity = MusicBrainzEntityType.WORK,
            ),
        )

        createListensListRepository(
            response = ListensResponse(
                payload = Payload(
                    latest_listen_ts = 1755101240L,
                    oldest_listen_ts = 1755101240L,
                    listens = listOf(
                        ListenBrainzListen(
                            insertedAtS = 1755101240L,
                            listenedAtS = 1755101240L,
                            recording_msid = "9021ba8c-a831-42c9-8052-f017bf3e6795",
                            user_name = TEST_USERNAME,
                            track_metadata = TrackMetadata(
                                artist_name = "Yoko Takahashi",
                                track_name = "残酷な天使のテーゼ",
                                release_name = "残酷な天使のテーゼ / 魂のルフラン",
                                mbid_mapping = MbidMapping(
                                    recording_mbid = "99b455ec-7f1e-49b2-bcbc-b8ee3302dbb1",
                                    recording_name = "残酷な天使のテーゼ",
                                    artists = listOf(
                                        ListenBrainzArtist(
                                            artist_credit_name = "高橋洋子",
                                            artist_mbid = "7004bc31-23ee-4216-8d74-0d230e88079f",
                                            join_phrase = "",
                                        ),
                                    ),
                                    caa_id = 20686427578,
                                    caa_release_mbid = "0cc76aef-e050-4175-a83c-3ebc69058367",
                                ),
                            ),
                        ),
                    ),
                ),
            ),
        )
            .observeListens(
                username = TEST_USERNAME,
                query = "",
                entityFacet = null,
                stopPrepending = false,
                stopAppending = false,
                onReachedLatest = {},
                onReachedOldest = {},
            )
            .asSnapshot()

        val works = listOf(
            cruelAngelThesisWorkMusicBrainzModel,
            hackingToTheGateWorkMusicBrainzModel,
        )
        val worksListRepository = createWorksListRepository(
            works = works,
            fakeBrowseUsername = TEST_USERNAME,
        )
        worksListRepository.observeWorks(
            browseMethod = browseMethod,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    cruelAngelThesisWorkListItemModel.copy(
                        collected = true,
                        listenState = WorkListItemModel.ListenState.Unknown,
                    ),
                    hackingToTheGateWorkListItemModel.copy(
                        collected = true,
                        listenState = WorkListItemModel.ListenState.Unknown,
                    ),
                ),
                this,
            )
        }

        // need to visit work before listens of its recordings can be linked to it
        createWorkRepository(
            musicBrainzModel = cruelAngelThesisWorkMusicBrainzModel.copy(
                relations = listOf(
                    RelationMusicBrainzModel(
                        type = "performance",
                        typeId = "",
                        direction = Direction.BACKWARD,
                        targetType = SerializableMusicBrainzEntity.RECORDING,
                        recording = RecordingMusicBrainzNetworkModel(
                            id = "99b455ec-7f1e-49b2-bcbc-b8ee3302dbb1",
                            name = "残酷な天使のテーゼ",
                            length = 246000,
                        ),
                    ),
                    RelationMusicBrainzModel(
                        type = "performance",
                        typeId = "",
                        direction = Direction.BACKWARD,
                        targetType = SerializableMusicBrainzEntity.RECORDING,
                        recording = RecordingMusicBrainzNetworkModel(
                            id = "b0ac4111-a8ff-4deb-b683-b645d14b10d8",
                            name = "残酷な天使のテーゼ 2009VERSION",
                            length = 267653,
                        ),
                    ),
                )
            ),
            fakeBrowseUsername = TEST_USERNAME,
        ).lookupWork(
            workId = cruelAngelThesisWorkMusicBrainzModel.id,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        )
        worksListRepository.observeWorks(
            browseMethod = browseMethod,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    cruelAngelThesisWorkListItemModel.copy(
                        collected = true,
                        visited = true,
                        listenState = WorkListItemModel.ListenState.Known(1),
                    ),
                    hackingToTheGateWorkListItemModel.copy(
                        collected = true,
                        listenState = WorkListItemModel.ListenState.Unknown,
                    ),
                ),
                this,
            )
        }
    }
}
