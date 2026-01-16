package ly.david.musicsearch.data.repository.recording

import androidx.paging.testing.asSnapshot
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.itouKanakoArtistMusicBrainzModel
import ly.david.data.test.roseliaArtistMusicBrainzModel
import ly.david.data.test.skycladObserverCoverRecordingListItemModel
import ly.david.data.test.skycladObserverCoverRecordingMusicBrainzModel
import ly.david.data.test.skycladObserverRecordingListItemModel
import ly.david.data.test.skycladObserverRecordingMusicBrainzModel
import ly.david.data.test.skycladObserverWorkMusicBrainzModel
import ly.david.data.test.underPressureRecordingListItemModel
import ly.david.data.test.underPressureRecordingMusicBrainzModel
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.RecordingDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.RelationsMetadataDao
import ly.david.musicsearch.data.listenbrainz.api.AdditionalInfo
import ly.david.musicsearch.data.listenbrainz.api.ListenBrainzArtist
import ly.david.musicsearch.data.listenbrainz.api.ListenBrainzListen
import ly.david.musicsearch.data.listenbrainz.api.ListensResponse
import ly.david.musicsearch.data.listenbrainz.api.MbidMapping
import ly.david.musicsearch.data.listenbrainz.api.Payload
import ly.david.musicsearch.data.listenbrainz.api.TrackMetadata
import ly.david.musicsearch.data.repository.helpers.FilterTestCase
import ly.david.musicsearch.data.repository.helpers.TEST_USERNAME
import ly.david.musicsearch.data.repository.helpers.TestListensListRepository
import ly.david.musicsearch.data.repository.helpers.TestRecordingRepository
import ly.david.musicsearch.data.repository.helpers.TestRecordingsListRepository
import ly.david.musicsearch.data.repository.helpers.testDateTimeInThePast
import ly.david.musicsearch.data.repository.helpers.testFilter
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.RecordingDetailsModel
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.listen.ListenDao
import ly.david.musicsearch.shared.domain.listitem.CollectionListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

class RecordingsListRepositoryImplTest :
    KoinTest,
    TestRecordingRepository,
    TestRecordingsListRepository,
    TestListensListRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    override val recordingDao: RecordingDao by inject()
    override val artistCreditDao: ArtistCreditDao by inject()
    override val relationsMetadataDao: RelationsMetadataDao by inject()
    override val detailsMetadataDao: DetailsMetadataDao by inject()
    override val relationDao: RelationDao by inject()
    private val collectionDao: CollectionDao by inject()
    override val browseRemoteMetadataDao: BrowseRemoteMetadataDao by inject()
    override val collectionEntityDao: CollectionEntityDao by inject()
    override val aliasDao: AliasDao by inject()
    override val coroutineDispatchers: CoroutineDispatchers by inject()
    override val listenDao: ListenDao by inject()
    private val collectionId = "950cea33-433e-497f-93bb-a05a393a2c02"

    @Test
    fun setupRecordingsByCollection() = runTest {
        val recordings = listOf(
            underPressureRecordingMusicBrainzModel,
            skycladObserverRecordingMusicBrainzModel,
        )
        val recordingsListRepository = createRecordingsListRepository(
            recordings = recordings,
        )

        collectionDao.insertLocal(
            collection = CollectionListItemModel(
                id = collectionId,
                isRemote = false,
                name = "recordings",
                entity = MusicBrainzEntityType.RECORDING,
            ),
        )
        collectionEntityDao.addAllToCollection(
            collectionId = collectionId,
            entityIds = recordings.map { it.id },
        )

        testFilter(
            pagingFlowProducer = { query ->
                recordingsListRepository.observeRecordings(
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
                        underPressureRecordingListItemModel.copy(
                            collected = true,
                        ),
                        skycladObserverRecordingListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
                FilterTestCase(
                    description = "filter by name",
                    query = "ス",
                    expectedResult = listOf(
                        skycladObserverRecordingListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
                FilterTestCase(
                    description = "filter by artist credit",
                    query = "&",
                    expectedResult = listOf(
                        underPressureRecordingListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
                FilterTestCase(
                    description = "filter by first release date",
                    query = "2009",
                    expectedResult = listOf(
                        skycladObserverRecordingListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
            ),
        )
    }

    @Test
    fun setUpRecordingsByArtist() = runTest {
        val entityId = itouKanakoArtistMusicBrainzModel.id
        val entity = MusicBrainzEntityType.ARTIST
        val recordings = listOf(
            skycladObserverRecordingMusicBrainzModel,
            skycladObserverCoverRecordingMusicBrainzModel,
        )
        val recordingsListRepository = createRecordingsListRepository(
            recordings = recordings,
        )
        val browseMethod = BrowseMethod.ByEntity(
            entityId = entityId,
            entityType = entity,
        )

        testFilter(
            pagingFlowProducer = { query ->
                recordingsListRepository.observeRecordings(
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
        val entity = MusicBrainzEntityType.WORK
        val recordings = listOf(
            skycladObserverRecordingMusicBrainzModel,
            skycladObserverCoverRecordingMusicBrainzModel,
        )
        val recordingsListRepository = createRecordingsListRepository(
            recordings = recordings,
        )
        val browseMethod = BrowseMethod.ByEntity(
            entityId = entityId,
            entityType = entity,
        )

        testFilter(
            pagingFlowProducer = { query ->
                recordingsListRepository.observeRecordings(
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

        val recordingsListRepository = createRecordingsListRepository(
            recordings = listOf(),
        )
        testFilter(
            pagingFlowProducer = { query ->
                recordingsListRepository.observeRecordings(
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
                        skycladObserverRecordingListItemModel.copy(
                            collected = true,
                        ),
                        skycladObserverCoverRecordingListItemModel,
                        underPressureRecordingListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
                FilterTestCase(
                    description = "filter by artist name",
                    query = "bowie",
                    expectedResult = listOf(
                        underPressureRecordingListItemModel.copy(
                            collected = true,
                        ),
                    ),
                ),
            ),
        )
    }

    @Test
    fun `refreshing recordings does not delete the recording`() = runTest {
        setUpRecordingsByArtist()
        setupRecordingsByWork()

        val modifiedRecordings = listOf(
            skycladObserverRecordingMusicBrainzModel.copy(
                id = "new-id-is-considered-a-different-recording",
            ),
            skycladObserverCoverRecordingMusicBrainzModel.copy(
                disambiguation = "changes will still show up",
            ),
        )
        val recordingsListRepository = createRecordingsListRepository(
            recordings = modifiedRecordings,
        )

        // refresh
        recordingsListRepository.observeRecordings(
            browseMethod = BrowseMethod.ByEntity(
                entityId = itouKanakoArtistMusicBrainzModel.id,
                entityType = MusicBrainzEntityType.ARTIST,
            ),
            listFilters = ListFilters(),
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                listOf(
                    skycladObserverRecordingListItemModel.copy(
                        id = "new-id-is-considered-a-different-recording",
                    ),
                    skycladObserverCoverRecordingListItemModel.copy(
                        disambiguation = "changes will still show up",
                    ),
                ),
                this,
            )
        }

        // browse by another entity
        recordingsListRepository.observeRecordings(
            browseMethod = BrowseMethod.ByEntity(
                entityId = skycladObserverWorkMusicBrainzModel.id,
                entityType = MusicBrainzEntityType.WORK,
            ),
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    skycladObserverRecordingListItemModel,
                    skycladObserverCoverRecordingListItemModel.copy(
                        disambiguation = "changes will still show up",
                    ),
                ),
                this,
            )
        }

        // both old and new version of recording exists
        recordingsListRepository.observeRecordings(
            browseMethod = BrowseMethod.All,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    skycladObserverRecordingListItemModel,
                    skycladObserverCoverRecordingListItemModel.copy(
                        disambiguation = "changes will still show up",
                    ),
                    skycladObserverRecordingListItemModel.copy(
                        id = "new-id-is-considered-a-different-recording",
                    ),
                ),
                this,
            )
        }

        recordingsListRepository.observeRecordings(
            browseMethod = BrowseMethod.ByEntity(
                entityId = skycladObserverWorkMusicBrainzModel.id,
                entityType = MusicBrainzEntityType.WORK,
            ),
            listFilters = ListFilters(),
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                listOf(
                    skycladObserverRecordingListItemModel.copy(
                        id = "new-id-is-considered-a-different-recording",
                    ),
                    skycladObserverCoverRecordingListItemModel.copy(
                        disambiguation = "changes will still show up",
                    ),
                ),
                this,
            )
        }

        // both versions will continue to exist
        recordingsListRepository.observeRecordings(
            browseMethod = BrowseMethod.All,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    skycladObserverRecordingListItemModel,
                    skycladObserverCoverRecordingListItemModel.copy(
                        disambiguation = "changes will still show up",
                    ),
                    skycladObserverRecordingListItemModel.copy(
                        id = "new-id-is-considered-a-different-recording",
                    ),
                ),
                this,
            )
        }

        // now visit the recording and refresh it
        val recordingRepository = createRecordingRepository(
            skycladObserverCoverRecordingMusicBrainzModel.copy(
                disambiguation = "different change will show up",
            ),
        )
        // because we have never visited this, we upsert it, so any changes will show up right away
        recordingRepository.lookupRecording(
            recordingId = skycladObserverCoverRecordingMusicBrainzModel.id,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        ).let { recordingDetailsModel ->
            assertEquals(
                RecordingDetailsModel(
                    id = "108a3d66-d1ef-424d-a7cb-2f53a702ce45",
                    name = "スカイクラッドの観測者",
                    firstReleaseDate = "2023-09-06",
                    length = 273826,
                    artistCredits = persistentListOf(
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
                    disambiguation = "different change will show up",
                    isrcs = persistentListOf(
                        "JPR562300374",
                    ),
                    lastUpdated = testDateTimeInThePast,
                    listenBrainzUrl = "/track/108a3d66-d1ef-424d-a7cb-2f53a702ce45",
                ),
                recordingDetailsModel,
            )
        }
        recordingRepository.lookupRecording(
            recordingId = skycladObserverCoverRecordingMusicBrainzModel.id,
            forceRefresh = true,
            lastUpdated = testDateTimeInThePast,
        ).let { recordingDetailsModel ->
            assertEquals(
                RecordingDetailsModel(
                    id = "108a3d66-d1ef-424d-a7cb-2f53a702ce45",
                    name = "スカイクラッドの観測者",
                    firstReleaseDate = "2023-09-06",
                    length = 273826,
                    artistCredits = persistentListOf(
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
                    disambiguation = "different change will show up",
                    isrcs = persistentListOf(
                        "JPR562300374",
                    ),
                    lastUpdated = testDateTimeInThePast,
                    listenBrainzUrl = "/track/108a3d66-d1ef-424d-a7cb-2f53a702ce45",
                ),
                recordingDetailsModel,
            )
        }
        recordingsListRepository.observeRecordings(
            browseMethod = BrowseMethod.All,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    skycladObserverRecordingListItemModel,
                    skycladObserverRecordingListItemModel.copy(
                        id = "new-id-is-considered-a-different-recording",
                    ),
                    skycladObserverCoverRecordingListItemModel.copy(
                        disambiguation = "different change will show up",
                        visited = true,
                    ),
                ),
                this,
            )
        }
    }

    @Test
    fun `refreshing recordings that also belong to collection do not delete the recording`() = runTest {
        setUpRecordingsByArtist()
        setupRecordingsByCollection()

        val modifiedRecordings = listOf(
            skycladObserverCoverRecordingMusicBrainzModel.copy(
                id = "new-id-is-considered-a-different-recording",
            ),
        )
        val recordingsListRepository = createRecordingsListRepository(
            recordings = modifiedRecordings,
        )

        // refresh
        recordingsListRepository.observeRecordings(
            browseMethod = BrowseMethod.ByEntity(
                entityId = itouKanakoArtistMusicBrainzModel.id,
                entityType = MusicBrainzEntityType.ARTIST,
            ),
            listFilters = ListFilters(),
        ).asSnapshot {
            refresh()
        }.run {
            assertEquals(
                listOf(
                    skycladObserverCoverRecordingListItemModel.copy(
                        id = "new-id-is-considered-a-different-recording",
                    ),
                ),
                this,
            )
        }

        // other entities remain unchanged
        recordingsListRepository.observeRecordings(
            browseMethod = BrowseMethod.ByEntity(
                entityId = collectionId,
                entityType = MusicBrainzEntityType.COLLECTION,
            ),
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    underPressureRecordingListItemModel.copy(
                        collected = true,
                    ),
                    skycladObserverRecordingListItemModel.copy(
                        collected = true,
                    ),
                ),
                this,
            )
        }
    }

    @Test
    fun `aliases does not multiply listen count`() = runTest {
        val entityId = itouKanakoArtistMusicBrainzModel.id
        val entity = MusicBrainzEntityType.ARTIST
        val browseMethod = BrowseMethod.ByEntity(
            entityId = entityId,
            entityType = entity,
        )

        val recordings = listOf(
            skycladObserverRecordingMusicBrainzModel,
            skycladObserverCoverRecordingMusicBrainzModel,
        )
        val recordingsListRepository = createRecordingsListRepository(
            recordings = recordings,
            fakeBrowseUsername = TEST_USERNAME,
        )

        val listensListRepository = createListensListRepository(
            response = ListensResponse(
                payload = Payload(
                    latest_listen_ts = 1755101240L,
                    oldest_listen_ts = 1755101240L,
                    listens = listOf(
                        ListenBrainzListen(
                            insertedAtS = 1755101240L,
                            listenedAtS = 1755101240L,
                            recording_msid = "10821143-ab67-4cfa-9ceb-c837bf8b4bdf",
                            user_name = TEST_USERNAME,
                            track_metadata = TrackMetadata(
                                artist_name = "いとうかなこ",
                                track_name = "スカイクラッドの観測者",
                                release_name = "ChaosAttractor",
                                additional_info = AdditionalInfo(
                                    duration_ms = 275640L,
                                    submission_client = "listenbrainz",
                                    music_service = "spotify.com",
                                    origin_url = "https://open.spotify.com/track/3Y0W7Lxg1X4cbyvtmdCHzL",
                                    spotify_album_artist_ids = listOf(
                                        "https://open.spotify.com/artist/2d12dVIZQZk9CKhEsezaoN",
                                    ),
                                    spotify_album_id = "https://open.spotify.com/album/0yHtsepi9vEUIxuHq5ohz7",
                                    spotify_artist_ids = listOf(
                                        "https://open.spotify.com/artist/7Il739Q5W4yJUYC3hfnX6z",
                                        "https://open.spotify.com/artist/1qM11R4ylJyQiPJ0DffE9z",
                                    ),
                                    spotify_id = "https://open.spotify.com/track/3Y0W7Lxg1X4cbyvtmdCHzL",
                                ),
                                mbid_mapping = MbidMapping(
                                    recording_mbid = "6a8fc477-9b12-4001-9387-f5d936b05503",
                                    recording_name = "絶絶絶絶対聖域",
                                    artists = listOf(
                                        ListenBrainzArtist(
                                            artist_credit_name = "いとうかなこ",
                                            artist_mbid = "eee65fbc-ead0-4c01-b385-a6f563c418d3",
                                            join_phrase = "",
                                        ),
                                    ),
                                    caa_id = 12397242767,
                                    caa_release_mbid = "2387c59b-62c4-4752-b1fa-64f126ed0c8c",
                                ),
                            ),
                        ),
                    ),
                ),
            ),
        )
        listensListRepository.observeListens(
            username = TEST_USERNAME,
            query = "",
            entityFacet = null,
            stopPrepending = false,
            stopAppending = false,
            onReachedLatest = {},
            onReachedOldest = {},
        ).asSnapshot()

        recordingsListRepository.observeRecordings(
            browseMethod = browseMethod,
            listFilters = ListFilters(),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    skycladObserverRecordingListItemModel.copy(
                        listenCount = 1,
                    ),
                    skycladObserverCoverRecordingListItemModel.copy(
                        listenCount = 0,
                    ),
                ),
                this,
            )
        }

        recordingsListRepository.observeRecordings(
            browseMethod = browseMethod,
            listFilters = ListFilters(
                query = "observe",
            ),
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    skycladObserverRecordingListItemModel.copy(
                        listenCount = 1,
                    ),
                ),
                this,
            )
        }
    }
}
