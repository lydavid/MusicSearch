package ly.david.musicsearch.shared.feature.details.event

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
import ly.david.musicsearch.core.models.event.EventScaffoldModel
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.history.LookupHistory
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.shared.domain.event.EventRepository
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.ui.common.relation.RelationsPresenter
import ly.david.ui.common.relation.RelationsUiEvent
import ly.david.ui.common.relation.RelationsUiState
import ly.david.ui.common.screen.DetailsScreen

internal class EventPresenter(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    private val repository: EventRepository,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val relationsPresenter: RelationsPresenter,
    private val logger: Logger,
) : Presenter<EventUiState> {

    @Composable
    override fun present(): EventUiState {
        var title by rememberSaveable { mutableStateOf(screen.title.orEmpty()) }
        var isError by rememberSaveable { mutableStateOf(false) }
        var recordedHistory by rememberSaveable { mutableStateOf(false) }
        var query by rememberSaveable { mutableStateOf("") }
        var event: EventScaffoldModel? by remember { mutableStateOf(null) }
        val tabs: List<EventTab> by rememberSaveable {
            mutableStateOf(EventTab.entries)
        }
        var selectedTab by rememberSaveable { mutableStateOf(EventTab.DETAILS) }
        var forceRefreshDetails by rememberSaveable { mutableStateOf(false) }

        val relationsUiState = relationsPresenter.present()
        val relationsEventSink = relationsUiState.eventSink

        LaunchedEffect(forceRefreshDetails) {
            try {
                val eventListItemModel = repository.lookupEvent(screen.id)
                if (title.isEmpty()) {
                    title = eventListItemModel.getNameWithDisambiguation()
                }
                event = eventListItemModel
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
                EventTab.DETAILS -> {
                    // Loaded above
                }

                EventTab.RELATIONSHIPS -> {
                    relationsEventSink(
                        RelationsUiEvent.GetRelations(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    relationsEventSink(RelationsUiEvent.UpdateQuery(query))
                }

                EventTab.STATS -> {
                    // Handled in UI
                }
            }
        }

        fun eventSink(event: EventUiEvent) {
            when (event) {
                EventUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is EventUiEvent.UpdateQuery -> {
                    query = event.query
                }

                is EventUiEvent.UpdateTab -> {
                    selectedTab = event.tab
                }

                is EventUiEvent.ClickItem -> {
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

                EventUiEvent.ForceRefresh -> {
                    forceRefreshDetails = true
                }
            }
        }

        return EventUiState(
            title = title,
            isError = isError,
            event = event,
            tabs = tabs,
            selectedTab = selectedTab,
            query = query,
            relationsUiState = relationsUiState,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class EventUiState(
    val title: String,
    val isError: Boolean,
    val event: EventScaffoldModel?,
    val tabs: List<EventTab>,
    val selectedTab: EventTab,
    val query: String,
    val relationsUiState: RelationsUiState,
    val eventSink: (EventUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface EventUiEvent : CircuitUiEvent {
    data object NavigateUp : EventUiEvent
    data object ForceRefresh : EventUiEvent
    data class UpdateTab(val tab: EventTab) : EventUiEvent
    data class UpdateQuery(val query: String) : EventUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : EventUiEvent
}
