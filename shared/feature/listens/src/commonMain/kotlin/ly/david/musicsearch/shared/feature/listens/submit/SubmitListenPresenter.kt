package ly.david.musicsearch.shared.feature.listens.submit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import ly.david.musicsearch.shared.domain.MS_IN_SECOND
import ly.david.musicsearch.shared.domain.artist.getDisplayNames
import ly.david.musicsearch.shared.domain.common.toEpochSeconds
import ly.david.musicsearch.shared.domain.error.Feedback
import ly.david.musicsearch.shared.domain.error.withTime
import ly.david.musicsearch.shared.domain.listen.GetTracksByReleaseForListenSubmission
import ly.david.musicsearch.shared.domain.listen.ListenBrainzAuthStore
import ly.david.musicsearch.shared.domain.listen.ListenSubmission
import ly.david.musicsearch.shared.domain.listen.ListensListRepository
import ly.david.musicsearch.shared.domain.listen.SubmitListenFeedback
import ly.david.musicsearch.shared.domain.listen.SubmitListenType
import ly.david.musicsearch.shared.domain.listen.TrackInfo
import ly.david.musicsearch.ui.common.screen.SnackbarPopResultV2
import ly.david.musicsearch.ui.common.screen.SubmitListenScreen
import kotlin.time.Clock
import kotlin.time.Instant

internal class SubmitListenPresenter(
    private val screen: SubmitListenScreen,
    private val navigator: Navigator,
    private val listenBrainzAuthStore: ListenBrainzAuthStore,
    private val listensListRepository: ListensListRepository,
    private val getTracksByReleaseForListenSubmission: GetTracksByReleaseForListenSubmission,
    private val clock: Clock,
    private val timeZone: TimeZone,
) : Presenter<SubmitListenUiState> {
    @Composable
    override fun present(): SubmitListenUiState {
        val type = screen.submitListenType

        val coroutineScope = rememberCoroutineScope()
        val loggedInUsername by listenBrainzAuthStore.username.collectAsRetainedState("")
        var dateTimeEpochSeconds: Long by rememberSaveable { mutableLongStateOf(0) }
        var timestampIsStartTime by rememberSaveable { mutableStateOf(true) }
        var useCustomTime by rememberSaveable { mutableStateOf(false) }

        val allSelectedTrackInfo: List<TrackInfo>? by rememberSaveable {
            mutableStateOf(
                when (type) {
                    is SubmitListenType.Album -> {
                        val allTrackInfo = getTracksByReleaseForListenSubmission(releaseId = type.releaseId)
                        allTrackInfo.filter { trackInfo ->
                            trackInfo.recordingId in type.recordingIds
                        }
                    }

                    is SubmitListenType.Track -> null
                },
            )
        }
        val listenedAtDateTimeEpochSeconds by rememberSaveable(
            dateTimeEpochSeconds,
            timestampIsStartTime,
            allSelectedTrackInfo,
        ) {
            mutableLongStateOf(
                dateTimeEpochSeconds - offsetStartTimeSeconds(
                    timestampIsStartTime = timestampIsStartTime,
                    type = type,
                    allSelectedTrackInfo = allSelectedTrackInfo,
                ),
            )
        }

        if (!useCustomTime) {
            LaunchedEffect(useCustomTime) {
                while (true) {
                    dateTimeEpochSeconds = clock.now().toEpochSeconds()
                    delay(MS_IN_SECOND.toLong())
                }
            }
        }

        fun eventSink(event: SubmitListenUiEvent) {
            when (event) {
                is SubmitListenUiEvent.UpdateDate -> {
                    val existing = Instant.fromEpochSeconds(dateTimeEpochSeconds).toLocalDateTime(timeZone)
                    val newDate = Instant.fromEpochSeconds(event.epochSeconds).toLocalDateTime(timeZone).date
                    dateTimeEpochSeconds = LocalDateTime(newDate, existing.time)
                        .toInstant(timeZone)
                        .toEpochSeconds()
                }

                is SubmitListenUiEvent.UpdateTime -> {
                    val existing = Instant.fromEpochSeconds(dateTimeEpochSeconds).toLocalDateTime(timeZone)
                    val newTime = Instant.fromEpochSeconds(event.epochSeconds).toLocalDateTime(timeZone).time
                    dateTimeEpochSeconds = LocalDateTime(existing.date, newTime)
                        .toInstant(timeZone)
                        .toEpochSeconds()
                }

                is SubmitListenUiEvent.UpdateDateTimeIsStartTime -> {
                    timestampIsStartTime = event.isStartTime
                }

                is SubmitListenUiEvent.UpdateUseCustomTime -> {
                    useCustomTime = event.useCustomTime
                }

                is SubmitListenUiEvent.Submit -> {
                    coroutineScope.launch {
                        val submissions = when (type) {
                            is SubmitListenType.Track -> {
                                listOf(
                                    ListenSubmission(
                                        listenedAtS = listenedAtDateTimeEpochSeconds,
                                        trackName = type.info.name,
                                        recordingMbid = type.info.recordingId,
                                        durationMs = type.info.lengthMilliseconds,
                                        artistName = type.info.artists.getDisplayNames(),
                                        artistMbids = type.info.artists.map { it.artistId },
                                        releaseName = type.releaseName,
                                        releaseMbid = type.releaseId,
                                    ),
                                )
                            }

                            is SubmitListenType.Album -> {
                                val tracks = allSelectedTrackInfo ?: return@launch
                                tracks.toSubmissions(
                                    listenedAtDateTimeEpochSeconds = listenedAtDateTimeEpochSeconds,
                                    album = type,
                                )
                            }
                        }

                        // TODO: emit flow, so that we can show more feedback, don't pop until final feedback
                        val feedback = listensListRepository.submitListens(
                            username = loggedInUsername,
                            listenSubmissions = submissions,
                        )
                        navigator.pop(
                            result = SnackbarPopResultV2(
                                feedback = feedback.withTime(clock.now()),
                            ),
                        )
                    }
                }

                is SubmitListenUiEvent.Dismiss -> {
                    navigator.pop(result = SnackbarPopResultV2<Feedback.Success<SubmitListenFeedback>>(feedback = null))
                }
            }
        }

        return SubmitListenUiState(
            submitListenType = screen.submitListenType,
            dateTimeEpochSeconds = dateTimeEpochSeconds,
            listenedAtDateTimeEpochSeconds = listenedAtDateTimeEpochSeconds,
            timestampIsStartTime = timestampIsStartTime,
            useCustomTime = useCustomTime,
            allSelectedTrackInfo = allSelectedTrackInfo,
            // This style is required for presenter tests, or we get KotlinReflectionInternalError
            eventSink = { event -> eventSink(event) },
        )
    }
}

private fun offsetStartTimeSeconds(
    timestampIsStartTime: Boolean,
    type: SubmitListenType,
    allSelectedTrackInfo: List<TrackInfo>?,
): Long = if (timestampIsStartTime) {
    0
} else {
    val fullLengthMilliseconds = when (type) {
        is SubmitListenType.Album -> allSelectedTrackInfo?.getTotalListenLength() ?: 0
        is SubmitListenType.Track -> type.info.lengthMilliseconds ?: 0
    }
    fullLengthMilliseconds / MS_IN_SECOND
}

private fun List<TrackInfo>.toSubmissions(
    listenedAtDateTimeEpochSeconds: Long,
    album: SubmitListenType.Album,
): List<ListenSubmission> {
    var cumulativeOffsetSeconds = 0L

    return this.map { trackInfo ->
        val submission = ListenSubmission(
            listenedAtS = listenedAtDateTimeEpochSeconds + cumulativeOffsetSeconds,
            trackName = trackInfo.name,
            recordingMbid = trackInfo.recordingId,
            // non-zero length is only to make it more sensible for the user's listens.
            // we don't want to pass incorrect duration to the mapper, so don't pass a fake length.
            durationMs = trackInfo.lengthMilliseconds,
            artistName = trackInfo.artists.getDisplayNames(),
            artistMbids = trackInfo.artists.map { it.artistId },
            releaseName = album.releaseName,
            releaseMbid = album.releaseId,
        )
        cumulativeOffsetSeconds += trackInfo.nonZeroLengthMilliseconds / MS_IN_SECOND
        submission
    }
}

/**
 * Use [dateTimeEpochSeconds] for date and time selection.
 * Use [listenedAtDateTimeEpochSeconds] for preview and submission.
 */
@Stable
internal data class SubmitListenUiState(
    val submitListenType: SubmitListenType,
    val dateTimeEpochSeconds: Long,
    val listenedAtDateTimeEpochSeconds: Long,
    val timestampIsStartTime: Boolean = true,
    val useCustomTime: Boolean = false,
    val allSelectedTrackInfo: List<TrackInfo>? = null,
    val eventSink: (SubmitListenUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface SubmitListenUiEvent : CircuitUiEvent {
    data class UpdateDate(val epochSeconds: Long) : SubmitListenUiEvent
    data class UpdateTime(val epochSeconds: Long) : SubmitListenUiEvent
    data class UpdateDateTimeIsStartTime(val isStartTime: Boolean) : SubmitListenUiEvent
    data class UpdateUseCustomTime(val useCustomTime: Boolean) : SubmitListenUiEvent
    data object Submit : SubmitListenUiEvent
    data object Dismiss : SubmitListenUiEvent
}
