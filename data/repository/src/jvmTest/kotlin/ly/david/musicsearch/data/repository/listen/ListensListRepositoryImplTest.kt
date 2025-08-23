package ly.david.musicsearch.data.repository.listen

import androidx.paging.testing.asSnapshot
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.listenbrainz.api.AdditionalInfo
import ly.david.musicsearch.data.listenbrainz.api.ListenBrainzApi
import ly.david.musicsearch.data.listenbrainz.api.ListenBrainzArtist
import ly.david.musicsearch.data.listenbrainz.api.ListenBrainzListen
import ly.david.musicsearch.data.listenbrainz.api.ListensResponse
import ly.david.musicsearch.data.listenbrainz.api.MbidMapping
import ly.david.musicsearch.data.listenbrainz.api.Payload
import ly.david.musicsearch.data.listenbrainz.api.TrackMetadata
import ly.david.musicsearch.data.repository.helpers.FilterTestCase
import ly.david.musicsearch.data.repository.helpers.TestMusicBrainzImageMetadataRepository
import ly.david.musicsearch.data.repository.helpers.TestReleasesListRepository
import ly.david.musicsearch.data.repository.helpers.testFilter
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.ListFilters
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.image.ImageId
import ly.david.musicsearch.shared.domain.image.ImageMetadata
import ly.david.musicsearch.shared.domain.image.ImageUrlDao
import ly.david.musicsearch.shared.domain.image.ImagesSortOption
import ly.david.musicsearch.shared.domain.listen.ListenDao
import ly.david.musicsearch.shared.domain.listen.ListenListItemModel
import ly.david.musicsearch.shared.domain.listen.ListensListRepository
import ly.david.musicsearch.shared.domain.listitem.ReleaseListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.time.Instant

class ListensListRepositoryImplTest :
    KoinTest,
    TestMusicBrainzImageMetadataRepository,
    TestReleasesListRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    private val listenDao: ListenDao by inject()
    override val imageUrlDao: ImageUrlDao by inject()
    override val coroutineDispatchers: CoroutineDispatchers by inject()
    override val releaseDao: ReleaseDao by inject()
    override val collectionEntityDao: CollectionEntityDao by inject()
    override val browseRemoteMetadataDao: BrowseRemoteMetadataDao by inject()
    override val aliasDao: AliasDao by inject()

    private fun createRepository(
        response: ListensResponse,
    ): ListensListRepository {
        return ListensListRepositoryImpl(
            listenDao = listenDao,
            listenBrainzApi = object : ListenBrainzApi {
                override suspend fun getListensByUser(
                    username: String,
                    minTimestamp: Long?,
                    maxTimestamp: Long?,
                ): ListensResponse {
                    return response
                }
            },
        )
    }

    @Test
    fun listensByUser() = runTest {
        val caaReleaseMbid = "71c9f176-e6e3-4610-807d-b8a11b870df3"
        val listensListRepository = createRepository(
            response = ListensResponse(
                payload = Payload(
                    listens = listOf(
                        // with mapping
                        ListenBrainzListen(
                            insertedAtS = 1755101240L,
                            listenedAtS = 1755100634L,
                            recording_msid = "f5700f45-6003-40ee-9c01-3ea270c77cd3",
                            user_name = "user",
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
                                    caa_release_mbid = caaReleaseMbid,
                                    release_mbid = "837e8abc-01e9-4ef9-9a69-4a4e9d3455fa",
                                ),
                            ),
                        ),
                        // without mapping
                        ListenBrainzListen(
                            insertedAtS = 1755101240L,
                            listenedAtS = 1755100633L,
                            recording_msid = "9e164036-5379-4bbd-8a9b-fb7b9e697993",
                            user_name = "user",
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
                            ),
                        ),
                    ),
                ),
            ),
        )

        testFilter(
            pagingFlowProducer = { query ->
                listensListRepository.observeListens(
                    username = "user",
                    query = query,
                )
            },
            testCases = listOf(
                FilterTestCase(
                    description = "no filter",
                    query = "",
                    expectedResult = listOf(
                        ListenListItemModel(
                            id = "1755100634000_f5700f45-6003-40ee-9c01-3ea270c77cd3_user",
                            name = "絶絶絶絶対聖域",
                            formattedArtistCredits = "ano feat. 幾田りら",
                            listenedAt = Instant.fromEpochSeconds(1755100634),
                            recordingId = "57c4f7cb-99f1-4305-bf3e-9ea51cc243f0",
                            imageUrl = "https://coverartarchive.org/release/71c9f176-e6e3-4610-807d-b8a11b870df3/42143556739-250",
                            imageId = ImageId(1),
                        ),
                        ListenListItemModel(
                            id = "1755100633000_9e164036-5379-4bbd-8a9b-fb7b9e697993_user",
                            name = "Full Moon Full Life",
                            formattedArtistCredits = "高橋あず美, Lotus Juice, アトラスサウンドチーム, ATLUS GAME MUSIC",
                            listenedAt = Instant.fromEpochSeconds(1755100633),
                            recordingId = null,
                            imageUrl = null,
                            imageId = null,
                        ),
                    ),
                ),
                FilterTestCase(
                    description = "filter name",
                    query = "full",
                    expectedResult = listOf(
                        ListenListItemModel(
                            id = "1755100633000_9e164036-5379-4bbd-8a9b-fb7b9e697993_user",
                            name = "Full Moon Full Life",
                            formattedArtistCredits = "高橋あず美, Lotus Juice, アトラスサウンドチーム, ATLUS GAME MUSIC",
                            listenedAt = Instant.fromEpochSeconds(1755100633),
                            recordingId = null,
                            imageUrl = null,
                            imageId = null,
                        ),
                    ),
                ),
                FilterTestCase(
                    description = "filter artist",
                    query = "feat",
                    expectedResult = listOf(
                        ListenListItemModel(
                            id = "1755100634000_f5700f45-6003-40ee-9c01-3ea270c77cd3_user",
                            name = "絶絶絶絶対聖域",
                            formattedArtistCredits = "ano feat. 幾田りら",
                            listenedAt = Instant.fromEpochSeconds(1755100634),
                            recordingId = "57c4f7cb-99f1-4305-bf3e-9ea51cc243f0",
                            imageUrl = "https://coverartarchive.org/release/71c9f176-e6e3-4610-807d-b8a11b870df3/42143556739-250",
                            imageId = ImageId(1),
                        ),
                    ),
                ),
            ),
        )

        testImageExists(caaReleaseMbid)

        testReleaseStubExists(caaReleaseMbid)
    }

    @Suppress("MaxLineLength")
    private suspend fun testReleaseStubExists(caaReleaseMbid: String) {
        val releasesListRepository = createReleasesListRepository(
            releases = emptyList(),
        )
        val releases = releasesListRepository.observeReleases(
            browseMethod = BrowseMethod.All,
            listFilters = ListFilters(),
        ).asSnapshot()
        Assert.assertEquals(
            listOf(
                ReleaseListItemModel(
                    id = caaReleaseMbid,
                    name = "絶絶絶絶対聖域",
                    imageId = ImageId(1L),
                    imageUrl = "https://coverartarchive.org/release/71c9f176-e6e3-4610-807d-b8a11b870df3/42143556739-250",
                ),
            ),
            releases,
        )
    }

    @Suppress("MaxLineLength")
    private suspend fun testImageExists(caaReleaseMbid: String) {
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
                    imageId = ImageId(1L),
                    thumbnailUrl = "https://coverartarchive.org/release/71c9f176-e6e3-4610-807d-b8a11b870df3/42143556739-250",
                    largeUrl = "https://coverartarchive.org/release/71c9f176-e6e3-4610-807d-b8a11b870df3/42143556739-1200",
                    mbid = caaReleaseMbid,
                    name = "絶絶絶絶対聖域",
                    disambiguation = "",
                    entity = MusicBrainzEntityType.RELEASE,
                ),
            ),
            imageMetadataList,
        )
    }
}
