package ly.david.musicsearch.shared.feature.details.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.slack.circuit.foundation.CircuitContent
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.details.MusicBrainzDetailsModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.feature.details.release.TracksByReleaseUi
import ly.david.musicsearch.ui.common.fullscreen.DetailsWithErrorHandling
import ly.david.musicsearch.ui.common.paging.EntitiesLazyPagingItems
import ly.david.musicsearch.ui.common.paging.EntitiesPagingListUi
import ly.david.musicsearch.ui.common.paging.EntitiesPagingListUiState
import ly.david.musicsearch.ui.common.paging.getLoadedIdsForTab
import ly.david.musicsearch.ui.common.screen.StatsScreen
import ly.david.musicsearch.ui.common.topappbar.Tab
import ly.david.musicsearch.ui.common.topappbar.toMusicBrainzEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun <T : MusicBrainzDetailsModel> DetailsHorizontalPager(
    pagerState: PagerState,
    state: DetailsUiState<T>,
    browseMethod: BrowseMethod.ByEntity,
    entitiesLazyPagingItems: EntitiesLazyPagingItems,
    innerPadding: PaddingValues,
    scrollBehavior: TopAppBarScrollBehavior,
    now: Instant = Clock.System.now(),
    onEditCollectionClick: (String) -> Unit = {},
    requestForMissingCoverArtUrl: (entityId: String, entity: MusicBrainzEntity?) -> Unit = { _, _ -> },
    detailsScreen: @Composable ((T) -> Unit),
) {
    val eventSink = state.eventSink

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .padding(innerPadding)
            .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { page ->
        val tab = state.tabs[page]

        when (tab) {
            Tab.DETAILS -> {
                DetailsWithErrorHandling(
                    modifier = Modifier.fillMaxSize(),
                    isLoading = state.detailsTabUiState.isLoading,
                    handledException = state.detailsTabUiState.handledException,
                    onRefresh = {
                        eventSink(DetailsUiEvent.ForceRefreshDetails)
                    },
                    detailsModel = state.detailsModel,
                ) { detailsModel ->
                    detailsScreen(detailsModel)
                }
            }

            Tab.STATS -> {
                CircuitContent(
                    screen = StatsScreen(
                        byEntityId = browseMethod.entityId,
                        byEntity = browseMethod.entity,
                        tabs = state.tabs,
                    ),
                    modifier = Modifier.fillMaxSize(),
                )
            }

            Tab.TRACKS -> {
                TracksByReleaseUi(
                    uiState = state.allEntitiesListUiState.tracksByReleaseUiState,
                    onRecordingClick = { id, title ->
                        eventSink(
                            DetailsUiEvent.ClickItem(
                                entity = MusicBrainzEntity.RECORDING,
                                id = id,
                                title = title,
                            ),
                        )
                    },
                )
            }

            else -> {
                val uiState = when (tab) {
                    Tab.ARTISTS -> EntitiesPagingListUiState(
                        lazyPagingItems = entitiesLazyPagingItems.artistsLazyPagingItems,
                        lazyListState = state.allEntitiesListUiState.artistsListUiState.lazyListState,
                    )

                    Tab.EVENTS -> EntitiesPagingListUiState(
                        lazyPagingItems = entitiesLazyPagingItems.eventsLazyPagingItems,
                        lazyListState = state.allEntitiesListUiState.eventsListUiState.lazyListState,
                    )

                    Tab.LABELS -> EntitiesPagingListUiState(
                        lazyPagingItems = entitiesLazyPagingItems.labelsLazyPagingItems,
                        lazyListState = state.allEntitiesListUiState.labelsListUiState.lazyListState,
                    )

                    Tab.PLACES -> EntitiesPagingListUiState(
                        lazyPagingItems = entitiesLazyPagingItems.placesLazyPagingItems,
                        lazyListState = state.allEntitiesListUiState.placesListUiState.lazyListState,
                    )

                    Tab.RECORDINGS -> EntitiesPagingListUiState(
                        lazyPagingItems = entitiesLazyPagingItems.recordingsLazyPagingItems,
                        lazyListState = state.allEntitiesListUiState.recordingsListUiState.lazyListState,
                    )

                    Tab.RELEASES -> EntitiesPagingListUiState(
                        lazyPagingItems = entitiesLazyPagingItems.releasesLazyPagingItems,
                        lazyListState = state.allEntitiesListUiState.releasesListUiState.lazyListState,
                        showMoreInfo = state.allEntitiesListUiState.releasesListUiState.showMoreInfo,
                    )

                    Tab.RELEASE_GROUPS -> EntitiesPagingListUiState(
                        lazyPagingItems = entitiesLazyPagingItems.releaseGroupsLazyPagingItems,
                        lazyListState = state.allEntitiesListUiState.releaseGroupsListUiState.lazyListState,
                    )

                    Tab.WORKS -> EntitiesPagingListUiState(
                        lazyPagingItems = entitiesLazyPagingItems.worksLazyPagingItems,
                        lazyListState = state.allEntitiesListUiState.worksListUiState.lazyListState,
                    )

                    Tab.RELATIONSHIPS -> EntitiesPagingListUiState(
                        lazyPagingItems = entitiesLazyPagingItems.relationsLazyPagingItems,
                        lazyListState = state.allEntitiesListUiState.relationsUiState.lazyListState,
                    )

                    else -> {
                        error("$tab tab should not be accessible for ${browseMethod.entity}.")
                    }
                }
                val tabEntity = tab.toMusicBrainzEntity()
                EntitiesPagingListUi(
                    uiState = uiState,
                    now = now,
                    onItemClick = { entity, id, title ->
                        eventSink(
                            DetailsUiEvent.ClickItem(
                                entity = entity,
                                id = id,
                                title = title,
                            ),
                        )
                    },
                    selectedIds = state.selectionState.selectedIds,
                    onSelect = {
                        eventSink(
                            DetailsUiEvent.ToggleSelectItem(
                                collectableId = it,
                                loadedCount = entitiesLazyPagingItems.getLoadedIdsForTab(
                                    tab = tab,
                                ).size,
                            ),
                        )
                    },
                    onEditCollectionClick = onEditCollectionClick,
                    requestForMissingCoverArtUrl = { entityId ->
                        requestForMissingCoverArtUrl(entityId, tabEntity)
                    },
                )
            }
        }
    }
}
