package ly.david.musicsearch.share.feature.database.all

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import app.cash.paging.compose.collectAsLazyPagingItems
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.getNamePlural
import ly.david.musicsearch.ui.common.list.EntitiesListUi
import ly.david.musicsearch.ui.common.list.EntitiesListUiState
import ly.david.musicsearch.ui.common.release.ReleasesListUiEvent
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsListUiEvent
import ly.david.musicsearch.ui.common.topappbar.ToggleMenuItem
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import ly.david.musicsearch.ui.core.LocalStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AllEntitiesUi(
    state: AllEntitiesUiState,
    modifier: Modifier = Modifier,
) {
    val eventSink = state.eventSink
    val releasesEventSink = state.releasesListUiState.eventSink
    val releaseGroupsEventSink = state.releaseGroupsListUiState.eventSink
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
                            toggled = state.releaseGroupsListUiState.sort,
                            onToggle = {
                                releaseGroupsEventSink(ReleaseGroupsListUiEvent.UpdateSortReleaseGroupListItem(it))
                            },
                        )
                    }
                    if (state.entity == MusicBrainzEntity.RELEASE) {
                        ToggleMenuItem(
                            toggleOnText = strings.showMoreInfo,
                            toggleOffText = strings.showLessInfo,
                            toggled = state.releasesListUiState.showMoreInfo,
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
        val uiState = when (val entity = state.entity) {
            MusicBrainzEntity.AREA -> {
                EntitiesListUiState(
                    lazyListState = state.areasListUiState.lazyListState,
                    lazyPagingItems = state.areasListUiState.pagingDataFlow.collectAsLazyPagingItems(),
                )
            }

            MusicBrainzEntity.ARTIST -> {
                EntitiesListUiState(
                    lazyListState = state.artistsListUiState.lazyListState,
                    lazyPagingItems = state.artistsListUiState.pagingDataFlow.collectAsLazyPagingItems(),
                )
            }

            MusicBrainzEntity.EVENT -> {
                EntitiesListUiState(
                    lazyListState = state.eventsListUiState.lazyListState,
                    lazyPagingItems = state.eventsListUiState.pagingDataFlow.collectAsLazyPagingItems(),
                )
            }

            MusicBrainzEntity.GENRE -> {
                EntitiesListUiState(
                    lazyListState = state.genresListUiState.lazyListState,
                    lazyPagingItems = state.genresListUiState.lazyPagingItems,
                )
            }

            MusicBrainzEntity.INSTRUMENT -> {
                EntitiesListUiState(
                    lazyListState = state.instrumentsListUiState.lazyListState,
                    lazyPagingItems = state.instrumentsListUiState.lazyPagingItems,
                )
            }

            MusicBrainzEntity.LABEL -> {
                EntitiesListUiState(
                    lazyListState = state.labelsListUiState.lazyListState,
                    lazyPagingItems = state.labelsListUiState.lazyPagingItems,
                )
            }

            MusicBrainzEntity.PLACE -> {
                EntitiesListUiState(
                    lazyListState = state.placesListUiState.lazyListState,
                    lazyPagingItems = state.placesListUiState.lazyPagingItems,
                )
            }

            MusicBrainzEntity.RECORDING -> {
                EntitiesListUiState(
                    lazyListState = state.recordingsListUiState.lazyListState,
                    lazyPagingItems = state.recordingsListUiState.lazyPagingItems,
                )
            }

            MusicBrainzEntity.RELEASE -> {
                EntitiesListUiState(
                    lazyListState = state.releasesListUiState.lazyListState,
                    lazyPagingItems = state.releasesListUiState.lazyPagingItems,
                    showMoreInfo = state.releasesListUiState.showMoreInfo,
                )
            }

            MusicBrainzEntity.RELEASE_GROUP -> {
                EntitiesListUiState(
                    lazyListState = state.releaseGroupsListUiState.lazyListState,
                    lazyPagingItems = state.releaseGroupsListUiState.pagingDataFlow.collectAsLazyPagingItems(),
                )
            }

            MusicBrainzEntity.SERIES -> {
                EntitiesListUiState(
                    lazyListState = state.seriesListUiState.lazyListState,
                    lazyPagingItems = state.seriesListUiState.lazyPagingItems,
                )
            }

            MusicBrainzEntity.WORK -> {
                EntitiesListUiState(
                    lazyListState = state.worksListUiState.lazyListState,
                    lazyPagingItems = state.worksListUiState.lazyPagingItems,
                )
            }

            else -> {
                error("$entity is not supported for collections.")
            }
        }
        EntitiesListUi(
            uiState = uiState,
            innerPadding = innerPadding,
            scrollBehavior = scrollBehavior,
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
                when (state.entity) {
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
