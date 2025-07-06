package ly.david.musicsearch.share.feature.database.all

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.overlay.LocalOverlayHost
import kotlinx.coroutines.flow.flowOf
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.collection.showAddToCollectionSheet
import ly.david.musicsearch.ui.common.getNamePlural
import ly.david.musicsearch.ui.common.musicbrainz.LoginUiEvent
import ly.david.musicsearch.ui.common.paging.EntitiesLazyPagingItems
import ly.david.musicsearch.ui.common.paging.EntitiesPagingListUi
import ly.david.musicsearch.ui.common.paging.EntitiesPagingListUiState
import ly.david.musicsearch.ui.common.paging.getLoadedIdsForTab
import ly.david.musicsearch.ui.common.release.ReleasesListUiEvent
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsListUiEvent
import ly.david.musicsearch.ui.common.theme.LocalStrings
import ly.david.musicsearch.ui.common.topappbar.AddAllToCollectionMenuItem
import ly.david.musicsearch.ui.common.topappbar.MoreInfoToggleMenuItem
import ly.david.musicsearch.ui.common.topappbar.SortToggleMenuItem
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import ly.david.musicsearch.ui.common.topappbar.toTab

@Suppress("CyclomaticComplexMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AllEntitiesUi(
    state: AllEntitiesUiState,
    modifier: Modifier = Modifier,
) {
    val entity = state.entity
    val eventSink = state.eventSink
    val releasesEventSink = state.entitiesListUiState.releasesListUiState.eventSink
    val releaseGroupsEventSink = state.entitiesListUiState.releaseGroupsListUiState.eventSink
    val loginEventSink = state.loginUiState.eventSink
    val strings = LocalStrings.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val overlayHost = LocalOverlayHost.current

    val areasLazyPagingItems =
        state.entitiesListUiState.areasListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val artistsLazyPagingItems =
        state.entitiesListUiState.artistsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val eventsLazyPagingItems =
        state.entitiesListUiState.eventsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val genresLazyPagingItems =
        state.entitiesListUiState.genresListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val instrumentsLazyPagingItems =
        state.entitiesListUiState.instrumentsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val labelsLazyPagingItems =
        state.entitiesListUiState.labelsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val placesLazyPagingItems =
        state.entitiesListUiState.placesListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val recordingsLazyPagingItems =
        state.entitiesListUiState.recordingsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val releasesLazyPagingItems =
        state.entitiesListUiState.releasesListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val releaseGroupsLazyPagingItems =
        state.entitiesListUiState.releaseGroupsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val seriesLazyPagingItems =
        state.entitiesListUiState.seriesListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val worksLazyPagingItems =
        state.entitiesListUiState.worksListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val unusedLazyPagingItems =
        flowOf(PagingData.empty<ListItemModel>()).collectAsLazyPagingItems()
    val entitiesLazyPagingItems = EntitiesLazyPagingItems(
        areasLazyPagingItems = areasLazyPagingItems,
        artistsLazyPagingItems = artistsLazyPagingItems,
        eventsLazyPagingItems = eventsLazyPagingItems,
        genresLazyPagingItems = genresLazyPagingItems,
        instrumentsLazyPagingItems = instrumentsLazyPagingItems,
        labelsLazyPagingItems = labelsLazyPagingItems,
        placesLazyPagingItems = placesLazyPagingItems,
        recordingsLazyPagingItems = recordingsLazyPagingItems,
        releasesLazyPagingItems = releasesLazyPagingItems,
        releaseGroupsLazyPagingItems = releaseGroupsLazyPagingItems,
        seriesLazyPagingItems = seriesLazyPagingItems,
        worksLazyPagingItems = worksLazyPagingItems,
        relationsLazyPagingItems = unusedLazyPagingItems,
        tracksLazyPagingItems = unusedLazyPagingItems,
    )

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBarWithFilter(
                onBack = {
                    eventSink(AllEntitiesUiEvent.NavigateUp)
                },
                title = entity.getNamePlural(strings),
                scrollBehavior = scrollBehavior,
                overflowDropdownMenuItems = {
                    if (entity == MusicBrainzEntity.RELEASE_GROUP) {
                        SortToggleMenuItem(
                            sorted = state.entitiesListUiState.releaseGroupsListUiState.sort,
                            onToggle = {
                                releaseGroupsEventSink(ReleaseGroupsListUiEvent.UpdateSortReleaseGroupListItem(it))
                            },
                        )
                    }
                    if (entity == MusicBrainzEntity.RELEASE) {
                        MoreInfoToggleMenuItem(
                            showMoreInfo = state.entitiesListUiState.releasesListUiState.showMoreInfo,
                            onToggle = {
                                releasesEventSink(ReleasesListUiEvent.UpdateShowMoreInfoInReleaseListItem(it))
                            },
                        )
                    }
                    AddAllToCollectionMenuItem(
                        tab = entity.toTab(),
                        entityIds = state.selectionState.selectedIds,
                        overlayHost = overlayHost,
                        coroutineScope = coroutineScope,
                        snackbarHostState = snackbarHostState,
                        onLoginClick = {
                            loginEventSink(LoginUiEvent.StartLogin)
                        },
                    )
                },
                topAppBarFilterState = state.topAppBarFilterState,
                selectionState = state.selectionState,
                onSelectAllToggle = {
                    state.selectionState.toggleSelectAll(
                        ids = entitiesLazyPagingItems.getLoadedIdsForTab(
                            tab = entity.toTab(),
                        ),
                    )
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        val uiState = when (entity) {
            MusicBrainzEntity.AREA -> {
                EntitiesPagingListUiState(
                    lazyPagingItems = areasLazyPagingItems,
                    lazyListState = state.entitiesListUiState.areasListUiState.lazyListState,
                )
            }

            MusicBrainzEntity.ARTIST -> {
                EntitiesPagingListUiState(
                    lazyListState = state.entitiesListUiState.artistsListUiState.lazyListState,
                    lazyPagingItems = artistsLazyPagingItems,
                )
            }

            MusicBrainzEntity.EVENT -> {
                EntitiesPagingListUiState(
                    lazyListState = state.entitiesListUiState.eventsListUiState.lazyListState,
                    lazyPagingItems = eventsLazyPagingItems,
                )
            }

            MusicBrainzEntity.GENRE -> {
                EntitiesPagingListUiState(
                    lazyListState = state.entitiesListUiState.genresListUiState.lazyListState,
                    lazyPagingItems = genresLazyPagingItems,
                )
            }

            MusicBrainzEntity.INSTRUMENT -> {
                EntitiesPagingListUiState(
                    lazyListState = state.entitiesListUiState.instrumentsListUiState.lazyListState,
                    lazyPagingItems = instrumentsLazyPagingItems,
                )
            }

            MusicBrainzEntity.LABEL -> {
                EntitiesPagingListUiState(
                    lazyListState = state.entitiesListUiState.labelsListUiState.lazyListState,
                    lazyPagingItems = labelsLazyPagingItems,
                )
            }

            MusicBrainzEntity.PLACE -> {
                EntitiesPagingListUiState(
                    lazyListState = state.entitiesListUiState.placesListUiState.lazyListState,
                    lazyPagingItems = placesLazyPagingItems,
                )
            }

            MusicBrainzEntity.RECORDING -> {
                EntitiesPagingListUiState(
                    lazyListState = state.entitiesListUiState.recordingsListUiState.lazyListState,
                    lazyPagingItems = recordingsLazyPagingItems,
                )
            }

            MusicBrainzEntity.RELEASE -> {
                EntitiesPagingListUiState(
                    lazyListState = state.entitiesListUiState.releasesListUiState.lazyListState,
                    lazyPagingItems = releasesLazyPagingItems,
                    showMoreInfo = state.entitiesListUiState.releasesListUiState.showMoreInfo,
                )
            }

            MusicBrainzEntity.RELEASE_GROUP -> {
                EntitiesPagingListUiState(
                    lazyListState = state.entitiesListUiState.releaseGroupsListUiState.lazyListState,
                    lazyPagingItems = releaseGroupsLazyPagingItems,
                )
            }

            MusicBrainzEntity.SERIES -> {
                EntitiesPagingListUiState(
                    lazyListState = state.entitiesListUiState.seriesListUiState.lazyListState,
                    lazyPagingItems = seriesLazyPagingItems,
                )
            }

            MusicBrainzEntity.WORK -> {
                EntitiesPagingListUiState(
                    lazyListState = state.entitiesListUiState.worksListUiState.lazyListState,
                    lazyPagingItems = worksLazyPagingItems,
                )
            }

            else -> {
                error("$entity is not supported.")
            }
        }
        EntitiesPagingListUi(
            uiState = uiState,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            onItemClick = { entity, id, title ->
                eventSink(
                    AllEntitiesUiEvent.ClickItem(
                        entity = entity,
                        id = id,
                        title = title,
                    ),
                )
            },
            selectedIds = state.selectionState.selectedIds,
            onSelect = {
                state.selectionState.toggleSelection(
                    id = it,
                    totalLoadedCount = entitiesLazyPagingItems.getLoadedIdsForTab(
                        tab = entity.toTab(),
                    ).size,
                )
            },
            onEditCollectionClick = {
                showAddToCollectionSheet(
                    coroutineScope = coroutineScope,
                    overlayHost = overlayHost,
                    entity = entity,
                    entityIds = setOf(it),
                    snackbarHostState = snackbarHostState,
                    onLoginClick = {
                        loginEventSink(LoginUiEvent.StartLogin)
                    },
                )
            },
            requestForMissingCoverArtUrl = { entityId ->
                when (entity) {
                    MusicBrainzEntity.RELEASE -> {
                        releasesEventSink(ReleasesListUiEvent.RequestForMissingCoverArtUrl(entityId))
                    }

                    MusicBrainzEntity.RELEASE_GROUP -> {
                        releaseGroupsEventSink(ReleaseGroupsListUiEvent.RequestForMissingCoverArtUrl(entityId))
                    }

                    else -> {
                        // no-op
                    }
                }
            },
        )
    }
}
