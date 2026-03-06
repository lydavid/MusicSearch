package ly.david.musicsearch.shared.feature.listens.submit

import androidx.compose.runtime.Composable
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
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.MS_IN_SECOND
import ly.david.musicsearch.shared.domain.SECONDS_IN_DAY
import ly.david.musicsearch.shared.domain.artist.getDisplayNames
import ly.david.musicsearch.shared.domain.listen.ListenSubmission
import ly.david.musicsearch.shared.domain.listen.ListensListRepository
import ly.david.musicsearch.shared.domain.listen.SubmitListenType
import ly.david.musicsearch.ui.common.screen.SnackbarPopResult
import ly.david.musicsearch.ui.common.screen.SubmitListenScreen
import kotlin.time.Clock

internal class SubmitListenPresenter(
    private val screen: SubmitListenScreen,
    private val navigator: Navigator,
    private val listensListRepository: ListensListRepository,
) : Presenter<SubmitListenUiState> {
    @Composable
    override fun present(): SubmitListenUiState {
        val coroutineScope = rememberCoroutineScope()

        var dateTimestampSeconds: Long by rememberSaveable { mutableLongStateOf(0) }
        var timeTimestampSeconds: Long by rememberSaveable { mutableLongStateOf(0) }
        var timestampIsStartTime by rememberSaveable { mutableStateOf(true) }
        var isCustomTime by rememberSaveable { mutableStateOf(false) }

        fun eventSink(event: SubmitListenUiEvent) {
            when (event) {
                is SubmitListenUiEvent.UpdateDateTimestamp -> {
                    dateTimestampSeconds = event.timestampSeconds
                }

                is SubmitListenUiEvent.UpdateTimeTimestamp -> {
                    timeTimestampSeconds = event.timestampSeconds
                }

                is SubmitListenUiEvent.UpdateTimestampIsStartTime -> {
                    timestampIsStartTime = event.isStartTime
                }

                is SubmitListenUiEvent.UpdateIsCustomTime -> {
                    isCustomTime = event.isCustomTime
                }

                is SubmitListenUiEvent.UpdateTimestampsToNow -> {
                    val now = Clock.System.now().toEpochMilliseconds() / MS_IN_SECOND
                    val lastMidnight = now - (now % SECONDS_IN_DAY)
                    dateTimestampSeconds = lastMidnight
                    timeTimestampSeconds = now - lastMidnight
                }

                is SubmitListenUiEvent.Submit -> {
                    coroutineScope.launch {
                        when (val listenType = screen.submitListenType) {
                            is SubmitListenType.Track -> {
                                val listenStartTime = dateTimestampSeconds + timeTimestampSeconds -
                                    if (timestampIsStartTime) {
                                        0
                                    } else {
                                        (listenType.lengthMilliseconds ?: 0) / MS_IN_SECOND
                                    }
                                listensListRepository.submitListens(
                                    listenSubmissions = listOf(
                                        ListenSubmission(
                                            listenedAtS = listenStartTime,
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
            dateTimestampSeconds = dateTimestampSeconds,
            timeTimestampSeconds = timeTimestampSeconds,
            timestampIsStartTime = timestampIsStartTime,
            isCustomTime = isCustomTime,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class SubmitListenUiState(
    val submitListenType: SubmitListenType,
    val dateTimestampSeconds: Long = 0,
    val timeTimestampSeconds: Long = 0,
    val timestampIsStartTime: Boolean = true,
    val isCustomTime: Boolean = false,
    val eventSink: (SubmitListenUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface SubmitListenUiEvent : CircuitUiEvent {
    data class UpdateDateTimestamp(val timestampSeconds: Long) : SubmitListenUiEvent
    data class UpdateTimeTimestamp(val timestampSeconds: Long) : SubmitListenUiEvent
    data class UpdateTimestampIsStartTime(val isStartTime: Boolean) : SubmitListenUiEvent
    data class UpdateIsCustomTime(val isCustomTime: Boolean) : SubmitListenUiEvent
    data object UpdateTimestampsToNow : SubmitListenUiEvent
    data object Submit : SubmitListenUiEvent
    data object Dismiss : SubmitListenUiEvent
}
