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
import ly.david.musicsearch.shared.domain.listen.ListenSubmission
import ly.david.musicsearch.shared.domain.listen.ListensListRepository
import ly.david.musicsearch.shared.domain.listen.SubmitListenType
import ly.david.musicsearch.ui.common.screen.SnackbarPopResult
import ly.david.musicsearch.ui.common.screen.SubmitListenScreen
import kotlin.time.Clock
import kotlin.time.Instant

internal class SubmitListenPresenter(
    private val screen: SubmitListenScreen,
    private val navigator: Navigator,
    private val listensListRepository: ListensListRepository,
    val clock: Clock,
    val timeZone: TimeZone,
) : Presenter<SubmitListenUiState> {
    @Composable
    override fun present(): SubmitListenUiState {
        val coroutineScope = rememberCoroutineScope()

        var dateTimeEpochSeconds: Long by rememberSaveable { mutableLongStateOf(0) }
        var timestampIsStartTime by rememberSaveable { mutableStateOf(true) }
        var useCustomTime by rememberSaveable { mutableStateOf(false) }
        val listenedAtDateTimeEpochSeconds by rememberSaveable(dateTimeEpochSeconds, timestampIsStartTime) {
            mutableLongStateOf(
                dateTimeEpochSeconds - if (timestampIsStartTime) {
                    0
                } else {
                    ((screen.submitListenType as? SubmitListenType.Track)?.lengthMilliseconds ?: 0) / MS_IN_SECOND
                },
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
                        when (val listenType = screen.submitListenType) {
                            is SubmitListenType.Track -> {
                                // TODO: observe response and show UI
                                listensListRepository.submitListens(
                                    listenSubmissions = listOf(
                                        ListenSubmission(
                                            listenedAtS = listenedAtDateTimeEpochSeconds,
                                            trackName = listenType.name,
                                            recordingMbid = listenType.recordingId,
                                            artistName = listenType.artists.getDisplayNames(),
                                            artistMbids = listenType.artists.map { it.artistId },
                                            durationMs = listenType.lengthMilliseconds?.toLong(),
                                        ),
                                    ),
                                )
                            }
                        }
                        navigator.pop(result = SnackbarPopResult())
                    }
                }

                is SubmitListenUiEvent.Dismiss -> {
                    navigator.pop(result = SnackbarPopResult())
                }
            }
        }

        return SubmitListenUiState(
            submitListenType = screen.submitListenType,
            dateTimeEpochSeconds = dateTimeEpochSeconds,
            listenedAtDateTimeEpochSeconds = listenedAtDateTimeEpochSeconds,
            timestampIsStartTime = timestampIsStartTime,
            useCustomTime = useCustomTime,
            // This style is required for presenter tests, or we get KotlinReflectionInternalError
            eventSink = { event -> eventSink(event) },
        )
    }
}

/**
 * Use [dateTimeEpochSeconds] for date and time selection.
 * Use [listenedAtDateTimeEpochSeconds] for preview and submission.
 */
@Stable
internal data class SubmitListenUiState(
    val submitListenType: SubmitListenType,
    val dateTimeEpochSeconds: Long = 0,
    val listenedAtDateTimeEpochSeconds: Long = 0,
    val timestampIsStartTime: Boolean = true,
    val useCustomTime: Boolean = false,
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
