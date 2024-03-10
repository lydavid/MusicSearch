package ly.david.musicsearch.shared.feature.details.area

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import app.cash.paging.PagingData
import app.cash.paging.cachedIn
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import ly.david.musicsearch.core.logging.Logger
import ly.david.musicsearch.core.models.ListFilters
import ly.david.musicsearch.core.models.area.AreaScaffoldModel
import ly.david.musicsearch.core.models.area.showReleases
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.history.LookupHistory
import ly.david.musicsearch.core.models.listitem.PlaceListItemModel
import ly.david.musicsearch.core.models.listitem.ReleaseListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.preferences.AppPreferences
import ly.david.musicsearch.data.common.network.RecoverableNetworkException
import ly.david.musicsearch.domain.area.AreaRepository
import ly.david.musicsearch.domain.history.usecase.IncrementLookupHistory
import ly.david.musicsearch.domain.place.usecase.GetPlacesByEntity
import ly.david.musicsearch.domain.release.ReleaseImageRepository
import ly.david.musicsearch.domain.release.usecase.GetReleasesByEntity
import ly.david.musicsearch.domain.releasegroup.ReleaseGroupImageRepository
import ly.david.musicsearch.shared.screens.DetailsScreen
import ly.david.ui.common.paging.RelationsList

internal class AreaPresenter(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    private val repository: AreaRepository,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val appPreferences: AppPreferences,
    private val getPlacesByEntity: GetPlacesByEntity,
    private val getReleasesByEntity: GetReleasesByEntity,
    private val relationsList: RelationsList,

    // TODO: extract for this and collections?
    private val releaseGroupImageRepository: ReleaseGroupImageRepository,
    private val releaseImageRepository: ReleaseImageRepository,
    private val logger: Logger,
) : Presenter<AreaUiState> {
//    MusicBrainzEntityViewModel,
//    IRelationsList by relationsList {

    @Composable
    override fun present(): AreaUiState {
        val scope = rememberCoroutineScope()
        var title by rememberSaveable { mutableStateOf(screen.title.orEmpty()) }
        var isError by rememberSaveable { mutableStateOf(false) }
        var recordedHistory by rememberSaveable { mutableStateOf(false) }
        var query by rememberSaveable { mutableStateOf("") }
        val showMoreInfoInReleaseListItem by appPreferences.showMoreInfoInReleaseListItem.collectAsState(true)
        var area: AreaScaffoldModel? by remember { mutableStateOf(null) }
        var tabs: List<AreaTab> by rememberSaveable {
            mutableStateOf(AreaTab.entries.filter { it != AreaTab.RELEASES })
        }
        var selectedTab by rememberSaveable { mutableStateOf(AreaTab.DETAILS) }
        var placeListItems: Flow<PagingData<PlaceListItemModel>> by remember { mutableStateOf(emptyFlow()) }
        var releaseListItems: Flow<PagingData<ReleaseListItemModel>> by remember { mutableStateOf(emptyFlow()) }
        val releasesLazyListState = rememberLazyListState()

//        val releaseListItems =
//            getReleasesByEntity(
//                entityId = screen.id,
//                entity = screen.entity,
//                listFilters = ListFilters(
//                    query = query,
//                ),
//            )
//                .collectAsLazyPagingItems()

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
            key2 = showMoreInfoInReleaseListItem,
            key3 = selectedTab,
        ) {
            when (selectedTab) {
                AreaTab.DETAILS -> {
                    // Loaded above
                }
                AreaTab.RELATIONSHIPS -> {}
                AreaTab.RELEASES -> {
                    // TODO: unfortunately, we're reloading this every time, losing our place when switching tabs
                    //  .distinctUntilChanged() and .cachedIn(scope) doesn't help
                    //  retained doesn't help either, it's for retaining state from backstack
                    // TODO: I wonder if making each tab a screen could work?
                    releaseListItems = getReleasesByEntity(
                        entityId = screen.id,
                        entity = screen.entity,
                        listFilters = ListFilters(
                            query = query,
                        )
                    )
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

                is AreaUiEvent.UpdateShowMoreInfoInReleaseListItem -> {
                    appPreferences.setShowMoreInfoInReleaseListItem(event.showMore)
                }

                is AreaUiEvent.UpdateQuery -> {
                    query = event.query
                }

                is AreaUiEvent.UpdateTab -> {
                    selectedTab = event.tab
                }

                is AreaUiEvent.RequestForMissingCoverArtUrl -> {
                    scope.launch {
                        when (event.entity) {
                            MusicBrainzEntity.RELEASE -> {
                                releaseImageRepository.getReleaseCoverArtUrlFromNetwork(
                                    releaseId = event.entityId,
                                    thumbnail = true,
                                )
                            }

                            MusicBrainzEntity.RELEASE_GROUP -> {
                                releaseGroupImageRepository.getReleaseGroupCoverArtUrlFromNetwork(
                                    releaseGroupId = event.entityId,
                                    thumbnail = true,
                                )
                            }

                            else -> {
                                // no-op
                            }
                        }
                    }
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
            showMoreInfoInReleaseListItem = showMoreInfoInReleaseListItem,
            placesLazyPagingItems = placeListItems.collectAsLazyPagingItems(),
            releasesLazyPagingItems = releaseListItems.collectAsLazyPagingItems(),
            releasesLazyListState = releasesLazyListState,
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
