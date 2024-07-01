package ly.david.musicsearch.shared.feature.details.label

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
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.history.LookupHistory
import ly.david.musicsearch.core.models.label.LabelScaffoldModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.label.LabelRepository
import ly.david.ui.common.relation.RelationsPresenter
import ly.david.ui.common.relation.RelationsUiEvent
import ly.david.ui.common.relation.RelationsUiState
import ly.david.ui.common.release.ReleasesByEntityPresenter
import ly.david.ui.common.release.ReleasesByEntityUiEvent
import ly.david.ui.common.release.ReleasesByEntityUiState
import ly.david.ui.common.screen.DetailsScreen

internal class LabelPresenter(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    private val repository: LabelRepository,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val releasesByEntityPresenter: ReleasesByEntityPresenter,
    private val relationsPresenter: RelationsPresenter,
    private val logger: Logger,
) : Presenter<LabelUiState> {

    @Composable
    override fun present(): LabelUiState {
        var title by rememberSaveable { mutableStateOf(screen.title.orEmpty()) }
        var isError by rememberSaveable { mutableStateOf(false) }
        var recordedHistory by rememberSaveable { mutableStateOf(false) }
        var query by rememberSaveable { mutableStateOf("") }
        var label: LabelScaffoldModel? by remember { mutableStateOf(null) }
        val tabs: List<LabelTab> by rememberSaveable {
            mutableStateOf(LabelTab.entries)
        }
        var selectedTab by rememberSaveable { mutableStateOf(LabelTab.DETAILS) }
        var forceRefreshDetails by rememberSaveable { mutableStateOf(false) }

        val releasesByEntityUiState = releasesByEntityPresenter.present()
        val releasesEventSink = releasesByEntityUiState.eventSink
        val relationsUiState = relationsPresenter.present()
        val relationsEventSink = relationsUiState.eventSink

        LaunchedEffect(forceRefreshDetails) {
            try {
                val labelScaffoldModel = repository.lookupLabel(screen.id)
                if (title.isEmpty()) {
                    title = labelScaffoldModel.getNameWithDisambiguation()
                }
                label = labelScaffoldModel
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
                LabelTab.DETAILS -> {
                    // Loaded above
                }

                LabelTab.RELATIONSHIPS -> {
                    relationsEventSink(
                        RelationsUiEvent.GetRelations(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    relationsEventSink(RelationsUiEvent.UpdateQuery(query))
                }

                LabelTab.RELEASES -> {
                    releasesEventSink(
                        ReleasesByEntityUiEvent.Get(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    releasesEventSink(ReleasesByEntityUiEvent.UpdateQuery(query))
                }

                LabelTab.STATS -> {
                    // Handled in UI
                }
            }
        }

        fun eventSink(event: LabelUiEvent) {
            when (event) {
                LabelUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is LabelUiEvent.UpdateQuery -> {
                    query = event.query
                }

                is LabelUiEvent.UpdateTab -> {
                    selectedTab = event.tab
                }

                is LabelUiEvent.ClickItem -> {
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

                LabelUiEvent.ForceRefresh -> {
                    forceRefreshDetails = true
                }
            }
        }

        return LabelUiState(
            title = title,
            isError = isError,
            label = label,
            tabs = tabs,
            selectedTab = selectedTab,
            query = query,
            releasesByEntityUiState = releasesByEntityUiState,
            relationsUiState = relationsUiState,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class LabelUiState(
    val title: String,
    val isError: Boolean,
    val label: LabelScaffoldModel?,
    val tabs: List<LabelTab>,
    val selectedTab: LabelTab,
    val query: String,
    val releasesByEntityUiState: ReleasesByEntityUiState,
    val relationsUiState: RelationsUiState,
    val eventSink: (LabelUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface LabelUiEvent : CircuitUiEvent {
    data object NavigateUp : LabelUiEvent
    data object ForceRefresh : LabelUiEvent
    data class UpdateTab(val tab: LabelTab) : LabelUiEvent
    data class UpdateQuery(val query: String) : LabelUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : LabelUiEvent
}
