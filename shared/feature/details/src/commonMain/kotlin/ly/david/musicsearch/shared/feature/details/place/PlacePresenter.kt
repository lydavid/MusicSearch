package ly.david.musicsearch.shared.feature.details.place

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
import ly.david.musicsearch.core.models.place.PlaceScaffoldModel
import ly.david.musicsearch.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.domain.place.PlaceRepository
import ly.david.ui.common.event.EventsByEntityPresenter
import ly.david.ui.common.event.EventsByEntityUiEvent
import ly.david.ui.common.relation.RelationsPresenter
import ly.david.ui.common.relation.RelationsUiEvent
import ly.david.ui.common.screen.DetailsScreen

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
        var query by rememberSaveable { mutableStateOf("") }
        var place: PlaceScaffoldModel? by remember { mutableStateOf(null) }
        val tabs: List<PlaceTab> by rememberSaveable {
            mutableStateOf(PlaceTab.entries)
        }
        var selectedTab by rememberSaveable { mutableStateOf(PlaceTab.DETAILS) }
        var forceRefreshDetails by rememberSaveable { mutableStateOf(false) }

        val eventsByEntityUiState = eventsByEntityPresenter.present()
        val eventsEventSink = eventsByEntityUiState.eventSink
        val relationsUiState = relationsPresenter.present()
        val relationsEventSink = relationsUiState.eventSink

        LaunchedEffect(forceRefreshDetails) {
            try {
                val placeScaffoldModel = repository.lookupPlace(screen.id)
                if (title.isEmpty()) {
                    title = placeScaffoldModel.getNameWithDisambiguation()
                }
                place = placeScaffoldModel
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

                is PlaceUiEvent.UpdateQuery -> {
                    query = event.query
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
            query = query,
            eventsByEntityUiState = eventsByEntityUiState,
            relationsUiState = relationsUiState,
            eventSink = ::eventSink,
        )
    }
}
