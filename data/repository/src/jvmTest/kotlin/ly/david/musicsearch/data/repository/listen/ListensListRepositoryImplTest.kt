package ly.david.musicsearch.data.repository.listen

import androidx.paging.testing.asSnapshot
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.TestScope
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
import ly.david.musicsearch.data.listenbrainz.api.AdditionalInfo
import ly.david.musicsearch.data.listenbrainz.api.ListenBrainzApi
import ly.david.musicsearch.data.listenbrainz.api.ListenBrainzArtist
import ly.david.musicsearch.data.listenbrainz.api.ListenBrainzListen
import ly.david.musicsearch.data.listenbrainz.api.ListensResponse
import ly.david.musicsearch.data.listenbrainz.api.MbidMapping
import ly.david.musicsearch.data.listenbrainz.api.Payload
import ly.david.musicsearch.data.listenbrainz.api.TokenValidationResponse
import ly.david.musicsearch.data.listenbrainz.api.TrackMetadata
import ly.david.musicsearch.data.musicbrainz.models.common.ArtistCreditMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.helpers.FilterTestCase
import ly.david.musicsearch.data.repository.helpers.TestMusicBrainzImageMetadataRepository
import ly.david.musicsearch.data.repository.helpers.TestRecordingRepository
import ly.david.musicsearch.data.repository.helpers.TestReleasesListRepository
import ly.david.musicsearch.data.repository.helpers.testDateTimeInThePast
import ly.david.musicsearch.data.repository.helpers.testFilter
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
import ly.david.musicsearch.shared.domain.listen.ListensListRepository
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.time.Instant

private const val USERNAME = "user"

@Suppress("MaxLineLength")
class ListensListRepositoryImplTest :
    KoinTest,
    TestMusicBrainzImageMetadataRepository,
    TestReleasesListRepository,
    TestRecordingRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val listenDao: ListenDao by inject()
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

    private fun createRepository(
        response: ListensResponse,
    ): ListensListRepository {
        return ListensListRepositoryImpl(
            listenDao = listenDao,
            listenBrainzApi = object : ListenBrainzApi {
                override suspend fun validateToken(token: String): TokenValidationResponse {
                    return TokenValidationResponse(valid = false)
                }

                override suspend fun getListensByUser(
                    username: String,
                    minTimestamp: Long?,
                    maxTimestamp: Long?,
                ): ListensResponse {
                    return response
                }
            },
            coroutineScope = TestScope(coroutineDispatchers.io),
        )
    }

    @Test
    fun listensByUser() = runTest {
        val track1ListenedAtS = 1756100634L
        val track2ListenedAtS = 1755100633L
        val track3ListenedAtS = 1755100632L
        val track4ListenedAtS = 1755100631L
        val listensListRepository = createRepository(
            response = ListensResponse(
                payload = Payload(
                    latest_listen_ts = track1ListenedAtS,
                    oldest_listen_ts = track4ListenedAtS,
                    listens = listOf(
                        // with mapping
                        ListenBrainzListen(
                            insertedAtS = 1755101240L,
                            listenedAtS = track1ListenedAtS,
                            recording_msid = "f5700f45-6003-40ee-9c01-3ea270c77cd3",
                            user_name = USERNAME,
                            track_metadata = TrackMetadata(
                                artist_name = "ano, Lilas",
                                track_name = "絶絶絶絶対聖域",
                                release_name = "絶絶絶絶対聖域",
                                additional_info = AdditionalInfo(
                                    duration_ms = 213868L,
                                    submission_client = "listenbrainz",
                                    music_service = "spotify.com",
                                    origin_url = "https://open.spotify.com/track/3n4p9wJEgt4szBc92wPwmu",
                                    spotify_album_artist_ids = listOf(
                                        "https://open.spotify.com/artist/7Il739Q5W4yJUYC3hfnX6z",
                                    ),
                                    spotify_album_id = "https://open.spotify.com/album/0qsnfQzcoZgycLGjJ9zKom",
                                    spotify_artist_ids = listOf(
                                        "https://open.spotify.com/artist/7Il739Q5W4yJUYC3hfnX6z",
                                        "https://open.spotify.com/artist/1qM11R4ylJyQiPJ0DffE9z",
                                    ),
                                    spotify_id = "https://open.spotify.com/track/3n4p9wJEgt4szBc92wPwmu",
                                ),
                                mbid_mapping = MbidMapping(
                                    recording_mbid = "57c4f7cb-99f1-4305-bf3e-9ea51cc243f0",
                                    recording_name = "絶絶絶絶対聖域",
                                    artists = listOf(
                                        ListenBrainzArtist(
                                            artist_credit_name = "ano",
                                            artist_mbid = "ebb4513e-4aab-4ac9-a949-14e77bb7b836",
                                            join_phrase = " feat. ",
                                        ),
                                        ListenBrainzArtist(
                                            artist_credit_name = "幾田りら",
                                            artist_mbid = "55e42264-ef27-49d8-93fd-29f930dc96e4",
                                            join_phrase = "",
                                        ),
                                    ),
                                    caa_id = 42143556739L,
                                    caa_release_mbid = "71c9f176-e6e3-4610-807d-b8a11b870df3",
                                    release_mbid = "837e8abc-01e9-4ef9-9a69-4a4e9d3455fa",
                                ),
                            ),
                        ),
                        ListenBrainzListen(
                            insertedAtS = 1755101240L,
                            listenedAtS = track2ListenedAtS,
                            recording_msid = "28f390ae-b7a3-4636-82bc-7d39a7348978",
                            user_name = USERNAME,
                            track_metadata = TrackMetadata(
                                artist_name = "高橋あず美, Lotus Juice, アトラスサウンドチーム, ATLUS GAME MUSIC",
                                track_name = "Color Your Night",
                                release_name = "Persona 3 Reload Original Soundtrack",
                                additional_info = AdditionalInfo(
                                    duration_ms = 227240,
                                    submission_client = "listenbrainz",
                                    music_service = "spotify.com",
                                    origin_url = "https://open.spotify.com/track/4pjFNyjGaoKgLTnndISP6V",
                                    spotify_album_artist_ids = listOf(
                                        "https://open.spotify.com/artist/4hFBhdNVZZuVk5FYThUwaN",
                                        "https://open.spotify.com/artist/7tUDDR0lAc9PLMPHPfzaqI",
                                    ),
                                    spotify_album_id = "https://open.spotify.com/album/20Bf2RVERC5Bc2eo3vyvJv",
                                    spotify_artist_ids = listOf(
                                        "https://open.spotify.com/artist/4VeqFgWkP7P9eEGwzPuXcM",
                                        "https://open.spotify.com/artist/0HM4KuHUJ5ww5DdOGi3FEf",
                                        "https://open.spotify.com/artist/4hFBhdNVZZuVk5FYThUwaN",
                                        "https://open.spotify.com/artist/7tUDDR0lAc9PLMPHPfzaqI",
                                    ),
                                    spotify_id = "https://open.spotify.com/track/4pjFNyjGaoKgLTnndISP6V",
                                ),
                                mbid_mapping = MbidMapping(
                                    recording_mbid = "e68e22b0-241e-4a6a-b4bf-0cfa8b83fda1",
                                    recording_name = "Color Your Night",
                                    artists = listOf(
                                        ListenBrainzArtist(
                                            artist_credit_name = "Lotus Juice",
                                            artist_mbid = "c731e592-2620-4f4c-859d-39e294b06b35",
                                            join_phrase = " & ",
                                        ),
                                        ListenBrainzArtist(
                                            artist_credit_name = "高橋あず美",
                                            artist_mbid = "2bd16069-0d18-4925-a4c0-cf99344cca0b",
                                            join_phrase = "",
                                        ),
                                    ),
                                    caa_id = 40524230813,
                                    caa_release_mbid = "0d516a93-061e-4a27-9cf7-f36e3a96f888",
                                    release_mbid = "0d516a93-061e-4a27-9cf7-f36e3a96f888",
                                ),
                            ),
                        ),
                        ListenBrainzListen(
                            insertedAtS = 1755101240L,
                            listenedAtS = track3ListenedAtS,
                            recording_msid = "9e164036-5379-4bbd-8a9b-fb7b9e697993",
                            user_name = USERNAME,
                            track_metadata = TrackMetadata(
                                artist_name = "高橋あず美, Lotus Juice, アトラスサウンドチーム, ATLUS GAME MUSIC",
                                track_name = "Full Moon Full Life",
                                release_name = "Persona 3 Reload Original Soundtrack",
                                additional_info = AdditionalInfo(
                                    duration_ms = 293493,
                                    submission_client = "listenbrainz",
                                    music_service = "spotify.com",
                                    origin_url = "https://open.spotify.com/track/3Jl2LQmRwbXEF2lO1RTvxn",
                                    spotify_album_artist_ids = listOf(
                                        "https://open.spotify.com/artist/4hFBhdNVZZuVk5FYThUwaN",
                                        "https://open.spotify.com/artist/7tUDDR0lAc9PLMPHPfzaqI",
                                    ),
                                    spotify_album_id = "https://open.spotify.com/album/20Bf2RVERC5Bc2eo3vyvJv",
                                    spotify_artist_ids = listOf(
                                        "https://open.spotify.com/artist/4VeqFgWkP7P9eEGwzPuXcM",
                                        "https://open.spotify.com/artist/0HM4KuHUJ5ww5DdOGi3FEf",
                                        "https://open.spotify.com/artist/4hFBhdNVZZuVk5FYThUwaN",
                                        "https://open.spotify.com/artist/7tUDDR0lAc9PLMPHPfzaqI",
                                    ),
                                    spotify_id = "https://open.spotify.com/track/3Jl2LQmRwbXEF2lO1RTvxn",
                                ),
                                mbid_mapping = MbidMapping(
                                    recording_mbid = "c4090c59-be0c-4a79-b76d-5e2669e0cd4c",
                                    recording_name = "Full Moon Full Life",
                                    artists = listOf(
                                        ListenBrainzArtist(
                                            artist_credit_name = "Lotus Juice",
                                            artist_mbid = "c731e592-2620-4f4c-859d-39e294b06b35",
                                            join_phrase = " & ",
                                        ),
                                        ListenBrainzArtist(
                                            artist_credit_name = "高橋あず美",
                                            artist_mbid = "2bd16069-0d18-4925-a4c0-cf99344cca0b",
                                            join_phrase = "",
                                        ),
                                    ),
                                    caa_id = 40524230813,
                                    caa_release_mbid = "0d516a93-061e-4a27-9cf7-f36e3a96f888",
                                    release_mbid = "0d516a93-061e-4a27-9cf7-f36e3a96f888",
                                ),
                            ),
                        ),
                        // minimum without mapping
                        ListenBrainzListen(
                            insertedAtS = 1755101240L,
                            listenedAtS = track4ListenedAtS,
                            recording_msid = "e46e0ad5-6b2d-4ab1-aa68-acd29dd204f2",
                            user_name = USERNAME,
                            track_metadata = TrackMetadata(
                                artist_name = "Tsukuyomi",
                                track_name = "Absolute zero",
                            ),
                        ),
                    ),
                ),
            ),
        )

        testFilter(
            pagingFlowProducer = { query ->
                listensListRepository.observeListens(
                    username = USERNAME,
                    query = query,
                    recordingId = null,
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
                            id = "1756100634",
                            text = "Monday, August 25, 2025",
                        ),
                        ListenListItemModel(
                            id = "1756100634000_f5700f45-6003-40ee-9c01-3ea270c77cd3_user",
                            name = "絶絶絶絶対聖域",
                            formattedArtistCredits = "ano feat. 幾田りら",
                            listenedAt = Instant.fromEpochSeconds(track1ListenedAtS),
                            recordingId = "57c4f7cb-99f1-4305-bf3e-9ea51cc243f0",
                            durationMs = 213868,
                            imageUrl = "https://coverartarchive.org/release/71c9f176-e6e3-4610-807d-b8a11b870df3/42143556739-250",
                            imageId = ImageId(1),
                            release = ListenRelease(
                                id = "71c9f176-e6e3-4610-807d-b8a11b870df3",
                                name = "絶絶絶絶対聖域",
                            ),
                        ),
                        ListSeparator(
                            id = "1755100633",
                            text = "Wednesday, August 13, 2025",
                        ),
                        ListenListItemModel(
                            id = "1755100633000_28f390ae-b7a3-4636-82bc-7d39a7348978_user",
                            name = "Color Your Night",
                            formattedArtistCredits = "Lotus Juice & 高橋あず美",
                            listenedAt = Instant.fromEpochSeconds(track2ListenedAtS),
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
                            id = "1755100632000_9e164036-5379-4bbd-8a9b-fb7b9e697993_user",
                            name = "Full Moon Full Life",
                            formattedArtistCredits = "Lotus Juice & 高橋あず美",
                            listenedAt = Instant.fromEpochSeconds(track3ListenedAtS),
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
                            id = "1755100631000_e46e0ad5-6b2d-4ab1-aa68-acd29dd204f2_user",
                            name = "Absolute zero",
                            formattedArtistCredits = "Tsukuyomi",
                            listenedAt = Instant.fromEpochSeconds(track4ListenedAtS),
                        ),
                    ),
                ),
                FilterTestCase(
                    description = "filter by track name",
                    query = "full",
                    expectedResult = listOf(
                        ListSeparator(
                            id = "1755100632",
                            text = "Wednesday, August 13, 2025",
                        ),
                        ListenListItemModel(
                            id = "1755100632000_9e164036-5379-4bbd-8a9b-fb7b9e697993_user",
                            name = "Full Moon Full Life",
                            formattedArtistCredits = "Lotus Juice & 高橋あず美",
                            listenedAt = Instant.fromEpochSeconds(track3ListenedAtS),
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
                            id = "1756100634",
                            text = "Monday, August 25, 2025",
                        ),
                        ListenListItemModel(
                            id = "1756100634000_f5700f45-6003-40ee-9c01-3ea270c77cd3_user",
                            name = "絶絶絶絶対聖域",
                            formattedArtistCredits = "ano feat. 幾田りら",
                            listenedAt = Instant.fromEpochSeconds(track1ListenedAtS),
                            recordingId = "57c4f7cb-99f1-4305-bf3e-9ea51cc243f0",
                            durationMs = 213868,
                            imageUrl = "https://coverartarchive.org/release/71c9f176-e6e3-4610-807d-b8a11b870df3/42143556739-250",
                            imageId = ImageId(1),
                            release = ListenRelease(
                                id = "71c9f176-e6e3-4610-807d-b8a11b870df3",
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
                            id = "1755100633",
                            text = "Wednesday, August 13, 2025",
                        ),
                        ListenListItemModel(
                            id = "1755100633000_28f390ae-b7a3-4636-82bc-7d39a7348978_user",
                            name = "Color Your Night",
                            formattedArtistCredits = "Lotus Juice & 高橋あず美",
                            listenedAt = Instant.fromEpochSeconds(track2ListenedAtS),
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
                            id = "1755100632000_9e164036-5379-4bbd-8a9b-fb7b9e697993_user",
                            name = "Full Moon Full Life",
                            formattedArtistCredits = "Lotus Juice & 高橋あず美",
                            listenedAt = Instant.fromEpochSeconds(track3ListenedAtS),
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
                    username = USERNAME,
                    query = query,
                    recordingId = "e68e22b0-241e-4a6a-b4bf-0cfa8b83fda1",
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
                            id = "1755100633",
                            text = "Wednesday, August 13, 2025",
                        ),
                        ListenListItemModel(
                            id = "1755100633000_28f390ae-b7a3-4636-82bc-7d39a7348978_user",
                            name = "Color Your Night",
                            formattedArtistCredits = "Lotus Juice & 高橋あず美",
                            listenedAt = Instant.fromEpochSeconds(track2ListenedAtS),
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
                    username = USERNAME,
                    query = query,
                    recordingId = "",
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
                            id = "1755100631",
                            text = "Wednesday, August 13, 2025",
                        ),
                        ListenListItemModel(
                            id = "1755100631000_e46e0ad5-6b2d-4ab1-aa68-acd29dd204f2_user",
                            name = "Absolute zero",
                            formattedArtistCredits = "Tsukuyomi",
                            listenedAt = Instant.fromEpochSeconds(track4ListenedAtS),
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
            fakeBrowseUsername = USERNAME,
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
