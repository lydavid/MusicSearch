package ly.david.musicsearch.shared.feature.details.series

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
import ly.david.musicsearch.core.models.series.SeriesScaffoldModel
import ly.david.musicsearch.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.shared.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.shared.domain.series.SeriesRepository
import ly.david.ui.common.relation.RelationsPresenter
import ly.david.ui.common.relation.RelationsUiEvent
import ly.david.ui.common.relation.RelationsUiState
import ly.david.ui.common.screen.DetailsScreen

internal class SeriesPresenter(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    private val repository: SeriesRepository,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val relationsPresenter: RelationsPresenter,
    private val logger: Logger,
) : Presenter<SeriesUiState> {

    @Composable
    override fun present(): SeriesUiState {
        var title by rememberSaveable { mutableStateOf(screen.title.orEmpty()) }
        var isError by rememberSaveable { mutableStateOf(false) }
        var recordedHistory by rememberSaveable { mutableStateOf(false) }
        var query by rememberSaveable { mutableStateOf("") }
        var series: SeriesScaffoldModel? by remember { mutableStateOf(null) }
        val tabs: List<SeriesTab> by rememberSaveable {
            mutableStateOf(SeriesTab.entries)
        }
        var selectedTab by rememberSaveable { mutableStateOf(SeriesTab.DETAILS) }
        var forceRefreshDetails by rememberSaveable { mutableStateOf(false) }

        val relationsUiState = relationsPresenter.present()
        val relationsEventSink = relationsUiState.eventSink

        LaunchedEffect(forceRefreshDetails) {
            try {
                val seriesScaffoldModel = repository.lookupSeries(screen.id)
                if (title.isEmpty()) {
                    title = seriesScaffoldModel.getNameWithDisambiguation()
                }
                series = seriesScaffoldModel
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
                SeriesTab.DETAILS -> {
                    // Loaded above
                }

                SeriesTab.RELATIONSHIPS -> {
                    relationsEventSink(
                        RelationsUiEvent.GetRelations(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    relationsEventSink(RelationsUiEvent.UpdateQuery(query))
                }

                SeriesTab.STATS -> {
                    // Handled in UI
                }
            }
        }

        fun eventSink(event: SeriesUiEvent) {
            when (event) {
                SeriesUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is SeriesUiEvent.UpdateQuery -> {
                    query = event.query
                }

                is SeriesUiEvent.UpdateTab -> {
                    selectedTab = event.tab
                }

                is SeriesUiEvent.ClickItem -> {
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

                SeriesUiEvent.ForceRefresh -> {
                    forceRefreshDetails = true
                }
            }
        }

        return SeriesUiState(
            title = title,
            isError = isError,
            series = series,
            tabs = tabs,
            selectedTab = selectedTab,
            query = query,
            relationsUiState = relationsUiState,
            eventSink = ::eventSink,
        )
    }
}

@Stable
internal data class SeriesUiState(
    val title: String,
    val isError: Boolean,
    val series: SeriesScaffoldModel?,
    val tabs: List<SeriesTab>,
    val selectedTab: SeriesTab,
    val query: String,
    val relationsUiState: RelationsUiState,
    val eventSink: (SeriesUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface SeriesUiEvent : CircuitUiEvent {
    data object NavigateUp : SeriesUiEvent
    data object ForceRefresh : SeriesUiEvent
    data class UpdateTab(val tab: SeriesTab) : SeriesUiEvent
    data class UpdateQuery(val query: String) : SeriesUiEvent
    data class ClickItem(
        val entity: MusicBrainzEntity,
        val id: String,
        val title: String?,
    ) : SeriesUiEvent
}
