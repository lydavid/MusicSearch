package ly.david.musicsearch.data.repository.listen

import androidx.paging.testing.asSnapshot
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
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.RecordingDetailsModel
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.image.ImageId
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.image.ImageUrlDao
import ly.david.musicsearch.shared.domain.image.ImagesSortOption
import ly.david.musicsearch.shared.domain.listen.ListenDao
import ly.david.musicsearch.shared.domain.listen.ListenListItemModel
import ly.david.musicsearch.shared.domain.listen.ListenRelease
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

@Suppress("MaxLineLength")
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

    @Test
    fun listensByUser() = runTest {
        val listensListRepository = createListensListRepository(
            response = testListens,
        )

        testFilter(
            pagingFlowProducer = { query ->
                listensListRepository.observeListens(
                    username = TEST_USERNAME,
                    query = query,
                    recordingId = null,
                    stopPrepending = false,
                    stopAppending = false,
                    onReachedLatest = {},
                    onReachedOldest = {},
                )
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
                        ListenListItemModel(
                            listenedAtMs = track1ListenedAtMs,
                            username = "user",
                            recordingMessybrainzId = "f5700f45-6003-40ee-9c01-3ea270c77cd3",
                            name = "絶絶絶絶対聖域",
                            formattedArtistCredits = "ano feat. 幾田りら",
                            recordingId = "57c4f7cb-99f1-4305-bf3e-9ea51cc243f0",
                            durationMs = 213868,
                            imageUrl = "https://coverartarchive.org/release/71c9f176-e6e3-4610-807d-b8a11b870df3/42143556739-250",
                            imageId = ImageId(1),
                            release = ListenRelease(
                                id = "837e8abc-01e9-4ef9-9a69-4a4e9d3455fa",
                                name = "絶絶絶絶対聖域",
                            ),
                        ),
                        ListSeparator(
                            id = track2ListenedAtMs.toString(),
                            text = "Wednesday, August 13, 2025",
                        ),
                        ListenListItemModel(
                            listenedAtMs = track2ListenedAtMs,
                            username = "user",
                            recordingMessybrainzId = "28f390ae-b7a3-4636-82bc-7d39a7348978",
                            name = "Color Your Night",
                            formattedArtistCredits = "Lotus Juice & 高橋あず美",
                            recordingId = "e68e22b0-241e-4a6a-b4bf-0cfa8b83fda1",
                            durationMs = 227240,
                            imageUrl = "https://coverartarchive.org/release/0d516a93-061e-4a27-9cf7-f36e3a96f888/40524230813-250",
                            imageId = ImageId(2),
                            release = ListenRelease(
                                id = "0d516a93-061e-4a27-9cf7-f36e3a96f888",
                                name = "Persona 3 Reload Original Soundtrack",
                            ),
                        ),
                        ListenListItemModel(
                            username = "user",
                            recordingMessybrainzId = "9e164036-5379-4bbd-8a9b-fb7b9e697993",
                            name = "Full Moon Full Life",
                            formattedArtistCredits = "Lotus Juice & 高橋あず美",
                            listenedAtMs = track3ListenedAtMs,
                            recordingId = "c4090c59-be0c-4a79-b76d-5e2669e0cd4c",
                            durationMs = 293493,
                            imageUrl = "https://coverartarchive.org/release/0d516a93-061e-4a27-9cf7-f36e3a96f888/40524230813-250",
                            imageId = ImageId(2),
                            release = ListenRelease(
                                id = "0d516a93-061e-4a27-9cf7-f36e3a96f888",
                                name = "Persona 3 Reload Original Soundtrack",
                            ),
                        ),
                        ListenListItemModel(
                            username = "user",
                            recordingMessybrainzId = "e46e0ad5-6b2d-4ab1-aa68-acd29dd204f2",
                            name = "Absolute zero",
                            formattedArtistCredits = "Tsukuyomi",
                            listenedAtMs = track4ListenedAtMs,
                        ),
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
                        ListenListItemModel(
                            username = "user",
                            recordingMessybrainzId = "9e164036-5379-4bbd-8a9b-fb7b9e697993",
                            name = "Full Moon Full Life",
                            formattedArtistCredits = "Lotus Juice & 高橋あず美",
                            listenedAtMs = track3ListenedAtMs,
                            recordingId = "c4090c59-be0c-4a79-b76d-5e2669e0cd4c",
                            durationMs = 293493,
                            imageUrl = "https://coverartarchive.org/release/0d516a93-061e-4a27-9cf7-f36e3a96f888/40524230813-250",
                            imageId = ImageId(2),
                            release = ListenRelease(
                                id = "0d516a93-061e-4a27-9cf7-f36e3a96f888",
                                name = "Persona 3 Reload Original Soundtrack",
                            ),
                        ),
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
                        ListenListItemModel(
                            username = "user",
                            recordingMessybrainzId = "f5700f45-6003-40ee-9c01-3ea270c77cd3",
                            name = "絶絶絶絶対聖域",
                            formattedArtistCredits = "ano feat. 幾田りら",
                            listenedAtMs = track1ListenedAtMs,
                            recordingId = "57c4f7cb-99f1-4305-bf3e-9ea51cc243f0",
                            durationMs = 213868,
                            imageUrl = "https://coverartarchive.org/release/71c9f176-e6e3-4610-807d-b8a11b870df3/42143556739-250",
                            imageId = ImageId(1),
                            release = ListenRelease(
                                id = "837e8abc-01e9-4ef9-9a69-4a4e9d3455fa",
                                name = "絶絶絶絶対聖域",
                            ),
                        ),
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
                        ListenListItemModel(
                            username = "user",
                            recordingMessybrainzId = "28f390ae-b7a3-4636-82bc-7d39a7348978",
                            name = "Color Your Night",
                            formattedArtistCredits = "Lotus Juice & 高橋あず美",
                            listenedAtMs = track2ListenedAtMs,
                            recordingId = "e68e22b0-241e-4a6a-b4bf-0cfa8b83fda1",
                            durationMs = 227240,
                            imageUrl = "https://coverartarchive.org/release/0d516a93-061e-4a27-9cf7-f36e3a96f888/40524230813-250",
                            imageId = ImageId(2),
                            release = ListenRelease(
                                id = "0d516a93-061e-4a27-9cf7-f36e3a96f888",
                                name = "Persona 3 Reload Original Soundtrack",
                            ),
                        ),
                        ListenListItemModel(
                            username = "user",
                            recordingMessybrainzId = "9e164036-5379-4bbd-8a9b-fb7b9e697993",
                            name = "Full Moon Full Life",
                            formattedArtistCredits = "Lotus Juice & 高橋あず美",
                            listenedAtMs = track3ListenedAtMs,
                            recordingId = "c4090c59-be0c-4a79-b76d-5e2669e0cd4c",
                            durationMs = 293493,
                            imageUrl = "https://coverartarchive.org/release/0d516a93-061e-4a27-9cf7-f36e3a96f888/40524230813-250",
                            imageId = ImageId(2),
                            release = ListenRelease(
                                id = "0d516a93-061e-4a27-9cf7-f36e3a96f888",
                                name = "Persona 3 Reload Original Soundtrack",
                            ),
                        ),
                    ),
                ),
            ),
        )

        testFilter(
            pagingFlowProducer = { query ->
                listensListRepository.observeListens(
                    username = TEST_USERNAME,
                    query = query,
                    recordingId = "e68e22b0-241e-4a6a-b4bf-0cfa8b83fda1",
                    stopPrepending = false,
                    stopAppending = false,
                    onReachedLatest = {},
                    onReachedOldest = {},
                )
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
                        ListenListItemModel(
                            username = "user",
                            recordingMessybrainzId = "28f390ae-b7a3-4636-82bc-7d39a7348978",
                            name = "Color Your Night",
                            formattedArtistCredits = "Lotus Juice & 高橋あず美",
                            listenedAtMs = track2ListenedAtMs,
                            recordingId = "e68e22b0-241e-4a6a-b4bf-0cfa8b83fda1",
                            durationMs = 227240,
                            imageUrl = "https://coverartarchive.org/release/0d516a93-061e-4a27-9cf7-f36e3a96f888/40524230813-250",
                            imageId = ImageId(2),
                            release = ListenRelease(
                                id = "0d516a93-061e-4a27-9cf7-f36e3a96f888",
                                name = "Persona 3 Reload Original Soundtrack",
                            ),
                        ),
                    ),
                ),
            ),
        )

        testFilter(
            pagingFlowProducer = { query ->
                listensListRepository.observeListens(
                    username = TEST_USERNAME,
                    query = query,
                    recordingId = "",
                    stopPrepending = false,
                    stopAppending = false,
                    onReachedLatest = {},
                    onReachedOldest = {},
                )
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
                        ListenListItemModel(
                            username = "user",
                            recordingMessybrainzId = "e46e0ad5-6b2d-4ab1-aa68-acd29dd204f2",
                            name = "Absolute zero",
                            formattedArtistCredits = "Tsukuyomi",
                            listenedAtMs = track4ListenedAtMs,
                        ),
                    ),
                ),
            ),
        )

        testImageExists()
        testReleaseStubExists()
        testRecordingShowsListens()
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
                ImageMetadata(
                    imageId = ImageId(2L),
                    thumbnailUrl = "https://coverartarchive.org/release/0d516a93-061e-4a27-9cf7-f36e3a96f888/40524230813-250",
                    largeUrl = "https://coverartarchive.org/release/0d516a93-061e-4a27-9cf7-f36e3a96f888/40524230813-1200",
                    mbid = "0d516a93-061e-4a27-9cf7-f36e3a96f888",
                    name = "Persona 3 Reload Original Soundtrack",
                    disambiguation = "",
                    entity = MusicBrainzEntityType.RELEASE,
                ),
                ImageMetadata(
                    imageId = ImageId(1L),
                    thumbnailUrl = "https://coverartarchive.org/release/71c9f176-e6e3-4610-807d-b8a11b870df3/42143556739-250",
                    largeUrl = "https://coverartarchive.org/release/71c9f176-e6e3-4610-807d-b8a11b870df3/42143556739-1200",
                    mbid = "71c9f176-e6e3-4610-807d-b8a11b870df3",
                    name = "絶絶絶絶対聖域",
                    disambiguation = "",
                    entity = MusicBrainzEntityType.RELEASE,
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
            listFilters = ListFilters(),
            now = testDateTimeInThePast,
        ).asSnapshot()
        Assert.assertEquals(
            listOf(
                ReleaseListItemModel(
                    id = "0d516a93-061e-4a27-9cf7-f36e3a96f888",
                    name = "Persona 3 Reload Original Soundtrack",
                    imageId = ImageId(2L),
                    imageUrl = "https://coverartarchive.org/release/0d516a93-061e-4a27-9cf7-f36e3a96f888/40524230813-250",
                ),
                ReleaseListItemModel(
                    id = "71c9f176-e6e3-4610-807d-b8a11b870df3",
                    name = "絶絶絶絶対聖域",
                    imageId = ImageId(1L),
                    imageUrl = "https://coverartarchive.org/release/71c9f176-e6e3-4610-807d-b8a11b870df3/42143556739-250",
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
        val recordingDetailsModel = recordingRepository.lookupRecording(
            recordingId = id,
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
}
