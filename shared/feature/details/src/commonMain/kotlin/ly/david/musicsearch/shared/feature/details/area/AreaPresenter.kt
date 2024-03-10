package ly.david.musicsearch.shared.feature.details.area

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.area.AreaScaffoldModel
import ly.david.musicsearch.core.models.area.showReleases
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.history.LookupHistory
import ly.david.musicsearch.core.models.listitem.PlaceListItemModel
import ly.david.musicsearch.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.domain.area.AreaRepository
import ly.david.musicsearch.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.domain.place.usecase.GetPlacesByEntity
import ly.david.musicsearch.shared.screens.DetailsScreen
import ly.david.ui.common.paging.RelationsList
import ly.david.ui.common.release.ReleasesByEntityPresenter
import ly.david.ui.common.release.ReleasesByEntityUiEvent

internal class AreaPresenter(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    private val repository: AreaRepository,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val releasesByEntityPresenter: ReleasesByEntityPresenter,
    private val getPlacesByEntity: GetPlacesByEntity,
    private val relationsList: RelationsList,
    private val logger: Logger,
) : Presenter<AreaUiState> {
//    IRelationsList by relationsList {

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
        var placeListItems: Flow<PagingData<PlaceListItemModel>> by remember { mutableStateOf(emptyFlow()) }
        val releasesByEntityUiState = releasesByEntityPresenter.present()
        val releasesEventSink = releasesByEntityUiState.eventSink

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

        LaunchedEffect(
            key1 = query,
            key2 = selectedTab,
        ) {
            when (selectedTab) {
                AreaTab.DETAILS -> {
                    // Loaded above
                }

                AreaTab.RELATIONSHIPS -> {}
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
                    placeListItems = getPlacesByEntity(
                        entityId = screen.id,
                        entity = screen.entity,
                        listFilters = ListFilters(
                            query = query,
                        ),
                    )
                }

                AreaTab.STATS -> {}
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
            placesLazyPagingItems = placeListItems.collectAsLazyPagingItems(),
            releasesByEntityUiState = releasesByEntityUiState,
            eventSink = ::eventSink,
        )
    }

//    fun loadDataForTab(
//        areaId: String,
//        selectedTab: AreaTab,
//    ) {
//        when (selectedTab) {
//            AreaTab.DETAILS -> {

//            }
//
//            AreaTab.RELATIONSHIPS -> loadRelations(areaId)
//            else -> {
//                // Not handled here.
//            }
//        }
//    }
}
