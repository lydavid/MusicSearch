package ly.david.musicsearch.shared.feature.details.area

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.core.models.area.AreaScaffoldModel
import ly.david.musicsearch.core.models.area.showReleases
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.history.LookupHistory
import ly.david.musicsearch.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.domain.area.AreaRepository
import ly.david.musicsearch.domain.history.usecase.IncrementLookupHistory
import ly.david.ui.common.screen.DetailsScreen
import ly.david.ui.common.place.PlacesByEntityPresenter
import ly.david.ui.common.place.PlacesByEntityUiEvent
import ly.david.ui.common.relation.RelationsPresenter
import ly.david.ui.common.relation.RelationsUiEvent
import ly.david.ui.common.release.ReleasesByEntityPresenter
import ly.david.ui.common.release.ReleasesByEntityUiEvent

internal class AreaPresenter(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    private val repository: AreaRepository,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val releasesByEntityPresenter: ReleasesByEntityPresenter,
    private val placesByEntityPresenter: PlacesByEntityPresenter,
    private val relationsPresenter: RelationsPresenter,
//    private val areaStatsPresenter: AreaStatsPresenter,
    private val logger: Logger,
) : Presenter<AreaUiState> {

    @Composable
    override fun present(): AreaUiState {
        var title by rememberSaveable { mutableStateOf(screen.title.orEmpty()) }
        var isError by rememberSaveable { mutableStateOf(false) }
        var recordedHistory by rememberSaveable { mutableStateOf(false) }
        var query by rememberSaveable { mutableStateOf("") }
        var area: AreaScaffoldModel? by remember { mutableStateOf(null) }
        var tabs: List<AreaTab> by rememberSaveable {
            mutableStateOf(AreaTab.entries.filter { it != AreaTab.RELEASES })
        }
        var selectedTab by rememberSaveable { mutableStateOf(AreaTab.DETAILS) }

        val releasesByEntityUiState = releasesByEntityPresenter.present()
        val releasesEventSink = releasesByEntityUiState.eventSink
        val placesByEntityUiState = placesByEntityPresenter.present()
        val placesEventSink = placesByEntityUiState.eventSink
        val relationsUiState = relationsPresenter.present()
        val relationsEventSink = relationsUiState.eventSink
//        val statsUiState = areaStatsPresenter.present()
//        val statsEventSink = statsUiState.eventSink

        LaunchedEffect(Unit) {
            try {
                val areaScaffoldModel = repository.lookupArea(screen.id)
                if (title.isEmpty()) {
                    title = areaScaffoldModel.getNameWithDisambiguation()
                }
                if (areaScaffoldModel.showReleases()) {
                    tabs = AreaTab.entries
                }
                area = areaScaffoldModel
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
                        searchHint = area?.sortName.orEmpty(),
                    ),
                )
                recordedHistory = true
            }
        }

        // TODO: good candidate for extraction
        // TODO: handle force refresh
        LaunchedEffect(
            key1 = query,
            key2 = selectedTab,
        ) {
            when (selectedTab) {
                AreaTab.DETAILS -> {
                    // Loaded above
                }

                AreaTab.RELATIONSHIPS -> {
                    relationsEventSink(
                        RelationsUiEvent.GetRelations(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    relationsEventSink(RelationsUiEvent.UpdateQuery(query))
                }

                AreaTab.RELEASES -> {
                    releasesEventSink(
                        ReleasesByEntityUiEvent.GetReleases(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    releasesEventSink(ReleasesByEntityUiEvent.UpdateQuery(query))
                }

                AreaTab.PLACES -> {
                    placesEventSink(
                        PlacesByEntityUiEvent.GetPlaces(
                            byEntityId = screen.id,
                            byEntity = screen.entity,
                        ),
                    )
                    placesEventSink(PlacesByEntityUiEvent.UpdateQuery(query))
                }

                AreaTab.STATS -> {
//                    statsEventSink(StatsUiEvent.GetStats(screen.id))
//                    navigator.goTo(AreaStatsScreen(screen.id))
                }
            }
        }

        fun eventSink(event: AreaUiEvent) {
            when (event) {
                AreaUiEvent.NavigateUp -> {
                    navigator.pop()
                }

                is AreaUiEvent.UpdateQuery -> {
                    query = event.query
                }

                is AreaUiEvent.UpdateTab -> {
                    selectedTab = event.tab
                }
            }
        }

        return AreaUiState(
            title = title,
            isError = isError,
            area = area,
            tabs = tabs,
            selectedTab = selectedTab,
            query = query,
            placesByEntityUiState = placesByEntityUiState,
            releasesByEntityUiState = releasesByEntityUiState,
            relationsUiState = relationsUiState,
//            statsUiState = statsUiState,
            eventSink = ::eventSink,
        )
    }
}
