package ly.david.musicsearch.shared.feature.details.event

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
import ly.david.musicsearch.shared.domain.event.EventDetailsModel
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.history.LookupHistory
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.shared.domain.event.EventRepository
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.ui.common.relation.RelationsPresenter
import ly.david.musicsearch.ui.common.relation.RelationsUiEvent
import ly.david.musicsearch.ui.common.relation.RelationsUiState
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

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
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val query = topAppBarFilterState.filterText
        var event: EventDetailsModel? by remember { mutableStateOf(null) }
        val tabs: List<EventTab> by rememberSaveable {
            mutableStateOf(EventTab.entries)
        }
        var selectedTab by rememberSaveable { mutableStateOf(EventTab.DETAILS) }
        var forceRefreshDetails by rememberSaveable { mutableStateOf(false) }
        val detailsLazyListState = rememberLazyListState()

        val relationsUiState = relationsPresenter.present()
        val relationsEventSink = relationsUiState.eventSink

        LaunchedEffect(forceRefreshDetails) {
            try {
                val eventListItemModel = repository.lookupEvent(screen.id)
                title = eventListItemModel.getNameWithDisambiguation()
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
            topAppBarFilterState = topAppBarFilterState,
            detailsLazyListState = detailsLazyListState,
            relationsUiState = relationsUiState,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class EventUiState(
    val title: String,
    val isError: Boolean,
    val event: EventDetailsModel?,
    val tabs: List<EventTab>,
    val selectedTab: EventTab,
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val detailsLazyListState: LazyListState = LazyListState(),
    val relationsUiState: RelationsUiState,
    val eventSink: (EventUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface EventUiEvent : CircuitUiEvent {
    data object NavigateUp : EventUiEvent
    data object ForceRefresh : EventUiEvent
    data class UpdateTab(val tab: EventTab) : EventUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : EventUiEvent
}
