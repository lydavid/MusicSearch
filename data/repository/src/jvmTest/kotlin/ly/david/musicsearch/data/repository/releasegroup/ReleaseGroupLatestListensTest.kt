package ly.david.musicsearch.data.repository.releasegroup

import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import ly.david.data.test.KoinTestRule
import ly.david.data.test.nutcrackerReleaseGroupMusicBrainzModel
import ly.david.musicsearch.data.database.dao.ReleaseDao
import ly.david.musicsearch.data.database.dao.ReleaseGroupDao
import ly.david.musicsearch.data.database.dao.ReleaseReleaseGroupDao
import ly.david.musicsearch.data.musicbrainz.models.MediumMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.TrackMusicBrainzModel
import ly.david.musicsearch.data.musicbrainz.models.core.RecordingMusicBrainzNetworkModel
import ly.david.musicsearch.data.musicbrainz.models.core.ReleaseMusicBrainzNetworkModel
import ly.david.musicsearch.shared.domain.listen.Listen
import ly.david.musicsearch.shared.domain.listen.ListenDao
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import org.koin.test.inject

private const val USERNAME = "testuser"

class ReleaseGroupLatestListensTest : KoinTest {

    @get:Rule(order = 0)
    val koinTestRule = KoinTestRule()

    val releaseGroupDao: ReleaseGroupDao by inject()
    val releaseDao: ReleaseDao by inject()
    val releaseReleaseGroupDao: ReleaseReleaseGroupDao by inject()
    val listenDao: ListenDao by inject()

    private val releaseGroupId = nutcrackerReleaseGroupMusicBrainzModel.id

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

    private fun insertListen(
        recordingMusicbrainzId: String,
        listenedAtMs: Long,
    ) {
        listenDao.insert(
            listOf(
                Listen(
                    insertedAtMs = 1_700_000_000_000L,
                    listenedAtMs = listenedAtMs,
                    recordingMessybrainzId = "$recordingMusicbrainzId-messy",
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
                ),
            ),
        )
    }

    @Test
    fun `latest listens only include recordings that belong to the release group`() = runTest {
        // Recording that belongs to the release group and has several listens.
        val groupRecordingId = "group-recording-0000-0000-0000-000000000001"
        // Recording that is NOT on any release of the group; it has the single most recent
        // listen of the user, so a query that ignores the release-group scoping would surface it.
        val unrelatedRecordingId = "unrelated-recording-0000-000000000002"

        releaseGroupDao.insertAllReleaseGroups(
            listOf(nutcrackerReleaseGroupMusicBrainzModel),
        )
        releaseWithTracks(
            id = "release-one-0000-0000-0000-0000000000aa",
            releaseGroupId = releaseGroupId,
            recordingIds = listOf(groupRecordingId),
        )
        releaseWithTracks(
            id = "unrelated-release-0000-0000-0000-0000000000cc",
            releaseGroupId = "unrelated-release-group-0000-0000-0000000000dd",
            recordingIds = listOf(unrelatedRecordingId),
        )

        // Listens for the group's recording, spread across time.
        insertListen(groupRecordingId, listenedAtMs = 1_750_000_000_000L)
        insertListen(groupRecordingId, listenedAtMs = 1_740_000_000_000L)
        insertListen(groupRecordingId, listenedAtMs = 1_730_000_000_000L)
        // The most recent listen belongs to an unrelated recording.
        insertListen(unrelatedRecordingId, listenedAtMs = 1_790_000_000_000L)

        val details = releaseGroupDao.getReleaseGroupForDetails(
            releaseGroupId = releaseGroupId,
            listenBrainzUsername = USERNAME,
            numberOfListensToShow = 10,
        )

        assertNotNull(details)
        // The unrelated, most recent listen must not leak in.
        assertEquals(
            3,
            details!!.latestListens.size,
        )
        assertTrue(
            "latest listens should be ordered by listened_at_ms descending",
            details.latestListens.zip(details.latestListens.drop(1)).all { (a, b) -> a.listenedMs >= b.listenedMs },
        )
        details.latestListens.forEach {
            assertEquals(groupRecordingId, it.id)
        }
    }
}
