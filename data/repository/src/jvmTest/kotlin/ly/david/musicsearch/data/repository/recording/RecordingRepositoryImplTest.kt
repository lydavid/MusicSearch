package ly.david.musicsearch.data.repository.recording

import androidx.paging.testing.asSnapshot
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.ArtistCreditDao
import ly.david.musicsearch.data.database.dao.RecordingDao
import ly.david.musicsearch.data.database.dao.RelationDao
import ly.david.musicsearch.data.database.dao.RelationsMetadataDao
import ly.david.musicsearch.data.musicbrainz.models.UrlMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.common.ArtistCreditMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.ArtistMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.relation.Direction
import ly.david.musicsearch.data.musicbrainz.models.relation.RelationMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.relation.SerializableMusicBrainzEntity
import ly.david.musicsearch.data.repository.helpers.TEST_USERNAME
import ly.david.musicsearch.data.repository.helpers.TestListensListRepository
import ly.david.musicsearch.data.repository.helpers.TestRecordingRepository
import ly.david.musicsearch.data.repository.helpers.testDateTimeInThePast
import ly.david.musicsearch.data.repository.helpers.testListens
import ly.david.musicsearch.data.repository.helpers.track2ListenedAtMs
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.details.RecordingDetailsModel
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.image.ImageId
import ly.david.musicsearch.shared.domain.listen.ListenDao
import ly.david.musicsearch.shared.domain.listen.ListenListItemModel
import ly.david.musicsearch.shared.domain.listen.ListenRelease
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.time.Duration.Companion.days

class RecordingRepositoryImplTest :
    KoinTest,
    TestRecordingRepository,
    TestListensListRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    override val relationsMetadataDao: RelationsMetadataDao by inject()
    override val detailsMetadataDao: DetailsMetadataDao by inject()
    override val relationDao: RelationDao by inject()
    override val recordingDao: RecordingDao by inject()
    override val artistCreditDao: ArtistCreditDao by inject()
    override val aliasDao: AliasDao by inject()
    override val listenDao: ListenDao by inject()
    override val coroutineDispatchers: CoroutineDispatchers by inject()

    @Test
    fun `lookup is cached, and force refresh invalidates cache`() = runTest {
        val sparseRepository = createRecordingRepository(
            musicBrainzModel = RecordingMusicBrainzNetworkModel(
                id = "7e52152f-c71a-49b1-b98d-f95e04c44445",
                name = "導火",
                artistCredits = listOf(
                    ArtistCreditMusicBrainzModel(
                        artist = ArtistMusicBrainzNetworkModel(
                            id = "6825ace2-3563-4ac5-8d85-c7bf1334bd2c",
                            name = "月詠み",
                            disambiguation = "",
                            type = "Group",
                            sortName = "Tsukuyomi",
                        ),
                        name = "月詠み (something)",
                        joinPhrase = "",
                    ),
                ),
            ),
        )
        val sparseDetailsModel = sparseRepository.lookupRecording(
            recordingId = "7e52152f-c71a-49b1-b98d-f95e04c44445",
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        )
        Assert.assertEquals(
            RecordingDetailsModel(
                id = "7e52152f-c71a-49b1-b98d-f95e04c44445",
                name = "導火",
                artistCredits = persistentListOf(
                    ArtistCreditUiModel(
                        artistId = "6825ace2-3563-4ac5-8d85-c7bf1334bd2c",
                        name = "月詠み (something)",
                        joinPhrase = "",
                    ),
                ),
                lastUpdated = testDateTimeInThePast,
                listenBrainzUrl = "/track/7e52152f-c71a-49b1-b98d-f95e04c44445",
            ),
            sparseDetailsModel,
        )

        val allDataRepository = createRecordingRepository(
            musicBrainzModel = RecordingMusicBrainzNetworkModel(
                id = "7e52152f-c71a-49b1-b98d-f95e04c44445",
                name = "導火",
                video = true,
                length = 209000,
                artistCredits = listOf(
                    ArtistCreditMusicBrainzModel(
                        artist = ArtistMusicBrainzNetworkModel(
                            id = "6825ace2-3563-4ac5-8d85-c7bf1334bd2c",
                            name = "月詠み",
                            disambiguation = "",
                            type = "Group",
                            sortName = "Tsukuyomi",
                        ),
                        name = "月詠み",
                        joinPhrase = "",
                    ),
                ),
                relations = listOf(
                    RelationMusicBrainzModel(
                        type = "free streaming",
                        typeId = "7e41ef12-a124-4324-afdb-fdbae687a89c",
                        direction = Direction.FORWARD,
                        targetType = SerializableMusicBrainzEntity.URL,
                        url = UrlMusicBrainzModel(
                            id = "b5322490-3003-42e9-a043-d26a83bd1bbd",
                            resource = "https://music.youtube.com/watch?v=2vkgqu3-_Pk",
                        ),
                        attributes = listOf("video"),
                    ),
                ),
            ),
        )
        var allDataArtistDetailsModel = allDataRepository.lookupRecording(
            recordingId = "7e52152f-c71a-49b1-b98d-f95e04c44445",
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast.plus(2.days),
        )
        Assert.assertEquals(
            RecordingDetailsModel(
                id = "7e52152f-c71a-49b1-b98d-f95e04c44445",
                name = "導火",
                artistCredits = persistentListOf(
                    ArtistCreditUiModel(
                        artistId = "6825ace2-3563-4ac5-8d85-c7bf1334bd2c",
                        name = "月詠み (something)",
                        joinPhrase = "",
                    ),
                ),
                lastUpdated = testDateTimeInThePast,
                listenBrainzUrl = "/track/7e52152f-c71a-49b1-b98d-f95e04c44445",
            ),
            allDataArtistDetailsModel,
        )
        allDataArtistDetailsModel = allDataRepository.lookupRecording(
            recordingId = "7e52152f-c71a-49b1-b98d-f95e04c44445",
            forceRefresh = true,
            lastUpdated = testDateTimeInThePast.plus(3.days),
        )
        Assert.assertEquals(
            RecordingDetailsModel(
                id = "7e52152f-c71a-49b1-b98d-f95e04c44445",
                name = "導火",
                length = 209000,
                artistCredits = persistentListOf(
                    ArtistCreditUiModel(
                        artistId = "6825ace2-3563-4ac5-8d85-c7bf1334bd2c",
                        name = "月詠み",
                        joinPhrase = "",
                    ),
                ),
                video = true,
                lastUpdated = testDateTimeInThePast.plus(3.days),
                urls = persistentListOf(
                    RelationListItemModel(
                        id = "b5322490-3003-42e9-a043-d26a83bd1bbd_1",
                        linkedEntityId = "b5322490-3003-42e9-a043-d26a83bd1bbd",
                        type = "free streaming",
                        name = "https://music.youtube.com/watch?v=2vkgqu3-_Pk",
                        disambiguation = null,
                        attributes = "video",
                        linkedEntity = MusicBrainzEntityType.URL,
                        visited = true,
                        isForwardDirection = true,
                    ),
                ),
                listenBrainzUrl = "/track/7e52152f-c71a-49b1-b98d-f95e04c44445",
            ),
            allDataArtistDetailsModel,
        )
    }

    @Test
    fun `when id changes, listens are updated`() = runTest {
        val listensListRepository = createListensListRepository(
            response = testListens,
        )
        listensListRepository.observeListens(
            username = TEST_USERNAME,
            query = "color",
            recordingId = null,
            stopPrepending = false,
            stopAppending = false,
            onReachedLatest = {},
            onReachedOldest = {},
        ).asSnapshot().run {
            Assert.assertEquals(
                listOf(
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
                        imageUrl = "coverartarchive.org/release/0d516a93-061e-4a27-9cf7-f36e3a96f888/40524230813-250",
                        imageId = ImageId(2),
                        release = ListenRelease(
                            id = "0d516a93-061e-4a27-9cf7-f36e3a96f888",
                            name = "Persona 3 Reload Original Soundtrack",
                        ),
                    ),
                ),
                this,
            )
        }

        val recordingRepository = createRecordingRepository(
            musicBrainzModel = RecordingMusicBrainzNetworkModel(
                id = "e68e22b0-241e-4a6a-b4bf-0cfa8b83fda2",
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
        recordingRepository.lookupRecording(
            // the id we lookup is different from the id returned
            recordingId = "e68e22b0-241e-4a6a-b4bf-0cfa8b83fda1",
            forceRefresh = false,
            lastUpdated = testDateTimeInThePast,
        ).run {
            Assert.assertEquals(
                RecordingDetailsModel(
                    id = "e68e22b0-241e-4a6a-b4bf-0cfa8b83fda2",
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
                    listenBrainzUrl = "/track/e68e22b0-241e-4a6a-b4bf-0cfa8b83fda2",
                    listenCount = 1,
                    latestListensTimestampsMs = persistentListOf(1755100633000),
                ),
                this,
            )
        }

        listensListRepository.observeListens(
            username = TEST_USERNAME,
            query = "color",
            recordingId = null,
            stopPrepending = false,
            stopAppending = false,
            onReachedLatest = {},
            onReachedOldest = {},
        ).asSnapshot().run {
            Assert.assertEquals(
                listOf(
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
                        recordingId = "e68e22b0-241e-4a6a-b4bf-0cfa8b83fda2",
                        durationMs = 227240,
                        imageUrl = "coverartarchive.org/release/0d516a93-061e-4a27-9cf7-f36e3a96f888/40524230813-250",
                        imageId = ImageId(2),
                        release = ListenRelease(
                            id = "0d516a93-061e-4a27-9cf7-f36e3a96f888",
                            name = "Persona 3 Reload Original Soundtrack",
                        ),
                        visited = true,
                    ),
                ),
                this,
            )
        }
    }
}
