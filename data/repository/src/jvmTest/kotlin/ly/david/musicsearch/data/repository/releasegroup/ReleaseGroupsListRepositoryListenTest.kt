package ly.david.musicsearch.data.repository.releasegroup

import androidx.paging.testing.asSnapshot
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.NoOpListenBrainzAuthStore
import ly.david.data.test.api.FakeBrowseApi
import ly.david.data.test.nutcrackerReleaseGroupListItemModel
import ly.david.data.test.nutcrackerReleaseGroupMusicBrainzModel
import ly.david.musicsearch.data.database.dao.AliasDao
import ly.david.musicsearch.data.database.dao.BrowseRemoteMetadataDao
import ly.david.musicsearch.data.database.dao.CollectionEntityDao
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.database.dao.ReleaseReleaseGroupDao
import ly.david.musicsearch.data.musicbrainz.api.BrowseReleaseGroupsResponse
import ly.david.musicsearch.data.musicbrainz.models.MediumMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.TrackMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseGroupMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzNetworkModel
import ly.david.musicsearch.data.repository.helpers.testDateTimeInThePast
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.coroutine.CoroutineDispatchers
import ly.david.musicsearch.shared.domain.list.ListFilters
import ly.david.musicsearch.shared.domain.listen.Listen
import ly.david.musicsearch.shared.domain.listen.ListenDao
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupsListRepository
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

private const val USERNAME = "testuser"

class ReleaseGroupsListRepositoryListenTest : KoinTest {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    val releaseGroupDao: ReleaseGroupDao by inject()
    val releaseDao: ReleaseDao by inject()
    val releaseReleaseGroupDao: ReleaseReleaseGroupDao by inject()
    val browseRemoteMetadataDao: BrowseRemoteMetadataDao by inject()
    val collectionEntityDao: CollectionEntityDao by inject()
    val aliasDao: AliasDao by inject()
    val coroutineDispatchers: CoroutineDispatchers by inject()
    val listenDao: ListenDao by inject()

    private val releaseGroupId = nutcrackerReleaseGroupMusicBrainzModel.id

    private fun createReleaseGroupsListRepository(
        releaseGroups: List<ReleaseGroupMusicBrainzNetworkModel>,
        fakeBrowseUsername: String = "",
    ): ReleaseGroupsListRepository {
        return ReleaseGroupsListRepositoryImpl(
            browseRemoteMetadataDao = browseRemoteMetadataDao,
            collectionEntityDao = collectionEntityDao,
            releaseGroupDao = releaseGroupDao,
            aliasDao = aliasDao,
            browseApi = object : FakeBrowseApi() {
                override suspend fun browseReleaseGroupsByEntity(
                    entityId: String,
                    entity: MusicBrainzEntityType,
                    limit: Int,
                    offset: Int,
                    include: String,
                ): BrowseReleaseGroupsResponse {
                    return BrowseReleaseGroupsResponse(
                        count = releaseGroups.size,
                        offset = 0,
                        musicBrainzModels = releaseGroups,
                    )
                }
            },
            listenBrainzAuthStore = object : NoOpListenBrainzAuthStore() {
                override val browseUsername: Flow<String>
                    get() = flowOf(fakeBrowseUsername)
            },
        )
    }

    /**
     * Builds a release that belongs to [releaseGroupId] with a single medium containing one track
     * per recording id in [recordingIds].
     */
    private fun releaseWithTracks(
        id: String,
        releaseGroupId: String,
        recordingIds: List<String>,
    ): ReleaseMusicBrainzNetworkModel {
        return ReleaseMusicBrainzNetworkModel(
            id = id,
            name = "release $id",
            media = listOf(
                MediumMusicBrainzModel(
                    position = 1,
                    trackCount = recordingIds.size,
                    tracks = recordingIds.mapIndexed { index, recordingId ->
                        TrackMusicBrainzModel(
                            id = "$id-medium-1-track-${index + 1}",
                            position = index + 1,
                            number = (index + 1).toString(),
                            name = "track ${index + 1}",
                            recording = RecordingMusicBrainzNetworkModel(
                                id = recordingId,
                                name = "recording $recordingId",
                            ),
                        )
                    },
                ),
            ),
        ).also {
            releaseDao.upsert(oldId = "", release = it)
            releaseReleaseGroupDao.insert(releaseId = it.id, releaseGroupId = releaseGroupId)
        }
    }

    private fun insertListens(
        recordingMusicbrainzId: String,
        count: Int,
        startListenedAtMs: Long,
    ) {
        val listens = (0 until count).map { index ->
            Listen(
                insertedAtMs = 1_700_000_000_000L,
                listenedAtMs = startListenedAtMs + index * 1000,
                recordingMessybrainzId = "$recordingMusicbrainzId-$index",
                username = USERNAME,
                artistName = "artist",
                trackName = "track",
                discNumber = null,
                trackNumber = null,
                isrc = null,
                mediaPlayer = null,
                submissionClient = null,
                musicService = null,
                musicServiceName = null,
                originUrl = null,
                spotifyAlbumArtistIds = null,
                spotifyAlbumId = null,
                spotifyArtistIds = null,
                spotifyId = null,
                entityMapping = Listen.EntityMapping(
                    recordingMusicbrainzId = recordingMusicbrainzId,
                    recordingName = "recording",
                    durationMs = null,
                    caaId = null,
                    caaReleaseMbid = null,
                    releaseMbid = null,
                    releaseName = null,
                    artistCredits = persistentListOf(),
                ),
            )
        }
        listenDao.insert(listens)
    }

    @Test
    fun `release group listen count sums distinct recordings across multiple releases`() = runTest {
        // The release group has two releases whose tracks are not all the same recording, and one
        // recording (`sharedRecordingId`) appears in both releases. The query must count it only
        // once towards the release group listen count.
        val recordingOneId = "recording-one-0000-0000-0000-000000000001"
        val sharedRecordingId = "shared-recording-0000-0000-0000-000000000003"
        val recordingThreeId = "recording-three-0000-0000-0000-000000000004"

        releaseGroupDao.insertAllReleaseGroups(
            listOf(nutcrackerReleaseGroupMusicBrainzModel),
        )

        releaseWithTracks(
            id = "release-one-0000-0000-0000-0000000000aa",
            releaseGroupId = releaseGroupId,
            recordingIds = listOf(recordingOneId, sharedRecordingId),
        )
        releaseWithTracks(
            id = "release-two-0000-0000-0000-0000000000bb",
            releaseGroupId = releaseGroupId,
            recordingIds = listOf(sharedRecordingId, recordingThreeId),
        )

        insertListens(
            recordingMusicbrainzId = recordingOneId,
            count = 2,
            startListenedAtMs = 1000,
        )
        insertListens(
            recordingMusicbrainzId = sharedRecordingId,
            count = 1,
            startListenedAtMs = 3000,
        )
        insertListens(
            recordingMusicbrainzId = recordingThreeId,
            count = 3,
            startListenedAtMs = 4000,
        )

        val releaseGroupsListRepository = createReleaseGroupsListRepository(
            releaseGroups = listOf(),
            fakeBrowseUsername = USERNAME,
        )

        releaseGroupsListRepository.observeReleaseGroups(
            browseMethod = BrowseMethod.All,
            listFilters = ListFilters.ReleaseGroups(username = USERNAME),
            now = testDateTimeInThePast,
        ).asSnapshot().run {
            // 2 + 1 (shared across both releases, deduplicated to a single count) + 3 = 6 listens
            assertEquals(
                listOf(
                    nutcrackerReleaseGroupListItemModel.copy(
                        listenCount = 6,
                        lastListenedAtMs = 6000,
                    ),
                ),
                this,
            )
        }
    }

    @Test
    fun `release group listen count is null when no browse username is set`() = runTest {
        releaseGroupDao.insertAllReleaseGroups(
            listOf(nutcrackerReleaseGroupMusicBrainzModel),
        )

        releaseWithTracks(
            id = "release-one-0000-0000-0000-0000000000aa",
            releaseGroupId = releaseGroupId,
            recordingIds = listOf("recording-one-0000-0000-0000-000000000001"),
        )

        val releaseGroupsListRepository = createReleaseGroupsListRepository(
            releaseGroups = listOf(),
        )

        releaseGroupsListRepository.observeReleaseGroups(
            browseMethod = BrowseMethod.All,
            listFilters = ListFilters.ReleaseGroups(),
            now = testDateTimeInThePast,
        ).asSnapshot().run {
            assertEquals(
                listOf(
                    nutcrackerReleaseGroupListItemModel.copy(
                        listenCount = null,
                        lastListenedAtMs = null,
                    ),
                ),
                this,
            )
        }
    }
}
