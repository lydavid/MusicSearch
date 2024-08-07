package ly.david.musicsearch.shared.feature.details.instrument

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
import ly.david.musicsearch.core.models.instrument.InstrumentDetailsModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.instrument.InstrumentRepository
import ly.david.musicsearch.ui.common.relation.RelationsPresenter
import ly.david.musicsearch.ui.common.relation.RelationsUiEvent
import ly.david.musicsearch.ui.common.relation.RelationsUiState
import ly.david.musicsearch.ui.common.screen.DetailsScreen
import ly.david.musicsearch.ui.common.topappbar.TopAppBarFilterState
import ly.david.musicsearch.ui.common.topappbar.rememberTopAppBarFilterState

internal class InstrumentPresenter(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    private val repository: InstrumentRepository,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val relationsPresenter: RelationsPresenter,
    private val logger: Logger,
) : Presenter<InstrumentUiState> {

    @Composable
    override fun present(): InstrumentUiState {
        var title by rememberSaveable { mutableStateOf(screen.title.orEmpty()) }
        var isError by rememberSaveable { mutableStateOf(false) }
        var recordedHistory by rememberSaveable { mutableStateOf(false) }
        val topAppBarFilterState = rememberTopAppBarFilterState()
        val query = topAppBarFilterState.filterText
        var instrument: InstrumentDetailsModel? by remember { mutableStateOf(null) }
        val tabs: List<InstrumentTab> by rememberSaveable {
            mutableStateOf(InstrumentTab.entries)
        }
        var selectedTab by rememberSaveable { mutableStateOf(InstrumentTab.DETAILS) }
        var forceRefreshDetails by rememberSaveable { mutableStateOf(false) }
        val detailsLazyListState = rememberLazyListState()

        val relationsUiState = relationsPresenter.present()
        val relationsEventSink = relationsUiState.eventSink

        LaunchedEffect(forceRefreshDetails) {
            try {
                val instrumentDetailsModel = repository.lookupInstrument(screen.id)
                title = instrumentDetailsModel.getNameWithDisambiguation()
                instrument = instrumentDetailsModel
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
                InstrumentTab.DETAILS -> {
                    // Loaded above
                }

                InstrumentTab.RELATIONSHIPS -> {
                    relationsEventSink(
                        RelationsUiEvent.GetRelations(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    relationsEventSink(RelationsUiEvent.UpdateQuery(query))
                }

                InstrumentTab.STATS -> {
                    // Handled in UI
                }
            }
        }

        fun eventSink(event: InstrumentUiEvent) {
            when (event) {
                InstrumentUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is InstrumentUiEvent.UpdateTab -> {
                    selectedTab = event.tab
                }

                is InstrumentUiEvent.ClickItem -> {
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

                InstrumentUiEvent.ForceRefresh -> {
                    forceRefreshDetails = true
                }
            }
        }

        return InstrumentUiState(
            title = title,
            isError = isError,
            instrument = instrument,
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
internal data class InstrumentUiState(
    val title: String,
    val isError: Boolean,
    val instrument: InstrumentDetailsModel?,
    val tabs: List<InstrumentTab>,
    val selectedTab: InstrumentTab,
    val topAppBarFilterState: TopAppBarFilterState = TopAppBarFilterState(),
    val detailsLazyListState: LazyListState = LazyListState(),
    val relationsUiState: RelationsUiState,
    val eventSink: (InstrumentUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface InstrumentUiEvent : CircuitUiEvent {
    data object NavigateUp : InstrumentUiEvent
    data object ForceRefresh : InstrumentUiEvent
    data class UpdateTab(val tab: InstrumentTab) : InstrumentUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : InstrumentUiEvent
}
