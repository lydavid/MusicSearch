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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import app.cash.paging.compose.collectAsLazyPagingItems
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.getNamePlural
import ly.david.musicsearch.ui.common.list.EntitiesPagingListUi
import ly.david.musicsearch.ui.common.list.EntitiesPagingListUiState
import ly.david.musicsearch.ui.common.release.ReleasesListUiEvent
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsListUiEvent
import ly.david.musicsearch.ui.common.theme.LocalStrings
import ly.david.musicsearch.ui.common.topappbar.ToggleMenuItem
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter

@Suppress("CyclomaticComplexMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AllEntitiesUi(
    state: AllEntitiesUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink
    val releasesEventSink = state.entitiesListUiState.releasesListUiState.eventSink
    val releaseGroupsEventSink = state.entitiesListUiState.releaseGroupsListUiState.eventSink
    val strings = LocalStrings.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBarWithFilter(
                onBack = {
                    eventSink(AllEntitiesUiEvent.NavigateUp)
                },
                title = state.entity.getNamePlural(strings),
                scrollBehavior = scrollBehavior,
                overflowDropdownMenuItems = {
                    if (state.entity == MusicBrainzEntity.RELEASE_GROUP) {
                        ToggleMenuItem(
                            toggleOnText = strings.sort,
                            toggleOffText = strings.unsort,
                            toggled = state.entitiesListUiState.releaseGroupsListUiState.sort,
                            onToggle = {
                                releaseGroupsEventSink(ReleaseGroupsListUiEvent.UpdateSortReleaseGroupListItem(it))
                            },
                        )
                    }
                    if (state.entity == MusicBrainzEntity.RELEASE) {
                        ToggleMenuItem(
                            toggleOnText = strings.showMoreInfo,
                            toggleOffText = strings.showLessInfo,
                            toggled = state.entitiesListUiState.releasesListUiState.showMoreInfo,
                            onToggle = {
                                releasesEventSink(ReleasesListUiEvent.UpdateShowMoreInfoInReleaseListItem(it))
                            },
                        )
                    }
                },
                topAppBarFilterState = state.topAppBarFilterState,
                topAppBarEditState = state.topAppBarEditState,
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        val entity = state.entity
        val uiState = when (entity) {
            MusicBrainzEntity.AREA -> {
                val lazyPagingItems =
                    state.entitiesListUiState.areasListUiState.pagingDataFlow.collectAsLazyPagingItems()
                EntitiesPagingListUiState(
                    lazyListState = state.entitiesListUiState.areasListUiState.lazyListState,
                    lazyPagingItems = lazyPagingItems,
                )
            }

            MusicBrainzEntity.ARTIST -> {
                val lazyPagingItems =
                    state.entitiesListUiState.artistsListUiState.pagingDataFlow.collectAsLazyPagingItems()
                EntitiesPagingListUiState(
                    lazyListState = state.entitiesListUiState.artistsListUiState.lazyListState,
                    lazyPagingItems = lazyPagingItems,
                )
            }

            MusicBrainzEntity.EVENT -> {
                val lazyPagingItems =
                    state.entitiesListUiState.eventsListUiState.pagingDataFlow.collectAsLazyPagingItems()
                EntitiesPagingListUiState(
                    lazyListState = state.entitiesListUiState.eventsListUiState.lazyListState,
                    lazyPagingItems = lazyPagingItems,
                )
            }

            MusicBrainzEntity.GENRE -> {
                val lazyPagingItems =
                    state.entitiesListUiState.genresListUiState.pagingDataFlow.collectAsLazyPagingItems()
                EntitiesPagingListUiState(
                    lazyListState = state.entitiesListUiState.genresListUiState.lazyListState,
                    lazyPagingItems = lazyPagingItems,
                )
            }

            MusicBrainzEntity.INSTRUMENT -> {
                val lazyPagingItems =
                    state.entitiesListUiState.instrumentsListUiState.pagingDataFlow.collectAsLazyPagingItems()
                EntitiesPagingListUiState(
                    lazyListState = state.entitiesListUiState.instrumentsListUiState.lazyListState,
                    lazyPagingItems = lazyPagingItems,
                )
            }

            MusicBrainzEntity.LABEL -> {
                val lazyPagingItems =
                    state.entitiesListUiState.labelsListUiState.pagingDataFlow.collectAsLazyPagingItems()
                EntitiesPagingListUiState(
                    lazyListState = state.entitiesListUiState.labelsListUiState.lazyListState,
                    lazyPagingItems = lazyPagingItems,
                )
            }

            MusicBrainzEntity.PLACE -> {
                val lazyPagingItems =
                    state.entitiesListUiState.placesListUiState.pagingDataFlow.collectAsLazyPagingItems()
                EntitiesPagingListUiState(
                    lazyListState = state.entitiesListUiState.placesListUiState.lazyListState,
                    lazyPagingItems = lazyPagingItems,
                )
            }

            MusicBrainzEntity.RECORDING -> {
                val lazyPagingItems =
                    state.entitiesListUiState.recordingsListUiState.pagingDataFlow.collectAsLazyPagingItems()
                EntitiesPagingListUiState(
                    lazyListState = state.entitiesListUiState.recordingsListUiState.lazyListState,
                    lazyPagingItems = lazyPagingItems,
                )
            }

            MusicBrainzEntity.RELEASE -> {
                val lazyPagingItems =
                    state.entitiesListUiState.releasesListUiState.pagingDataFlow.collectAsLazyPagingItems()
                EntitiesPagingListUiState(
                    lazyListState = state.entitiesListUiState.releasesListUiState.lazyListState,
                    lazyPagingItems = lazyPagingItems,
                    showMoreInfo = state.entitiesListUiState.releasesListUiState.showMoreInfo,
                )
            }

            MusicBrainzEntity.RELEASE_GROUP -> {
                val lazyPagingItems =
                    state.entitiesListUiState.releaseGroupsListUiState.pagingDataFlow.collectAsLazyPagingItems()
                EntitiesPagingListUiState(
                    lazyListState = state.entitiesListUiState.releaseGroupsListUiState.lazyListState,
                    lazyPagingItems = lazyPagingItems,
                )
            }

            MusicBrainzEntity.SERIES -> {
                val lazyPagingItems =
                    state.entitiesListUiState.seriesListUiState.pagingDataFlow.collectAsLazyPagingItems()
                EntitiesPagingListUiState(
                    lazyListState = state.entitiesListUiState.seriesListUiState.lazyListState,
                    lazyPagingItems = lazyPagingItems,
                )
            }

            MusicBrainzEntity.WORK -> {
                val lazyPagingItems =
                    state.entitiesListUiState.worksListUiState.pagingDataFlow.collectAsLazyPagingItems()
                EntitiesPagingListUiState(
                    lazyListState = state.entitiesListUiState.worksListUiState.lazyListState,
                    lazyPagingItems = lazyPagingItems,
                )
            }

            else -> {
                error("$entity is not supported for collections.")
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
