package ly.david.musicsearch.data.repository.recording

import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.api.FakeBrowseApi
import ly.david.data.test.itouKanakoArtistMusicBrainzModel
import ly.david.data.test.roseliaArtistMusicBrainzModel
import ly.david.data.test.skycladObserverCoverRecordingListItemModel
import ly.david.data.test.skycladObserverCoverRecordingMusicBrainzModel
import ly.david.data.test.skycladObserverRecordingListItemModel
import ly.david.data.test.skycladObserverRecordingMusicBrainzModel
import ly.david.data.test.skycladObserverWorkMusicBrainzModel
import ly.david.data.test.underPressureRecordingListItemModel
import ly.david.data.test.underPressureRecordingMusicBrainzModel
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.BrowseEntityCountDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.EntityHasRelationsDao
import ly.david.musicsearch.data.database.dao.RecordingDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseRecordingsResponse
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzModel
import ly.david.musicsearch.data.repository.helpers.FilterTestCase
import ly.david.musicsearch.data.repository.helpers.TestRecordingRepository
import ly.david.musicsearch.data.repository.helpers.testFilter
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.history.VisitedDao
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.recording.RecordingDetailsModel
import ly.david.musicsearch.shared.domain.recording.RecordingsByEntityRepository
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class RecordingsByEntityRepositoryImplTest : KoinTest, TestRecordingRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    override val recordingDao: RecordingDao by inject()
    override val artistCreditDao: ArtistCreditDao by inject()
    override val entityHasRelationsDao: EntityHasRelationsDao by inject()
    override val visitedDao: VisitedDao by inject()
    override val relationDao: RelationDao by inject()
    private val collectionDao: CollectionDao by inject()
    private val browseEntityCountDao: BrowseEntityCountDao by inject()
    private val collectionEntityDao: CollectionEntityDao by inject()

    private fun createRecordingsByEntityRepository(
        recordings: List<RecordingMusicBrainzModel>,
    ): RecordingsByEntityRepository {
        return RecordingsByEntityRepositoryImpl(
            browseEntityCountDao = browseEntityCountDao,
            collectionEntityDao = collectionEntityDao,
            recordingDao = recordingDao,
            browseApi = object : FakeBrowseApi() {
                override suspend fun browseRecordingsByEntity(
                    entityId: String,
                    entity: MusicBrainzEntity,
                    limit: Int,
                    offset: Int,
                    include: String,
                ): BrowseRecordingsResponse {
                    return BrowseRecordingsResponse(
                        count = 1,
                        offset = 0,
                        musicBrainzModels = recordings,
                    )
                }
            },
        )
    }

    @Test
    fun setupRecordingsByCollection() = runTest {
        val collectionId = "950cea33-433e-497f-93bb-a05a393a2c02"
        val recordings = listOf(
            underPressureRecordingMusicBrainzModel,
            skycladObserverRecordingMusicBrainzModel,
        )
        val recordingsByEntityRepository = createRecordingsByEntityRepository(
            recordings = recordings,
        )

        collectionDao.insertLocal(
            collection = CollectionListItemModel(
                id = collectionId,
                isRemote = false,
                name = "recordings",
                entity = MusicBrainzEntity.RECORDING,
            ),
        )
        collectionEntityDao.insertAll(
            collectionId = collectionId,
            entityIds = recordings.map { it.id },
        )

        testFilter(
            pagingFlowProducer = { query ->
                recordingsByEntityRepository.observeRecordingsByEntity(
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
                        underPressureRecordingListItemModel,
                        skycladObserverRecordingListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by name",
                    query = "ス",
                    expectedResult = listOf(
                        skycladObserverRecordingListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by artist credit",
                    query = "&",
                    expectedResult = listOf(
                        underPressureRecordingListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by first release date",
                    query = "2009",
                    expectedResult = listOf(
                        skycladObserverRecordingListItemModel,
                    ),
                ),
            ),
        )
    }

    @Test
    fun setUpRecordingsByArtist() = runTest {
        val entityId = itouKanakoArtistMusicBrainzModel.id
        val entity = MusicBrainzEntity.ARTIST
        val recordings = listOf(
            skycladObserverRecordingMusicBrainzModel,
            skycladObserverCoverRecordingMusicBrainzModel,
        )
        val recordingsByEntityRepository = createRecordingsByEntityRepository(
            recordings = recordings,
        )

        testFilter(
            pagingFlowProducer = { query ->
                recordingsByEntityRepository.observeRecordingsByEntity(
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
                        skycladObserverRecordingListItemModel,
                        skycladObserverCoverRecordingListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by artist credit",
                    query = "rose",
                    expectedResult = listOf(
                        skycladObserverCoverRecordingListItemModel,
                    ),
                ),
            ),
        )
    }

    @Test
    fun setupRecordingsByWork() = runTest {
        val entityId = skycladObserverWorkMusicBrainzModel.id
        val entity = MusicBrainzEntity.WORK
        val recordings = listOf(
            skycladObserverRecordingMusicBrainzModel,
            skycladObserverCoverRecordingMusicBrainzModel,
        )
        val recordingsByEntityRepository = createRecordingsByEntityRepository(
            recordings = recordings,
        )

        testFilter(
            pagingFlowProducer = { query ->
                recordingsByEntityRepository.observeRecordingsByEntity(
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
                        skycladObserverRecordingListItemModel,
                        skycladObserverCoverRecordingListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by artist credit",
                    query = "rose",
                    expectedResult = listOf(
                        skycladObserverCoverRecordingListItemModel,
                    ),
                ),
            ),
        )
    }

    @Test
    fun `recordings by entity (artist, work)`() = runTest {
        setUpRecordingsByArtist()
        setupRecordingsByWork()
    }

    @Test
    fun `all recordings`() = runTest {
        setUpRecordingsByArtist()
        setupRecordingsByWork()
        setupRecordingsByCollection()

        val recordingsByEntityRepository = createRecordingsByEntityRepository(
            recordings = listOf(),
        )
        testFilter(
            pagingFlowProducer = { query ->
                recordingsByEntityRepository.observeRecordingsByEntity(
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
                        underPressureRecordingListItemModel,
                        skycladObserverRecordingListItemModel,
                        skycladObserverCoverRecordingListItemModel,
                    ),
                ),
                FilterTestCase(
                    description = "filter by artist name",
                    query = "bowie",
                    expectedResult = listOf(
                        underPressureRecordingListItemModel,
                    ),
                ),
            ),
        )
    }

    @Test
    fun `refreshing recordings that belong to multiple entities does not delete the recording`() = runTest {
        setUpRecordingsByArtist()
        setupRecordingsByWork()

        val modifiedRecordings = listOf(
            skycladObserverRecordingMusicBrainzModel.copy(
                id = "new-id-is-considered-a-different-recording",
            ),
            skycladObserverCoverRecordingMusicBrainzModel.copy(
                disambiguation = "changes will be ignored if recording is linked to multiple entities",
            ),
        )
        val recordingsByEntityRepository = createRecordingsByEntityRepository(
            recordings = modifiedRecordings,
        )

        // refresh
        recordingsByEntityRepository.observeRecordingsByEntity(
            entityId = itouKanakoArtistMusicBrainzModel.id,
            entity = MusicBrainzEntity.ARTIST,
            listFilters = ListFilters(),
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                listOf(
                    skycladObserverRecordingListItemModel.copy(
                        id = "new-id-is-considered-a-different-recording",
                    ),
                    skycladObserverCoverRecordingListItemModel,
                ),
                this,
            )
        }

        // other entities remain unchanged
        recordingsByEntityRepository.observeRecordingsByEntity(
            entityId = skycladObserverWorkMusicBrainzModel.id,
            entity = MusicBrainzEntity.WORK,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    skycladObserverRecordingListItemModel,
                    skycladObserverCoverRecordingListItemModel,
                ),
                this,
            )
        }

        // both old and new version of recording exists
        recordingsByEntityRepository.observeRecordingsByEntity(
            entityId = null,
            entity = null,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    skycladObserverRecordingListItemModel,
                    skycladObserverRecordingListItemModel.copy(
                        id = "new-id-is-considered-a-different-recording",
                    ),
                    skycladObserverCoverRecordingListItemModel,
                ),
                this,
            )
        }

        recordingsByEntityRepository.observeRecordingsByEntity(
            entityId = skycladObserverWorkMusicBrainzModel.id,
            entity = MusicBrainzEntity.WORK,
            listFilters = ListFilters(),
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                listOf(
                    skycladObserverRecordingListItemModel.copy(
                        id = "new-id-is-considered-a-different-recording",
                    ),
                    skycladObserverCoverRecordingListItemModel,
                ),
                this,
            )
        }

        // now only new version of recording exists
        // however, the other recording is never updated unless we go into it and refresh
        recordingsByEntityRepository.observeRecordingsByEntity(
            entityId = null,
            entity = null,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    skycladObserverRecordingListItemModel.copy(
                        id = "new-id-is-considered-a-different-recording",
                    ),
                    skycladObserverCoverRecordingListItemModel,
                ),
                this,
            )
        }

        // now visit the recording and refresh it
        val recordingRepository = createRecordingRepository(
            skycladObserverCoverRecordingMusicBrainzModel.copy(
                disambiguation = "changes will be ignored if recording is linked to multiple entities",
            ),
        )
        recordingRepository.lookupRecording(
            recordingId = skycladObserverCoverRecordingMusicBrainzModel.id,
            forceRefresh = false,
        ).let { recordingDetailsModel ->
            assertEquals(
                RecordingDetailsModel(
                    id = "108a3d66-d1ef-424d-a7cb-2f53a702ce45",
                    name = "スカイクラッドの観測者",
                    firstReleaseDate = "2023-09-06",
                    length = 273826,
                    artistCredits = listOf(
                        ArtistCreditUiModel(
                            artistId = roseliaArtistMusicBrainzModel.id,
                            name = roseliaArtistMusicBrainzModel.name,
                            joinPhrase = "×",
                        ),
                        ArtistCreditUiModel(
                            artistId = itouKanakoArtistMusicBrainzModel.id,
                            name = itouKanakoArtistMusicBrainzModel.name,
                            joinPhrase = "",
                        ),
                    ),
                    disambiguation = "",
                ),
                recordingDetailsModel,
            )
        }
        recordingRepository.lookupRecording(
            recordingId = skycladObserverCoverRecordingMusicBrainzModel.id,
            forceRefresh = true,
        ).let { recordingDetailsModel ->
            assertEquals(
                RecordingDetailsModel(
                    id = "108a3d66-d1ef-424d-a7cb-2f53a702ce45",
                    name = "スカイクラッドの観測者",
                    firstReleaseDate = "2023-09-06",
                    length = 273826,
                    artistCredits = listOf(
                        ArtistCreditUiModel(
                            artistId = roseliaArtistMusicBrainzModel.id,
                            name = roseliaArtistMusicBrainzModel.name,
                            joinPhrase = "×",
                        ),
                        ArtistCreditUiModel(
                            artistId = itouKanakoArtistMusicBrainzModel.id,
                            name = itouKanakoArtistMusicBrainzModel.name,
                            joinPhrase = "",
                        ),
                    ),
                    disambiguation = "changes will be ignored if recording is linked to multiple entities",
                ),
                recordingDetailsModel,
            )
        }
    }
}
