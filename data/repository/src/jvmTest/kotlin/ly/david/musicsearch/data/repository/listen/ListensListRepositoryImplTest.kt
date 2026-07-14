package ly.david.musicsearch.data.repository.listen

import androidx.paging.testing.asSnapshot
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.RecordingDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.RelationsMetadataDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.database.dao.TagDao
import ly.david.musicsearch.data.listenbrainz.api.ListenBrainzApi
import ly.david.musicsearch.data.listenbrainz.api.ManualMappingResponse
import ly.david.musicsearch.data.listenbrainz.api.RecordingMetadata
import ly.david.musicsearch.data.musicbrainz.models.common.ArtistCreditMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.helpers.FilterTestCase
import ly.david.musicsearch.data.repository.helpers.TEST_USERNAME
import ly.david.musicsearch.data.repository.helpers.TestListensListRepository
import ly.david.musicsearch.data.repository.helpers.TestMusicBrainzImageMetadataRepository
import ly.david.musicsearch.data.repository.helpers.TestRecordingRepository
import ly.david.musicsearch.data.repository.helpers.TestReleasesListRepository
import ly.david.musicsearch.data.repository.helpers.testDateTimeInThePast
import ly.david.musicsearch.data.repository.helpers.testFilter
import ly.david.musicsearch.data.repository.helpers.testListens
import ly.david.musicsearch.data.repository.helpers.track1ListenedAtMs
import ly.david.musicsearch.data.repository.helpers.track2ListenedAtMs
import ly.david.musicsearch.data.repository.helpers.track3ListenedAtMs
import ly.david.musicsearch.data.repository.helpers.track4ListenedAtMs
import ly.david.musicsearch.data.repository.helpers.track5ListenedAtMs
import ly.david.musicsearch.data.repository.helpers.track6ListenedAtMs
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.RecordingDetailsModel
import ly.david.musicsearch.shared.domain.error.Feedback
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.image.ImageId
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.image.ImageMetadataWithEntity
import ly.david.musicsearch.shared.domain.image.ImageUrlDao
import ly.david.musicsearch.shared.domain.image.ImagesSortOption
import ly.david.musicsearch.shared.domain.list.ListFilters
import ly.david.musicsearch.shared.domain.listen.ListenDao
import ly.david.musicsearch.shared.domain.listen.ListenListItemModel
import ly.david.musicsearch.shared.domain.listen.ListenRelease
import ly.david.musicsearch.shared.domain.listen.ListensListFeedback
import ly.david.musicsearch.shared.domain.listen.ListensListRepository
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

@Suppress("MaxLineLength", "LongMethod")
class ListensListRepositoryImplTest :
    KoinTest,
    TestListensListRepository,
    TestMusicBrainzImageMetadataRepository,
    TestReleasesListRepository,
    TestRecordingRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    override val listenDao: ListenDao by inject()
    override val imageUrlDao: ImageUrlDao by inject()
    override val coroutineDispatchers: CoroutineDispatchers by inject()
    override val releaseDao: ReleaseDao by inject()
    override val collectionEntityDao: CollectionEntityDao by inject()
    override val browseRemoteMetadataDao: BrowseRemoteMetadataDao by inject()
    override val relationsMetadataDao: RelationsMetadataDao by inject()
    override val detailsMetadataDao: DetailsMetadataDao by inject()
    override val relationDao: RelationDao by inject()
    override val recordingDao: RecordingDao by inject()
    override val artistCreditDao: ArtistCreditDao by inject()
    override val aliasDao: AliasDao by inject()
    override val tagDao: TagDao by inject()

    private val listen1 = ListenListItemModel(
        listenedAtMs = track1ListenedAtMs,
        username = "user",
        recordingMessybrainzId = "f5700f45-6003-40ee-9c01-3ea270c77cd3",
        recordingName = "絶絶絶絶対聖域",
        unmappedTrackName = "絶絶絶絶対聖域",
        separateArtistCredits = persistentListOf(
            ArtistCreditUiModel(
                name = "ano",
                artistId = "ebb4513e-4aab-4ac9-a949-14e77bb7b836",
                joinPhrase = " feat. ",
            ),
            ArtistCreditUiModel(
                name = "幾田りら",
                artistId = "55e42264-ef27-49d8-93fd-29f930dc96e4",
                joinPhrase = "",
            ),
        ),
        unmappedFormattedArtistCredits = "ano, Lilas",
        recordingId = "57c4f7cb-99f1-4305-bf3e-9ea51cc243f0",
        recordingDurationMs = 213868,
        unmappedDurationMs = 213868,
        imageMetadata = ImageMetadata.InternetArchive(
            imageId = ImageId(1),
            rawThumbnailUrl = "coverartarchive.org/release/71c9f176-e6e3-4610-807d-b8a11b870df3/42143556739-250",
        ),
        release = ListenRelease(
            id = "837e8abc-01e9-4ef9-9a69-4a4e9d3455fa",
            unmappedName = "絶絶絶絶対聖域",
        ),
    )
    private val listen2 = ListenListItemModel(
        listenedAtMs = track2ListenedAtMs,
        username = "user",
        recordingMessybrainzId = "28f390ae-b7a3-4636-82bc-7d39a7348978",
        recordingName = "Color Your Night",
        unmappedTrackName = "Color Your Night",
        separateArtistCredits = persistentListOf(
            ArtistCreditUiModel(
                artistId = "c731e592-2620-4f4c-859d-39e294b06b35",
                name = "Lotus Juice",
                joinPhrase = " & ",
            ),
            ArtistCreditUiModel(
                artistId = "2bd16069-0d18-4925-a4c0-cf99344cca0b",
                name = "高橋あず美",
                joinPhrase = "",
            ),
        ),
        unmappedFormattedArtistCredits = "高橋あず美, Lotus Juice, アトラスサウンドチーム, ATLUS GAME MUSIC",
        recordingId = "e68e22b0-241e-4a6a-b4bf-0cfa8b83fda1",
        recordingDurationMs = 227240,
        unmappedDurationMs = 227240,
        imageMetadata = ImageMetadata.InternetArchive(
            imageId = ImageId(2),
            rawThumbnailUrl = "coverartarchive.org/release/0d516a93-061e-4a27-9cf7-f36e3a96f888/40524230813-250",
        ),
        release = ListenRelease(
            id = "0d516a93-061e-4a27-9cf7-f36e3a96f888",
            mappedName = "Persona 3 Reload Original Soundtrack",
            unmappedName = "Persona 3 Reload Original Soundtrack",
        ),
    )
    private val listen3 = ListenListItemModel(
        username = "user",
        recordingMessybrainzId = "9e164036-5379-4bbd-8a9b-fb7b9e697993",
        recordingName = "Full Moon Full Life",
        unmappedTrackName = "Full Moon Full Life",
        separateArtistCredits = persistentListOf(
            ArtistCreditUiModel(
                artistId = "c731e592-2620-4f4c-859d-39e294b06b35",
                name = "Lotus Juice",
                joinPhrase = " & ",
            ),
            ArtistCreditUiModel(
                artistId = "2bd16069-0d18-4925-a4c0-cf99344cca0b",
                name = "高橋あず美",
                joinPhrase = "",
            ),
        ),
        unmappedFormattedArtistCredits = "高橋あず美, Lotus Juice, アトラスサウンドチーム, ATLUS GAME MUSIC",
        listenedAtMs = track3ListenedAtMs,
        recordingId = "c4090c59-be0c-4a79-b76d-5e2669e0cd4c",
        recordingDurationMs = 293493,
        unmappedDurationMs = 293493,
        imageMetadata = ImageMetadata.InternetArchive(
            imageId = ImageId(2),
            rawThumbnailUrl = "coverartarchive.org/release/0d516a93-061e-4a27-9cf7-f36e3a96f888/40524230813-250",
        ),
        release = ListenRelease(
            id = "0d516a93-061e-4a27-9cf7-f36e3a96f888",
            mappedName = "Persona 3 Reload Original Soundtrack",
            unmappedName = "Persona 3 Reload Original Soundtrack",
        ),
    )
    private val listen4 = ListenListItemModel(
        username = "user",
        recordingMessybrainzId = "e46e0ad5-6b2d-4ab1-aa68-acd29dd204f2",
        unmappedTrackName = "Absolute zero",
        unmappedFormattedArtistCredits = "Tsukuyomi",
        release = ListenRelease(
            unmappedName = "Absolute zero",
        ),
        listenedAtMs = track4ListenedAtMs,
    )
    private val listen5 = ListenListItemModel(
        listenedAtMs = track5ListenedAtMs,
        username = "user",
        recordingMessybrainzId = "10821143-ab67-4cfa-9ceb-c837bf8b4bdf",
        recordingName = "スカイクラッドの観測者",
        unmappedTrackName = "スカイクラッドの観測者",
        disambiguation = "",
        separateArtistCredits = persistentListOf(
            ArtistCreditUiModel(
                name = "いとうかなこ",
                artistId = "eee65fbc-ead0-4c01-b385-a6f563c418d3",
                joinPhrase = "",
            ),
        ),
        unmappedFormattedArtistCredits = "いとうかなこ",
        recordingId = "6a8fc477-9b12-4001-9387-f5d936b05503",
        recordingDurationMs = 275640,
        unmappedDurationMs = 275640,
        imageMetadata = ImageMetadata.InternetArchive(
            imageId = ImageId(value = 4),
            rawThumbnailUrl = "coverartarchive.org/release/2387c59b-62c4-4752-b1fa-64f126ed0c8c/12397242767-250",
        ),
        visited = false,
        release = ListenRelease(
            mappedName = "ChaosAttractor",
            unmappedName = "ChaosAttractor",
            id = "2387c59b-62c4-4752-b1fa-64f126ed0c8c",
            visited = false,
        ),
    )
    private val listen6 = ListenListItemModel(
        listenedAtMs = track6ListenedAtMs,
        username = "user",
        recordingMessybrainzId = "77f971a8-6748-4314-9513-dffbc0969724",
        recordingName = "スカイクラッドの観測者",
        unmappedTrackName = "スカイクラッドの観測者",
        disambiguation = "",
        separateArtistCredits = persistentListOf(
            ArtistCreditUiModel(
                name = "Roselia",
                artistId = "adea3c3d-a84d-4f9e-ac0b-1ef71a8947a5",
                joinPhrase = "×",
            ),
            ArtistCreditUiModel(
                name = "いとうかなこ",
                artistId = "eee65fbc-ead0-4c01-b385-a6f563c418d3",
                joinPhrase = "",
            ),
        ),
        unmappedFormattedArtistCredits = "Roselia, いとうかなこ",
        recordingId = "cb10d0b9-26a5-4f84-93bb-ddcffa39c170",
        recordingDurationMs = 273866,
        unmappedDurationMs = 273866,
        imageMetadata = ImageMetadata.InternetArchive(
            imageId = ImageId(value = 5),
            rawThumbnailUrl = "coverartarchive.org/release/06fecdc4-dbfa-484f-a03b-5da975fadf0e/36678276363-250",
        ),
        visited = false,
        release = ListenRelease(
            mappedName = "バンドリ！ ガールズバンドパーティ！ カバーコレクションVol.8",
            unmappedName = "バンドリ！ ガールズバンドパーティ！ カバーコレクションVol.8",
            id = "06fecdc4-dbfa-484f-a03b-5da975fadf0e",
            visited = false,
        ),
    )

    private val listenBrainzApi: ListenBrainzApi = mockk()

    private val listensListRepository: ListensListRepository
        get() = createListensListRepository(
            listenBrainzApi = listenBrainzApi,
        )

    @Test
    fun listensByUser() {
        coEvery {
            listenBrainzApi.getListensByUser(
                username = any(),
                minTimestamp = any(),
                maxTimestamp = any(),
                count = any(),
            )
        } returns testListens
        runTest {
            testFilter(
                pagingFlowProducer = { query ->
                    listensListRepository.observeListens(
                        username = TEST_USERNAME,
                        query = query,
                        entityFacet = null,
                        maxDateTimeEpochMilliseconds = null,
                        stopPrepending = false,
                        stopAppending = false,
                        onReachedLatest = {},
                    ) {}
                },
                testCases = listOf(
                    FilterTestCase(
                        description = "no filter",
                        query = "",
                        expectedResult = listOf(
                            ListSeparator(
                                id = track1ListenedAtMs.toString(),
                                text = "Monday, August 25, 2025",
                            ),
                            listen1,
                            ListSeparator(
                                id = track2ListenedAtMs.toString(),
                                text = "Wednesday, August 13, 2025",
                            ),
                            listen2,
                            listen3,
                            listen4,
                            listen5,
                            listen6,
                        ),
                    ),
                    FilterTestCase(
                        description = "filter by track name",
                        query = "full",
                        expectedResult = listOf(
                            ListSeparator(
                                id = track3ListenedAtMs.toString(),
                                text = "Wednesday, August 13, 2025",
                            ),
                            listen3,
                        ),
                    ),
                    FilterTestCase(
                        description = "filter by artist",
                        query = "feat",
                        expectedResult = listOf(
                            ListSeparator(
                                id = track1ListenedAtMs.toString(),
                                text = "Monday, August 25, 2025",
                            ),
                            listen1,
                        ),
                    ),
                    FilterTestCase(
                        description = "filter by release name",
                        query = "persona",
                        expectedResult = listOf(
                            ListSeparator(
                                id = track2ListenedAtMs.toString(),
                                text = "Wednesday, August 13, 2025",
                            ),
                            listen2,
                            listen3,
                        ),
                    ),
                ),
            )

            testFilter(
                pagingFlowProducer = { query ->
                    listensListRepository.observeListens(
                        username = TEST_USERNAME,
                        query = query,
                        entityFacet = MusicBrainzEntity(
                            id = "e68e22b0-241e-4a6a-b4bf-0cfa8b83fda1",
                            type = MusicBrainzEntityType.RECORDING,
                        ),
                        maxDateTimeEpochMilliseconds = null,
                        stopPrepending = false,
                        stopAppending = false,
                        onReachedLatest = {},
                    ) {}
                },
                testCases = listOf(
                    FilterTestCase(
                        description = "facet by recording",
                        query = "",
                        expectedResult = listOf(
                            ListSeparator(
                                id = track2ListenedAtMs.toString(),
                                text = "Wednesday, August 13, 2025",
                            ),
                            listen2,
                        ),
                    ),
                ),
            )

            testFilter(
                pagingFlowProducer = { query ->
                    listensListRepository.observeListens(
                        username = TEST_USERNAME,
                        query = query,
                        entityFacet = MusicBrainzEntity(
                            id = "",
                            type = MusicBrainzEntityType.RECORDING,
                        ),
                        null,
                        stopPrepending = false,
                        stopAppending = false,
                        onReachedLatest = {},
                    ) {}
                },
                testCases = listOf(
                    FilterTestCase(
                        description = "facet by unlinked recording",
                        query = "",
                        expectedResult = listOf(
                            ListSeparator(
                                id = track4ListenedAtMs.toString(),
                                text = "Wednesday, August 13, 2025",
                            ),
                            listen4,
                        ),
                    ),
                ),
            )

            testImageExists()
            testReleaseStubExists()
            testRecordingShowsListens()
            testManualMapping()
        }
    }

    private suspend fun testImageExists() {
        val imageRepository = createMusicBrainzImageMetadataRepository(
            coverArtUrlsProducer = { _, _ ->
                emptyList()
            },
        )
        val imageMetadataList = imageRepository.observeAllImageMetadata(
            mbid = null,
            query = "",
            sortOption = ImagesSortOption.RECENTLY_ADDED,
        ).asSnapshot()
        Assert.assertEquals(
            listOf(
                ImageMetadataWithEntity(
                    imageMetadata = ImageMetadata.InternetArchive(
                        imageId = ImageId(value = 5),
                        rawThumbnailUrl = "coverartarchive.org/release/06fecdc4-dbfa-484f-a03b-5da975fadf0e/36678276363-250",
                        rawLargeUrl = "coverartarchive.org/release/06fecdc4-dbfa-484f-a03b-5da975fadf0e/36678276363-1200",
                    ),
                    musicBrainzEntity = MusicBrainzEntity(
                        id = "06fecdc4-dbfa-484f-a03b-5da975fadf0e",
                        type = MusicBrainzEntityType.RELEASE,
                    ),
                    name = "バンドリ！ ガールズバンドパーティ！ カバーコレクションVol.8",
                    disambiguation = "",
                ),
                ImageMetadataWithEntity(
                    imageMetadata = ImageMetadata.InternetArchive(
                        imageId = ImageId(value = 4),
                        rawThumbnailUrl = "coverartarchive.org/release/2387c59b-62c4-4752-b1fa-64f126ed0c8c/12397242767-250",
                        rawLargeUrl = "coverartarchive.org/release/2387c59b-62c4-4752-b1fa-64f126ed0c8c/12397242767-1200",
                    ),
                    musicBrainzEntity = MusicBrainzEntity(
                        id = "2387c59b-62c4-4752-b1fa-64f126ed0c8c",
                        type = MusicBrainzEntityType.RELEASE,
                    ),
                    name = "ChaosAttractor",
                    disambiguation = "",
                ),
                ImageMetadataWithEntity(
                    imageMetadata = ImageMetadata.InternetArchive(
                        imageId = ImageId(2L),
                        rawThumbnailUrl = "coverartarchive.org/release/0d516a93-061e-4a27-9cf7-f36e3a96f888/40524230813-250",
                        rawLargeUrl = "coverartarchive.org/release/0d516a93-061e-4a27-9cf7-f36e3a96f888/40524230813-1200",
                    ),
                    musicBrainzEntity = MusicBrainzEntity(
                        id = "0d516a93-061e-4a27-9cf7-f36e3a96f888",
                        type = MusicBrainzEntityType.RELEASE,
                    ),
                    name = "Persona 3 Reload Original Soundtrack",
                    disambiguation = "",
                ),
                ImageMetadataWithEntity(
                    imageMetadata = ImageMetadata.InternetArchive(
                        imageId = ImageId(1L),
                        rawThumbnailUrl = "coverartarchive.org/release/71c9f176-e6e3-4610-807d-b8a11b870df3/42143556739-250",
                        rawLargeUrl = "coverartarchive.org/release/71c9f176-e6e3-4610-807d-b8a11b870df3/42143556739-1200",
                    ),
                    musicBrainzEntity = MusicBrainzEntity(
                        id = "71c9f176-e6e3-4610-807d-b8a11b870df3",
                        type = MusicBrainzEntityType.RELEASE,
                    ),
                    name = "絶絶絶絶対聖域",
                    disambiguation = "",
                ),
            ),
            imageMetadataList,
        )
    }

    private suspend fun testReleaseStubExists() {
        val releasesListRepository = createReleasesListRepository(
            releases = emptyList(),
        )
        val releases = releasesListRepository.observeReleases(
            browseMethod = BrowseMethod.All,
            listFilters = ListFilters.Releases(),
            now = testDateTimeInThePast,
        ).asSnapshot()
        Assert.assertEquals(
            listOf(
                ReleaseListItemModel(
                    id = "71c9f176-e6e3-4610-807d-b8a11b870df3",
                    name = "絶絶絶絶対聖域",
                    imageMetadata = ImageMetadata.InternetArchive(
                        rawThumbnailUrl = "coverartarchive.org/release/71c9f176-e6e3-4610-807d-b8a11b870df3/42143556739-250",
                        imageId = ImageId(1L),
                    ),
                ),
                ReleaseListItemModel(
                    id = "0d516a93-061e-4a27-9cf7-f36e3a96f888",
                    name = "Persona 3 Reload Original Soundtrack",
                    imageMetadata = ImageMetadata.InternetArchive(
                        rawThumbnailUrl = "coverartarchive.org/release/0d516a93-061e-4a27-9cf7-f36e3a96f888/40524230813-250",
                        imageId = ImageId(2L),
                    ),
                ),
                ReleaseListItemModel(
                    id = "2387c59b-62c4-4752-b1fa-64f126ed0c8c",
                    name = "ChaosAttractor",
                    imageMetadata = ImageMetadata.InternetArchive(
                        rawThumbnailUrl = "coverartarchive.org/release/2387c59b-62c4-4752-b1fa-64f126ed0c8c/12397242767-250",
                        imageId = ImageId(4L),
                    ),
                ),
                ReleaseListItemModel(
                    id = "06fecdc4-dbfa-484f-a03b-5da975fadf0e",
                    name = "バンドリ！ ガールズバンドパーティ！ カバーコレクションVol.8",
                    imageMetadata = ImageMetadata.InternetArchive(
                        imageId = ImageId(5L),
                        rawThumbnailUrl = "coverartarchive.org/release/06fecdc4-dbfa-484f-a03b-5da975fadf0e/36678276363-250",
                    ),
                ),
            ),
            releases,
        )
    }

    @Suppress("LongMethod")
    private suspend fun testRecordingShowsListens() {
        val id = "e68e22b0-241e-4a6a-b4bf-0cfa8b83fda1"
        val recordingRepository = createRecordingRepository(
            musicBrainzModel = RecordingMusicBrainzNetworkModel(
                id = id,
                name = "Color Your Night",
                artistCredits = listOf(
                    ArtistCreditMusicBrainzModel(
                        artist = ArtistMusicBrainzNetworkModel(
                            id = "c731e592-2620-4f4c-859d-39e294b06b35",
                            name = "Lotus Juice",
                        ),
                        name = "Lotus Juice",
                        joinPhrase = " & ",
                    ),
                    ArtistCreditMusicBrainzModel(
                        artist = ArtistMusicBrainzNetworkModel(
                            id = "2bd16069-0d18-4925-a4c0-cf99344cca0b",
                            name = "高橋あず美",
                        ),
                        name = "高橋あず美",
                        joinPhrase = "",
                    ),
                ),
                firstReleaseDate = "2024-02-02",
                length = 227240,
                isrcs = listOf("JPK652300116"),
            ),
            fakeBrowseUsername = TEST_USERNAME,
        )
        val recordingDetailsModel = recordingRepository.lookupEntity(
            entityId = id,
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        )
        Assert.assertEquals(
            RecordingDetailsModel(
                id = "e68e22b0-241e-4a6a-b4bf-0cfa8b83fda1",
                name = "Color Your Night",
                artistCredits = persistentListOf(
                    ArtistCreditUiModel(
                        artistId = "c731e592-2620-4f4c-859d-39e294b06b35",
                        name = "Lotus Juice",
                        joinPhrase = " & ",
                    ),
                    ArtistCreditUiModel(
                        artistId = "2bd16069-0d18-4925-a4c0-cf99344cca0b",
                        name = "高橋あず美",
                        joinPhrase = "",
                    ),
                ),
                firstReleaseDate = "2024-02-02",
                length = 227240,
                isrcs = persistentListOf("JPK652300116"),
                lastUpdated = testDateTimeInThePast,
                listenBrainzUrl = "/track/e68e22b0-241e-4a6a-b4bf-0cfa8b83fda1",
                listenCount = 1,
                latestListensTimestampsMs = persistentListOf(1755100633000),
            ),
            recordingDetailsModel,
        )
    }

    private suspend fun testManualMapping() {
        val recordingMessyBrainzId = "e46e0ad5-6b2d-4ab1-aa68-acd29dd204f2"
        val recordingMbid = "6489abdb-6169-4a62-977a-8d7775f10a54"
        coEvery {
            listenBrainzApi.submitManualMapping(
                recordingMessyBrainzId = recordingMessyBrainzId,
                recordingMusicBrainzId = recordingMbid,
            )
        } returns Unit
        coEvery {
            listenBrainzApi.getManualMapping(recordingMessyBrainzId = recordingMessyBrainzId)
        } returns ManualMappingResponse(
            mapping = ManualMappingResponse.Mapping(
                recordingMbid = recordingMbid,
                recordingMsid = recordingMessyBrainzId,
            ),
        )
        coEvery {
            listenBrainzApi.getRecordingMetadata(recordingMusicBrainzId = recordingMbid)
        } returns RecordingMetadata(
            artistCredit = RecordingMetadata.ArtistCredit(
                name = "月詠み",
                artists = listOf(
                    RecordingMetadata.ArtistCredit.Artist(
                        artistMbid = "6825ace2-3563-4ac5-8d85-c7bf1334bd2c",
                        name = "月詠み",
                        joinPhrase = "",
                    ),
                ),
            ),
            recording = RecordingMetadata.Recording(
                name = "絶対零度",
                length = 222000,
            ),
            release = RecordingMetadata.Release(
                name = "絶対零度",
                caaId = 42428705569,
                caaReleaseMbid = "ac3e8592-b411-4b69-937d-9f58f8e4935f",
                mbid = "ac3e8592-b411-4b69-937d-9f58f8e4935f",
            ),
        )

        val feedback: Feedback<ListensListFeedback> = listensListRepository.submitManualMapping(
            recordingMessyBrainzId = recordingMessyBrainzId,
            rawRecordingMusicBrainzId = recordingMbid,
        )
        Assert.assertEquals(
            ListensListFeedback.Updated,
            feedback.data,
        )

        testFilter(
            pagingFlowProducer = { query ->
                listensListRepository.observeListens(
                    username = TEST_USERNAME,
                    query = query,
                    entityFacet = null,
                    maxDateTimeEpochMilliseconds = null,
                    stopPrepending = false,
                    stopAppending = false,
                    onReachedLatest = {},
                ) {}
            },
            testCases = listOf(
                FilterTestCase(
                    description = "tsukuyomi",
                    query = "tsukuyomi",
                    expectedResult = listOf(
                        ListSeparator(
                            id = track4ListenedAtMs.toString(),
                            text = "Wednesday, August 13, 2025",
                        ),
                        listen4.copy(
                            recordingId = "6489abdb-6169-4a62-977a-8d7775f10a54",
                            recordingName = "絶対零度",
                            separateArtistCredits = persistentListOf(
                                ArtistCreditUiModel(
                                    name = "月詠み",
                                    artistId = "6825ace2-3563-4ac5-8d85-c7bf1334bd2c",
                                    joinPhrase = "",
                                ),
                            ),
                            recordingDurationMs = 222000,
                            imageMetadata = ImageMetadata.InternetArchive(
                                imageId = ImageId(value = 46),
                                rawThumbnailUrl = "coverartarchive.org/release/ac3e8592-b411-4b69-937d-9f58f8e4935f/42428705569-250",
                            ),
                            release = ListenRelease(
                                id = "ac3e8592-b411-4b69-937d-9f58f8e4935f",
                                mappedName = "絶対零度",
                                unmappedName = "Absolute zero",
                            ),
                        ),
                    ),
                ),
            ),
        )
    }
}
