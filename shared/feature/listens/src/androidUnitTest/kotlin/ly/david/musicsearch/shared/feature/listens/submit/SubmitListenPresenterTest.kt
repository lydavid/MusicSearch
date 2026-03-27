package ly.david.musicsearch.shared.feature.listens.submit

import com.slack.circuit.runtime.Navigator
import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.presenterTestOf
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ly.david.data.test.NoOpListenBrainzAuthStore
import ly.david.data.test.clock.FixedClock
import ly.david.musicsearch.shared.domain.MS_IN_SECOND
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.error.Feedback
import ly.david.musicsearch.shared.domain.listen.GetTracksByReleaseForListenSubmission
import ly.david.musicsearch.shared.domain.listen.ListenSubmission
import ly.david.musicsearch.shared.domain.listen.ListensListRepository
import ly.david.musicsearch.shared.domain.listen.SubmitListenFeedback
import ly.david.musicsearch.shared.domain.listen.SubmitListenType
import ly.david.musicsearch.shared.domain.listen.TrackInfo
import ly.david.musicsearch.ui.common.screen.SnackbarPopResultV2
import ly.david.musicsearch.ui.common.screen.SubmitListenScreen
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.time.Instant

@RunWith(RobolectricTestRunner::class)
class SubmitListenPresenterTest {

    private val artists = persistentListOf(
        ArtistCreditUiModel(
            artistId = "a1",
            name = "Artist",
            joinPhrase = " feat. ",
        ),
        ArtistCreditUiModel(
            artistId = "a2",
            name = "Another Artist",
            joinPhrase = "",
        ),
    )
    private val trackSubmitListenType = SubmitListenType.Track(
        info = TrackInfo(
            name = "Track",
            disambiguation = "That one",
            aliases = persistentListOf(
                BasicAlias(
                    name = "English name",
                    locale = "en",
                    isPrimary = true,
                ),
            ),
            recordingId = "t1",
            lengthMilliseconds = 275186,
            artists = artists,
        ),
        releaseName = null,
        releaseId = null,
    )

    private val now = Instant.parse("1970-01-02T05:00:00Z")

    private val listensListRepository = mockk<ListensListRepository>()

    private fun createSubmitListenPresenter(
        screen: SubmitListenScreen,
        navigator: Navigator,
        allTrackInfo: List<TrackInfo> = emptyList(),
    ) = SubmitListenPresenter(
        screen = screen,
        navigator = navigator,
        listenBrainzAuthStore = NoOpListenBrainzAuthStore(),
        listensListRepository = listensListRepository,
        getTracksByReleaseForListenSubmission = object : GetTracksByReleaseForListenSubmission {
            override fun invoke(releaseId: String): List<TrackInfo> {
                return allTrackInfo
            }
        },
        clock = FixedClock(now = now),
        timeZone = TimeZone.UTC,
    )

    @Test
    fun `smoke test track`() = runTest {
        val screen = SubmitListenScreen(
            submitListenType = trackSubmitListenType,
        )
        val navigator = FakeNavigator(
            root = screen,
        )
        val submitListenPresenter = createSubmitListenPresenter(
            screen = screen,
            navigator = navigator,
        )
        val fixedClockEpochSeconds = 104400L
        coEvery { listensListRepository.submitListens(any(), any()) } answers {
            Feedback.Success(SubmitListenFeedback.SubmittedListens)
        }
        presenterTestOf({ submitListenPresenter.present() }) {
            var state = awaitItem()
            assertEquals(
                trackSubmitListenType,
                state.submitListenType,
            )
            assertEquals(
                0,
                state.dateTimeEpochSeconds,
            )
            assertEquals(
                0,
                state.listenedAtDateTimeEpochSeconds,
            )
            assertEquals(
                true,
                state.timestampIsStartTime,
            )
            assertEquals(
                false,
                state.useCustomTime,
            )

            state = awaitItem()
            assertEquals(
                fixedClockEpochSeconds,
                state.dateTimeEpochSeconds,
            )
            assertEquals(
                fixedClockEpochSeconds,
                state.listenedAtDateTimeEpochSeconds,
            )

            state.eventSink(
                SubmitListenUiEvent.UpdateDateTimeIsStartTime(isStartTime = false),
            )
            state = awaitItem()
            assertEquals(
                false,
                state.timestampIsStartTime,
            )
            assertEquals(
                104125,
                state.listenedAtDateTimeEpochSeconds,
            )

            state.eventSink(
                SubmitListenUiEvent.UpdateUseCustomTime(useCustomTime = true),
            )
            state = awaitItem()
            assertEquals(
                true,
                state.useCustomTime,
            )

            state.eventSink(
                SubmitListenUiEvent.UpdateDate(epochSeconds = Instant.parse("1970-01-01T05:00:00Z").epochSeconds),
            )
            state = awaitItem()
            assertEquals(
                18000,
                state.dateTimeEpochSeconds,
            )
            assertEquals(
                17725,
                state.listenedAtDateTimeEpochSeconds,
            )

            // only the time component is updated
            state.eventSink(
                SubmitListenUiEvent.UpdateTime(epochSeconds = Instant.parse("2000-01-01T01:00:00Z").epochSeconds),
            )
            state = awaitItem()
            var epochSeconds = 3600L
            assertEquals(
                epochSeconds,
                state.dateTimeEpochSeconds,
            )
            assertEquals(
                Instant.fromEpochSeconds(epochSeconds).toLocalDateTime(timeZone = TimeZone.UTC).toString(),
                "1970-01-01T01:00",
            )
            assertEquals(
                3325,
                state.listenedAtDateTimeEpochSeconds,
            )

            // only the date component is updated
            state.eventSink(
                SubmitListenUiEvent.UpdateDate(epochSeconds = Instant.parse("2026-03-08T05:00:00Z").epochSeconds),
            )
            state = awaitItem()
            epochSeconds = 1772931600L
            assertEquals(
                epochSeconds,
                state.dateTimeEpochSeconds,
            )
            assertEquals(
                Instant.fromEpochSeconds(epochSeconds).toLocalDateTime(timeZone = TimeZone.UTC).toString(),
                "2026-03-08T01:00",
            )
            assertEquals(
                1772931325,
                state.listenedAtDateTimeEpochSeconds,
            )

            state.eventSink(
                SubmitListenUiEvent.UpdateUseCustomTime(useCustomTime = false),
            )
            state = awaitItem()
            assertEquals(
                false,
                state.useCustomTime,
            )
            assertEquals(
                epochSeconds,
                state.dateTimeEpochSeconds,
            )

            state = awaitItem()
            assertEquals(
                fixedClockEpochSeconds,
                state.dateTimeEpochSeconds,
            )
            assertEquals(
                104125,
                state.listenedAtDateTimeEpochSeconds,
            )

            state.eventSink(
                SubmitListenUiEvent.UpdateDateTimeIsStartTime(isStartTime = true),
            )
            state = awaitItem()
            assertEquals(
                true,
                state.timestampIsStartTime,
            )
            assertEquals(
                fixedClockEpochSeconds,
                state.dateTimeEpochSeconds,
            )
            assertEquals(
                fixedClockEpochSeconds,
                state.listenedAtDateTimeEpochSeconds,
            )

            state.eventSink(
                SubmitListenUiEvent.Submit,
            )
            assertEquals(
                FakeNavigator.PopEvent(
                    poppedScreen = null,
                    result = SnackbarPopResultV2(
                        Feedback.Success(
                            data = SubmitListenFeedback.SubmittedListens,
                            time = now,
                        ),
                    ),
                ),
                navigator.awaitPop(),
            )
        }
    }

    @Test
    fun `dismiss pops screen`() = runTest {
        val screen = SubmitListenScreen(
            submitListenType = trackSubmitListenType,
        )
        val navigator = FakeNavigator(
            root = screen,
        )
        val submitListenPresenter = createSubmitListenPresenter(
            screen = screen,
            navigator = navigator,
        )
        coEvery { listensListRepository.submitListens(any(), any()) } answers {
            Feedback.Success(SubmitListenFeedback.SubmittedListens)
        }
        presenterTestOf({ submitListenPresenter.present() }) {
            skipItems(1)
            val state = awaitItem()
            state.eventSink(
                SubmitListenUiEvent.Dismiss,
            )
            assertEquals(
                FakeNavigator.PopEvent(poppedScreen = null, result = SnackbarPopResultV2(feedback = null)),
                navigator.awaitPop(),
            )
        }
    }

    @Test
    fun `smoke test album`() = runTest {
        val albumSubmitListenType = SubmitListenType.Album(
            releaseName = "Album name",
            releaseId = "r1",
            releaseArtists = artists,
            recordingIds = persistentListOf("rec2", "rec3"),
        )
        val screen = SubmitListenScreen(
            submitListenType = albumSubmitListenType,
        )
        val navigator = FakeNavigator(
            root = screen,
        )
        val track2 = TrackInfo(
            name = "Track 2",
            recordingId = "rec2",
            lengthMilliseconds = 400L * MS_IN_SECOND,
            artists = artists,
            disambiguation = null,
            aliases = persistentListOf(),
        )
        val track3 = TrackInfo(
            name = "Track 3",
            recordingId = "rec3",
            lengthMilliseconds = 4000L * MS_IN_SECOND,
            artists = artists,
            disambiguation = null,
            aliases = persistentListOf(),
        )
        val allTrackInfo = listOf(
            TrackInfo(
                name = "Track 1",
                recordingId = "rec1",
                lengthMilliseconds = 0L,
                artists = artists,
                disambiguation = null,
                aliases = persistentListOf(),
            ),
            track2,
            track3,
        )
        val submitListenPresenter = createSubmitListenPresenter(
            screen = screen,
            navigator = navigator,
            allTrackInfo = allTrackInfo,
        )
        val fixedClockEpochSeconds = 104400L
        coEvery { listensListRepository.submitListens(any(), any()) } answers {
            Feedback.Success(SubmitListenFeedback.SubmittedListens)
        }
        presenterTestOf({ submitListenPresenter.present() }) {
            var state = awaitItem()
            assertEquals(
                albumSubmitListenType,
                state.submitListenType,
            )
            assertEquals(
                0,
                state.dateTimeEpochSeconds,
            )
            assertEquals(
                0,
                state.listenedAtDateTimeEpochSeconds,
            )
            assertEquals(
                true,
                state.timestampIsStartTime,
            )
            assertEquals(
                false,
                state.useCustomTime,
            )
            assertEquals(
                listOf(
                    track2,
                    track3,
                ),
                state.allSelectedTrackInfo,
            )

            state = awaitItem()
            assertEquals(
                fixedClockEpochSeconds,
                state.dateTimeEpochSeconds,
            )
            assertEquals(
                fixedClockEpochSeconds,
                state.listenedAtDateTimeEpochSeconds,
            )

            state.eventSink(
                SubmitListenUiEvent.UpdateDateTimeIsStartTime(isStartTime = false),
            )
            state = awaitItem()
            assertEquals(
                false,
                state.timestampIsStartTime,
            )
            assertEquals(
                100000,
                state.listenedAtDateTimeEpochSeconds,
            )

            state.eventSink(
                SubmitListenUiEvent.UpdateUseCustomTime(useCustomTime = true),
            )
            state = awaitItem()
            assertEquals(
                true,
                state.useCustomTime,
            )

            state.eventSink(
                SubmitListenUiEvent.UpdateDate(epochSeconds = Instant.parse("1970-01-01T05:00:00Z").epochSeconds),
            )
            state = awaitItem()
            assertEquals(
                18000,
                state.dateTimeEpochSeconds,
            )
            assertEquals(
                13600,
                state.listenedAtDateTimeEpochSeconds,
            )

            // only the time component is updated
            state.eventSink(
                SubmitListenUiEvent.UpdateTime(epochSeconds = Instant.parse("2000-01-01T01:00:00Z").epochSeconds),
            )
            state = awaitItem()
            var epochSeconds = 3600L
            assertEquals(
                epochSeconds,
                state.dateTimeEpochSeconds,
            )
            assertEquals(
                Instant.fromEpochSeconds(epochSeconds).toLocalDateTime(timeZone = TimeZone.UTC).toString(),
                "1970-01-01T01:00",
            )
            assertEquals(
                -800,
                state.listenedAtDateTimeEpochSeconds,
            )

            // only the date component is updated
            state.eventSink(
                SubmitListenUiEvent.UpdateDate(epochSeconds = Instant.parse("2026-03-08T05:00:00Z").epochSeconds),
            )
            state = awaitItem()
            epochSeconds = 1772931600L
            assertEquals(
                epochSeconds,
                state.dateTimeEpochSeconds,
            )
            assertEquals(
                Instant.fromEpochSeconds(epochSeconds).toLocalDateTime(timeZone = TimeZone.UTC).toString(),
                "2026-03-08T01:00",
            )
            assertEquals(
                1772927200,
                state.listenedAtDateTimeEpochSeconds,
            )

            state.eventSink(
                SubmitListenUiEvent.UpdateUseCustomTime(useCustomTime = false),
            )
            state = awaitItem()
            assertEquals(
                false,
                state.useCustomTime,
            )
            assertEquals(
                epochSeconds,
                state.dateTimeEpochSeconds,
            )

            state = awaitItem()
            assertEquals(
                fixedClockEpochSeconds,
                state.dateTimeEpochSeconds,
            )
            assertEquals(
                100000,
                state.listenedAtDateTimeEpochSeconds,
            )

            state.eventSink(
                SubmitListenUiEvent.UpdateDateTimeIsStartTime(isStartTime = true),
            )
            state = awaitItem()
            assertEquals(
                true,
                state.timestampIsStartTime,
            )
            assertEquals(
                fixedClockEpochSeconds,
                state.dateTimeEpochSeconds,
            )
            assertEquals(
                fixedClockEpochSeconds,
                state.listenedAtDateTimeEpochSeconds,
            )

            state.eventSink(
                SubmitListenUiEvent.Submit,
            )
            assertEquals(
                FakeNavigator.PopEvent(
                    poppedScreen = null,
                    result = SnackbarPopResultV2(
                        Feedback.Success(
                            data = SubmitListenFeedback.SubmittedListens,
                            time = now,
                        ),
                    ),
                ),
                navigator.awaitPop(),
            )
        }
    }

    @Test
    fun `verify album listen submissions`() = runTest {
        val albumSubmitListenType = SubmitListenType.Album(
            releaseName = "Album name",
            releaseId = "r1",
            releaseArtists = artists,
            recordingIds = persistentListOf("rec2", "rec3", "rec4"),
        )
        val screen = SubmitListenScreen(
            submitListenType = albumSubmitListenType,
        )
        val navigator = FakeNavigator(
            root = screen,
        )
        val track2 = TrackInfo(
            name = "Track 2",
            recordingId = "rec2",
            lengthMilliseconds = 400L * MS_IN_SECOND,
            artists = persistentListOf(
                ArtistCreditUiModel(
                    artistId = "a3",
                    name = "Different track artist from album",
                    joinPhrase = "",
                ),
            ),
            disambiguation = null,
            aliases = persistentListOf(),
        )
        val track3 = TrackInfo(
            name = "Track 3",
            recordingId = "rec3",
            lengthMilliseconds = null,
            artists = artists,
            disambiguation = null,
            aliases = persistentListOf(),
        )
        val track4 = TrackInfo(
            name = "Track 4",
            recordingId = "rec4",
            lengthMilliseconds = null,
            artists = artists,
            disambiguation = null,
            aliases = persistentListOf(),
        )
        val allTrackInfo = listOf(
            TrackInfo(
                name = "Track 1",
                recordingId = "rec1",
                lengthMilliseconds = 0L,
                artists = artists,
                disambiguation = null,
                aliases = persistentListOf(),
            ),
            track2,
            track3,
            track4,
        )
        val submitListenPresenter = createSubmitListenPresenter(
            screen = screen,
            navigator = navigator,
            allTrackInfo = allTrackInfo,
        )
        val fixedClockEpochSeconds = 104400L
        val slot = slot<List<ListenSubmission>>()
        coEvery { listensListRepository.submitListens(any(), capture(slot)) } answers {
            Feedback.Success(SubmitListenFeedback.SubmittedListens)
        }
        presenterTestOf({ submitListenPresenter.present() }) {
            var state = awaitItem()
            state = awaitItem()

            state.eventSink(
                SubmitListenUiEvent.Submit,
            )
            val submissions = slot.captured
            assertEquals(
                listOf(
                    ListenSubmission(
                        listenedAtS = fixedClockEpochSeconds,
                        trackName = "Track 2",
                        recordingMbid = "rec2",
                        durationMs = 400000,
                        artistName = "Different track artist from album",
                        artistMbids = listOf("a3"),
                        releaseName = "Album name",
                        releaseMbid = "r1",
                    ),
                    ListenSubmission(
                        listenedAtS = 104800L,
                        trackName = "Track 3",
                        recordingMbid = "rec3",
                        durationMs = null,
                        artistName = "Artist feat. Another Artist",
                        artistMbids = listOf("a1", "a2"),
                        releaseName = "Album name",
                        releaseMbid = "r1",
                    ),
                    ListenSubmission(
                        listenedAtS = 104860, // some default seconds are added to the start time between tracks with unknown length
                        trackName = "Track 4",
                        recordingMbid = "rec4",
                        durationMs = null,
                        artistName = "Artist feat. Another Artist",
                        artistMbids = listOf("a1", "a2"),
                        releaseName = "Album name",
                        releaseMbid = "r1",
                    ),
                ),
                submissions,
            )
            assertEquals(
                FakeNavigator.PopEvent(
                    poppedScreen = null,
                    result = SnackbarPopResultV2(
                        Feedback.Success(
                            data = SubmitListenFeedback.SubmittedListens,
                            time = now,
                        ),
                    ),
                ),
                navigator.awaitPop(),
            )
        }
    }
}
