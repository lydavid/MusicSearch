package ly.david.musicsearch.shared.feature.details.instrument

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
import ly.david.musicsearch.core.models.instrument.InstrumentScaffoldModel
import ly.david.musicsearch.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.domain.instrument.InstrumentRepository
import ly.david.ui.common.relation.RelationsPresenter
import ly.david.ui.common.relation.RelationsUiEvent
import ly.david.ui.common.screen.DetailsScreen

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
        var query by rememberSaveable { mutableStateOf("") }
        var instrument: InstrumentScaffoldModel? by remember { mutableStateOf(null) }
        val tabs: List<InstrumentTab> by rememberSaveable {
            mutableStateOf(InstrumentTab.entries)
        }
        var selectedTab by rememberSaveable { mutableStateOf(InstrumentTab.DETAILS) }
        var forceRefreshDetails by rememberSaveable { mutableStateOf(false) }

        val relationsUiState = relationsPresenter.present()
        val relationsEventSink = relationsUiState.eventSink

        LaunchedEffect(forceRefreshDetails) {
            try {
                val instrumentScaffoldModel = repository.lookupInstrument(screen.id)
                if (title.isEmpty()) {
                    title = instrumentScaffoldModel.getNameWithDisambiguation()
                }
                instrument = instrumentScaffoldModel
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

                is InstrumentUiEvent.UpdateQuery -> {
                    query = event.query
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
            query = query,
            relationsUiState = relationsUiState,
            eventSink = ::eventSink,
        )
    }
}
