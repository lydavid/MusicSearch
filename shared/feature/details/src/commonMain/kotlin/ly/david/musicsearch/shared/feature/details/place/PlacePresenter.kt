package ly.david.musicsearch.shared.feature.details.place

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
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.history.LookupHistory
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.place.PlaceDetailsModel
import ly.david.musicsearch.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.place.PlaceRepository
import ly.david.musicsearch.ui.common.event.EventsByEntityPresenter
import ly.david.musicsearch.ui.common.event.EventsByEntityUiEvent
import ly.david.musicsearch.ui.common.event.EventsByEntityUiState
import ly.david.musicsearch.ui.common.relation.RelationsPresenter
import ly.david.musicsearch.ui.common.relation.RelationsUiEvent
import ly.david.musicsearch.ui.common.relation.RelationsUiState
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

internal class PlacePresenter(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    private val repository: PlaceRepository,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val eventsByEntityPresenter: EventsByEntityPresenter,
    private val relationsPresenter: RelationsPresenter,
    private val logger: Logger,
) : Presenter<PlaceUiState> {

    @Composable
    override fun present(): PlaceUiState {
        var title by rememberSaveable { mutableStateOf(screen.title.orEmpty()) }
        var isError by rememberSaveable { mutableStateOf(false) }
        var recordedHistory by rememberSaveable { mutableStateOf(false) }
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val query = topAppBarFilterState.filterText
        var place: PlaceDetailsModel? by remember { mutableStateOf(null) }
        val tabs: List<PlaceTab> by rememberSaveable {
            mutableStateOf(PlaceTab.entries)
        }
        var selectedTab by rememberSaveable { mutableStateOf(PlaceTab.DETAILS) }
        var forceRefreshDetails by rememberSaveable { mutableStateOf(false) }
        val detailsLazyListState = rememberLazyListState()

        val eventsByEntityUiState = eventsByEntityPresenter.present()
        val eventsEventSink = eventsByEntityUiState.eventSink
        val relationsUiState = relationsPresenter.present()
        val relationsEventSink = relationsUiState.eventSink

        LaunchedEffect(forceRefreshDetails) {
            try {
                val placeDetailsModel = repository.lookupPlace(screen.id)
                title = placeDetailsModel.getNameWithDisambiguation()
                place = placeDetailsModel
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
                PlaceTab.DETAILS -> {
                    // Loaded above
                }

                PlaceTab.RELATIONSHIPS -> {
                    relationsEventSink(
                        RelationsUiEvent.GetRelations(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    relationsEventSink(RelationsUiEvent.UpdateQuery(query))
                }

                PlaceTab.EVENTS -> {
                    eventsEventSink(
                        EventsByEntityUiEvent.Get(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    eventsEventSink(EventsByEntityUiEvent.UpdateQuery(query))
                }

                PlaceTab.STATS -> {
                    // Handled in UI
                }
            }
        }

        fun eventSink(event: PlaceUiEvent) {
            when (event) {
                PlaceUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is PlaceUiEvent.UpdateTab -> {
                    selectedTab = event.tab
                }

                is PlaceUiEvent.ClickItem -> {
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

                PlaceUiEvent.ForceRefresh -> {
                    forceRefreshDetails = true
                }
            }
        }

        return PlaceUiState(
            title = title,
            isError = isError,
            place = place,
            tabs = tabs,
            selectedTab = selectedTab,
            topAppBarFilterState = topAppBarFilterState,
            detailsLazyListState = detailsLazyListState,
            eventsByEntityUiState = eventsByEntityUiState,
            relationsUiState = relationsUiState,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class PlaceUiState(
    val title: String,
    val isError: Boolean,
    val place: PlaceDetailsModel?,
    val tabs: List<PlaceTab>,
    val selectedTab: PlaceTab,
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val detailsLazyListState: LazyListState = LazyListState(),
    val eventsByEntityUiState: EventsByEntityUiState,
    val relationsUiState: RelationsUiState,
    val eventSink: (PlaceUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface PlaceUiEvent : CircuitUiEvent {
    data object NavigateUp : PlaceUiEvent
    data object ForceRefresh : PlaceUiEvent
    data class UpdateTab(val tab: PlaceTab) : PlaceUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : PlaceUiEvent
}
