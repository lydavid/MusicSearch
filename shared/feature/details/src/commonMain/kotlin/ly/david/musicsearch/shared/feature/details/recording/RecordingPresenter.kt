package ly.david.musicsearch.shared.feature.details.recording

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.slack.circuit.foundation.NavEvent
import com.slack.circuit.foundation.onNavEvent
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.core.models.artist.getDisplayNames
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.history.LookupHistory
import ly.david.musicsearch.core.models.recording.RecordingScaffoldModel
import ly.david.musicsearch.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.domain.recording.RecordingRepository
import ly.david.ui.common.relation.RelationsPresenter
import ly.david.ui.common.relation.RelationsUiEvent
import ly.david.ui.common.release.ReleasesByEntityPresenter
import ly.david.ui.common.release.ReleasesByEntityUiEvent
import ly.david.ui.common.screen.DetailsScreen

internal class RecordingPresenter(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    private val repository: RecordingRepository,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val releasesByEntityPresenter: ReleasesByEntityPresenter,
    private val relationsPresenter: RelationsPresenter,
    private val logger: Logger,
) : Presenter<RecordingUiState> {

    @Composable
    override fun present(): RecordingUiState {
        var title by rememberSaveable { mutableStateOf(screen.title.orEmpty()) }
        var subtitle by rememberSaveable { mutableStateOf("") }
        var isError by rememberSaveable { mutableStateOf(false) }
        var recordedHistory by rememberSaveable { mutableStateOf(false) }
        var query by rememberSaveable { mutableStateOf("") }
        var recording: RecordingScaffoldModel? by remember { mutableStateOf(null) }
        val tabs: List<RecordingTab> by rememberSaveable {
            mutableStateOf(RecordingTab.entries)
        }
        var selectedTab by rememberSaveable { mutableStateOf(RecordingTab.DETAILS) }
        var forceRefreshDetails by rememberSaveable { mutableStateOf(false) }

        val releasesByEntityUiState = releasesByEntityPresenter.present()
        val releasesEventSink = releasesByEntityUiState.eventSink
        val relationsUiState = relationsPresenter.present()
        val relationsEventSink = relationsUiState.eventSink

        LaunchedEffect(forceRefreshDetails) {
            try {
                val recordingScaffoldModel = repository.lookupRecording(screen.id)
                if (title.isEmpty()) {
                    title = recordingScaffoldModel.getNameWithDisambiguation()
                }
                subtitle = "Recording by ${recordingScaffoldModel.artistCredits.getDisplayNames()}"
                recording = recordingScaffoldModel
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

                is RecordingUiEvent.UpdateQuery -> {
                    query = event.query
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
            query = query,
            releasesByEntityUiState = releasesByEntityUiState,
            relationsUiState = relationsUiState,
            eventSink = ::eventSink,
        )
    }
}