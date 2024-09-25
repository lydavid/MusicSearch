package ly.david.musicsearch.shared.feature.details.recording

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.shared.domain.artist.getDisplayNames
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.history.LookupHistory
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.recording.RecordingDetailsModel
import ly.david.musicsearch.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.recording.RecordingRepository
import ly.david.musicsearch.ui.common.musicbrainz.LoginPresenter
import ly.david.musicsearch.ui.common.musicbrainz.LoginUiState
import ly.david.musicsearch.ui.common.relation.RelationsPresenter
import ly.david.musicsearch.ui.common.relation.RelationsUiEvent
import ly.david.musicsearch.ui.common.relation.RelationsUiState
import ly.david.musicsearch.ui.common.release.ReleasesByEntityPresenter
import ly.david.musicsearch.ui.common.release.ReleasesByEntityUiEvent
import ly.david.musicsearch.ui.common.release.ReleasesByEntityUiState
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

internal class RecordingPresenter(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    private val repository: RecordingRepository,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val releasesByEntityPresenter: ReleasesByEntityPresenter,
    private val relationsPresenter: RelationsPresenter,
    private val logger: Logger,
    private val loginPresenter: LoginPresenter,
) : Presenter<RecordingUiState> {

    @Composable
    override fun present(): RecordingUiState {
        var title by rememberSaveable { mutableStateOf(screen.title.orEmpty()) }
        var subtitle by rememberSaveable { mutableStateOf("") }
        var isError by rememberSaveable { mutableStateOf(false) }
        var recordedHistory by rememberSaveable { mutableStateOf(false) }
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val query = topAppBarFilterState.filterText
        var recording: RecordingDetailsModel? by remember { mutableStateOf(null) }
        val tabs: List<RecordingTab> by rememberSaveable {
            mutableStateOf(RecordingTab.entries)
        }
        var selectedTab by rememberSaveable { mutableStateOf(RecordingTab.DETAILS) }
        var forceRefreshDetails by rememberSaveable { mutableStateOf(false) }
        val detailsLazyListState = rememberLazyListState()

        val releasesByEntityUiState = releasesByEntityPresenter.present()
        val releasesEventSink = releasesByEntityUiState.eventSink
        val relationsUiState = relationsPresenter.present()
        val relationsEventSink = relationsUiState.eventSink

        val loginUiState = loginPresenter.present()

        LaunchedEffect(forceRefreshDetails) {
            try {
                val recordingDetailsModel = repository.lookupRecording(screen.id)
                title = recordingDetailsModel.getNameWithDisambiguation()
                subtitle = "Recording by ${recordingDetailsModel.artistCredits.getDisplayNames()}"
                recording = recordingDetailsModel
                isError = false
            } catch (ex: RecoverableNetworkException) {
                logger.e(ex)
                isError = true
            }
            if (!recordedHistory) {
                incrementLookupHistory(
                    LookupHistory(
                        mbid = screen.id,
                        title = title,
                        entity = screen.entity,
                    ),
                )
                recordedHistory = true
            }
        }

        LaunchedEffect(
            key1 = query,
            key2 = selectedTab,
        ) {
            when (selectedTab) {
                RecordingTab.DETAILS -> {
                    // Loaded above
                }

                RecordingTab.RELATIONSHIPS -> {
                    relationsEventSink(
                        RelationsUiEvent.GetRelations(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    relationsEventSink(RelationsUiEvent.UpdateQuery(query))
                }

                RecordingTab.RELEASES -> {
                    releasesEventSink(
                        ReleasesByEntityUiEvent.Get(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    releasesEventSink(ReleasesByEntityUiEvent.UpdateQuery(query))
                }

                RecordingTab.STATS -> {
                    // Handled in UI
                }
            }
        }

        fun eventSink(event: RecordingUiEvent) {
            when (event) {
                RecordingUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is RecordingUiEvent.UpdateTab -> {
                    selectedTab = event.tab
                }

                is RecordingUiEvent.ClickItem -> {
                    navigator.onNavEvent(
                        NavEvent.GoTo(
                            DetailsScreen(
                                entity = event.entity,
                                id = event.id,
                                title = event.title,
                            ),
                        ),
                    )
                }

                RecordingUiEvent.ForceRefresh -> {
                    forceRefreshDetails = true
                }
            }
        }

        return RecordingUiState(
            title = title,
            subtitle = subtitle,
            isError = isError,
            recording = recording,
            tabs = tabs,
            selectedTab = selectedTab,
            topAppBarFilterState = topAppBarFilterState,
            detailsLazyListState = detailsLazyListState,
            releasesByEntityUiState = releasesByEntityUiState,
            relationsUiState = relationsUiState,
            loginUiState = loginUiState,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class RecordingUiState(
    val title: String,
    val subtitle: String,
    val isError: Boolean,
    val recording: RecordingDetailsModel?,
    val tabs: List<RecordingTab>,
    val selectedTab: RecordingTab,
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val detailsLazyListState: LazyListState = LazyListState(),
    val releasesByEntityUiState: ReleasesByEntityUiState,
    val relationsUiState: RelationsUiState,
    val loginUiState: LoginUiState = LoginUiState(),
    val eventSink: (RecordingUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface RecordingUiEvent : CircuitUiEvent {
    data object NavigateUp : RecordingUiEvent
    data object ForceRefresh : RecordingUiEvent
    data class UpdateTab(val tab: RecordingTab) : RecordingUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : RecordingUiEvent
}
