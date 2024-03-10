package ly.david.musicsearch.shared.feature.details.area

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
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
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

// TODO: extract to common
internal data class ReleasesByEntityUiState(
    val releasesLazyPagingItems: LazyPagingItems<ReleaseListItemModel>,
    val showMoreInfo: Boolean,
    val eventSink: (ReleasesByEntityUiEvent) -> Unit,
) : CircuitUiState

internal sealed interface ReleasesByEntityUiEvent : CircuitUiEvent {
    data class GetReleases(
        val id: String,
        val entity: MusicBrainzEntity,
    ) : ReleasesByEntityUiEvent

    data class UpdateQuery(val query: String) : ReleasesByEntityUiEvent
    data class UpdateShowMoreInfoInReleaseListItem(val showMore: Boolean) : ReleasesByEntityUiEvent
    data class RequestForMissingCoverArtUrl(
        val entityId: String,
        val entity: MusicBrainzEntity,
    ) : ReleasesByEntityUiEvent
}

// TODO: when nav to details screen, the factory for this is not available, so "No definition found"
//  these nested presenters need to always be available, but that means we can't use assisted injection
internal class ReleasesByEntityPresenter(
//    private val screen: ReleasesByEntityScreen,
    private val getReleasesByEntity: GetReleasesByEntity,
    private val appPreferences: AppPreferences,
    private val releaseImageRepository: ReleaseImageRepository,
) : Presenter<ReleasesByEntityUiState> {
    @Composable
    override fun present(): ReleasesByEntityUiState {
        val scope = rememberCoroutineScope()
        val showMoreInfoInReleaseListItem by appPreferences.showMoreInfoInReleaseListItem.collectAsState(true)
        var query by rememberSaveable { mutableStateOf("") }
        var id: String by rememberSaveable { mutableStateOf("") }
        var entity: MusicBrainzEntity? by rememberSaveable { mutableStateOf(null) }
        var releaseListItems: Flow<PagingData<ReleaseListItemModel>> by remember { mutableStateOf(emptyFlow()) }

        LaunchedEffect(
            key1 = id,
            key2 = entity,
            key3 = query,
        ) {
            if (id.isEmpty()) return@LaunchedEffect
            val capturedEntity = entity ?: return@LaunchedEffect

            releaseListItems = getReleasesByEntity(
                entityId = id,
                entity = capturedEntity,
                listFilters = ListFilters(
                    query = query,
                ),
            )
                .distinctUntilChanged()
                .cachedIn(scope)
        }

        fun eventSink(event: ReleasesByEntityUiEvent) {
            when (event) {
                is ReleasesByEntityUiEvent.RequestForMissingCoverArtUrl -> {
                    scope.launch {
                        releaseImageRepository.getReleaseCoverArtUrlFromNetwork(
                            releaseId = event.entityId,
                            thumbnail = true,
                        )
                    }
                }

                is ReleasesByEntityUiEvent.UpdateShowMoreInfoInReleaseListItem -> {
                    appPreferences.setShowMoreInfoInReleaseListItem(event.showMore)
                }

                is ReleasesByEntityUiEvent.GetReleases -> {
                    id = event.id
                    entity = event.entity
                }

                is ReleasesByEntityUiEvent.UpdateQuery -> {
                    query = event.query
                }
            }
        }

        return ReleasesByEntityUiState(
            releasesLazyPagingItems = releaseListItems.collectAsLazyPagingItems(),
            showMoreInfo = showMoreInfoInReleaseListItem,
            eventSink = ::eventSink,
        )
    }
}

internal class AreaPresenter(
    private val screen: DetailsScreen,
    private val navigator: Navigator,
    private val repository: AreaRepository,
    private val incrementLookupHistory: IncrementLookupHistory,
    private val releasesByEntityPresenter: ReleasesByEntityPresenter,
    private val getPlacesByEntity: GetPlacesByEntity,
    private val relationsList: RelationsList,

    // TODO: extract for this and collections?
    private val releaseGroupImageRepository: ReleaseGroupImageRepository,
    private val logger: Logger,
) : Presenter<AreaUiState> {
//    MusicBrainzEntityViewModel,
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
        val releasesByEntityEventSink = releasesByEntityUiState.eventSink

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
            key2 = selectedTab,
        ) {
            when (selectedTab) {
                AreaTab.DETAILS -> {
                    // Loaded above
                }

                AreaTab.RELATIONSHIPS -> {}
                AreaTab.RELEASES -> {
                    // TODO: filter for just this tab instead of next tab as well like we used to
                    releasesByEntityEventSink(
                        ReleasesByEntityUiEvent.GetReleases(
                            screen.id,
                            screen.entity,
                        ),
                    )
                    releasesByEntityEventSink(ReleasesByEntityUiEvent.UpdateQuery(query))
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

//                                releaseGroupImageRepository.getReleaseGroupCoverArtUrlFromNetwork(
//                                    releaseGroupId = event.entityId,
//                                    thumbnail = true,
//                                )
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
