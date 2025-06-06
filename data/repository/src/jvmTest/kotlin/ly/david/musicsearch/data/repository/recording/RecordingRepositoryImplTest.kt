package ly.david.musicsearch.data.repository.recording

import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
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
import ly.david.musicsearch.data.repository.helpers.TestRecordingRepository
import ly.david.musicsearch.data.repository.helpers.testDateTimeInThePast
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.details.RecordingDetailsModel
import ly.david.musicsearch.shared.domain.history.DetailsMetadataDao
import ly.david.musicsearch.shared.domain.listitem.RelationListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.time.Duration.Companion.days

class RecordingRepositoryImplTest : KoinTest, TestRecordingRepository {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    override val relationsMetadataDao: RelationsMetadataDao by inject()
    override val detailsMetadataDao: DetailsMetadataDao by inject()
    override val relationDao: RelationDao by inject()
    override val recordingDao: RecordingDao by inject()
    override val artistCreditDao: ArtistCreditDao by inject()

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
        assertEquals(
            RecordingDetailsModel(
                id = "7e52152f-c71a-49b1-b98d-f95e04c44445",
                name = "導火",
                artistCredits = listOf(
                    ArtistCreditUiModel(
                        artistId = "6825ace2-3563-4ac5-8d85-c7bf1334bd2c",
                        name = "月詠み (something)",
                        joinPhrase = "",
                    ),
                ),
                lastUpdated = testDateTimeInThePast,
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
        assertEquals(
            RecordingDetailsModel(
                id = "7e52152f-c71a-49b1-b98d-f95e04c44445",
                name = "導火",
                artistCredits = listOf(
                    ArtistCreditUiModel(
                        artistId = "6825ace2-3563-4ac5-8d85-c7bf1334bd2c",
                        name = "月詠み (something)",
                        joinPhrase = "",
                    ),
                ),
                lastUpdated = testDateTimeInThePast,
            ),
            allDataArtistDetailsModel,
        )
        allDataArtistDetailsModel = allDataRepository.lookupRecording(
            recordingId = "7e52152f-c71a-49b1-b98d-f95e04c44445",
            forceRefresh = true,
            lastUpdated = testDateTimeInThePast.plus(3.days),
        )
        assertEquals(
            RecordingDetailsModel(
                id = "7e52152f-c71a-49b1-b98d-f95e04c44445",
                name = "導火",
                length = 209000,
                artistCredits = listOf(
                    ArtistCreditUiModel(
                        artistId = "6825ace2-3563-4ac5-8d85-c7bf1334bd2c",
                        name = "月詠み",
                        joinPhrase = "",
                    ),
                ),
                video = true,
                lastUpdated = testDateTimeInThePast.plus(3.days),
                urls = listOf(
                    RelationListItemModel(
                        id = "b5322490-3003-42e9-a043-d26a83bd1bbd_1",
                        linkedEntityId = "b5322490-3003-42e9-a043-d26a83bd1bbd",
                        label = "free streaming",
                        name = "https://music.youtube.com/watch?v=2vkgqu3-_Pk",
                        disambiguation = null,
                        attributes = "video",
                        linkedEntity = MusicBrainzEntity.URL,
                        visited = true,
                        isForwardDirection = true,
                    ),
                ),
            ),
            allDataArtistDetailsModel,
        )
    }
}
