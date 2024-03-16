package ly.david.musicsearch.shared.feature.details.work

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
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.history.LookupHistory
import ly.david.musicsearch.core.models.work.WorkScaffoldModel
import ly.david.musicsearch.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.domain.work.WorkRepository
import ly.david.ui.common.recording.RecordingsByEntityPresenter
import ly.david.ui.common.recording.RecordingsByEntityUiEvent
import ly.david.ui.common.relation.RelationsPresenter
import ly.david.ui.common.relation.RelationsUiEvent
import ly.david.ui.common.screen.DetailsScreen

internal class WorkPresenter(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    private val repository: WorkRepository,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val recordingsByEntityPresenter: RecordingsByEntityPresenter,
    private val relationsPresenter: RelationsPresenter,
    private val logger: Logger,
) : Presenter<WorkUiState> {

    @Composable
    override fun present(): WorkUiState {
        var title by rememberSaveable { mutableStateOf(screen.title.orEmpty()) }
        var isError by rememberSaveable { mutableStateOf(false) }
        var recordedHistory by rememberSaveable { mutableStateOf(false) }
        var query by rememberSaveable { mutableStateOf("") }
        var work: WorkScaffoldModel? by remember { mutableStateOf(null) }
        val tabs: List<WorkTab> by rememberSaveable {
            mutableStateOf(WorkTab.entries)
        }
        var selectedTab by rememberSaveable { mutableStateOf(WorkTab.DETAILS) }
        var forceRefreshDetails by rememberSaveable { mutableStateOf(false) }

        val recordingsByEntityUiState = recordingsByEntityPresenter.present()
        val recordingsEventSink = recordingsByEntityUiState.eventSink
        val relationsUiState = relationsPresenter.present()
        val relationsEventSink = relationsUiState.eventSink

        LaunchedEffect(forceRefreshDetails) {
            try {
                val workScaffoldModel = repository.lookupWork(screen.id)
                if (title.isEmpty()) {
                    title = workScaffoldModel.getNameWithDisambiguation()
                }
                work = workScaffoldModel
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
                WorkTab.DETAILS -> {
                    // Loaded above
                }

                WorkTab.RELATIONSHIPS -> {
                    relationsEventSink(
                        RelationsUiEvent.GetRelations(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    relationsEventSink(RelationsUiEvent.UpdateQuery(query))
                }

                WorkTab.RECORDINGS -> {
                    recordingsEventSink(
                        RecordingsByEntityUiEvent.Get(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    recordingsEventSink(RecordingsByEntityUiEvent.UpdateQuery(query))
                }

                WorkTab.STATS -> {
                    // Handled in UI
                }
            }
        }

        fun eventSink(event: WorkUiEvent) {
            when (event) {
                WorkUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is WorkUiEvent.UpdateQuery -> {
                    query = event.query
                }

                is WorkUiEvent.UpdateTab -> {
                    selectedTab = event.tab
                }

                is WorkUiEvent.ClickItem -> {
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

                WorkUiEvent.ForceRefresh -> {
                    forceRefreshDetails = true
                }
            }
        }

        return WorkUiState(
            title = title,
            isError = isError,
            work = work,
            tabs = tabs,
            selectedTab = selectedTab,
            query = query,
            recordingsByEntityUiState = recordingsByEntityUiState,
            relationsUiState = relationsUiState,
            eventSink = ::eventSink,
        )
    }
}
