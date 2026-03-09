package ly.david.musicsearch.shared.feature.listens.submit

import com.slack.circuit.test.FakeNavigator
import com.slack.circuit.test.presenterTestOf
import io.mockk.mockk
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ly.david.data.test.NoOpListenBrainzAuthStore
import ly.david.data.test.clock.FixedClock
import ly.david.musicsearch.shared.domain.alias.BasicAlias
import ly.david.musicsearch.shared.domain.artist.ArtistCreditUiModel
import ly.david.musicsearch.shared.domain.listen.ListensListRepository
import ly.david.musicsearch.shared.domain.listen.SubmitListenType
import ly.david.musicsearch.ui.common.screen.SubmitListenScreen
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.time.Instant

@RunWith(RobolectricTestRunner::class)
class SubmitListenPresenterTest {

    val trackSubmitListenType = SubmitListenType.Track(
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
        artists = persistentListOf(
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
        ),
        releaseName = null,
    )
    val screen = SubmitListenScreen(
        submitListenType = trackSubmitListenType,
    )
    private val navigator = FakeNavigator(
        root = screen,
    )

    private val listensListRepository = mockk<ListensListRepository>()

    private fun createSubmitListenPresenter() = SubmitListenPresenter(
        screen = screen,
        navigator = navigator,
        listenBrainzAuthStore = NoOpListenBrainzAuthStore(),
        listensListRepository = listensListRepository,
        clock = FixedClock(now = Instant.parse("1970-01-02T05:00:00Z")),
        timeZone = TimeZone.UTC,
    )

    @Test
    fun `smoke test`() = runTest {
        val submitListenPresenter = createSubmitListenPresenter()
        val fixedClockEpochSeconds = 104400L
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
        }
    }
}
