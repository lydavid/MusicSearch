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
import ly.david.musicsearch.ui.common.list.EntitiesPagingListUi
import ly.david.musicsearch.ui.common.list.EntitiesPagingListUiState
import ly.david.musicsearch.ui.common.paging.EntitiesLazyPagingItems
import ly.david.musicsearch.ui.common.screen.StatsScreen
import ly.david.musicsearch.ui.common.topappbar.Tab
import ly.david.musicsearch.ui.common.topappbar.toMusicBrainzEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun <T : MusicBrainzDetailsModel> DetailsHorizontalPager(
    pagerState: PagerState,
    state: DetailsUiState<T>,
    browseMethod: BrowseMethod.ByEntity,
    entityLazyPagingItems: EntitiesLazyPagingItems,
    innerPadding: PaddingValues,
    scrollBehavior: TopAppBarScrollBehavior,
    now: Instant = Clock.System.now(),
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
                        id = browseMethod.entityId,
                        tabs = state.tabs,
                        isCollection = false,
                    ),
                    modifier = Modifier.fillMaxSize(),
                )
            }

            Tab.TRACKS -> {
                TracksByReleaseUi(
                    uiState = state.entitiesListUiState.tracksByReleaseUiState,
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
                        lazyPagingItems = entityLazyPagingItems.artistsLazyPagingItems,
                        lazyListState = state.entitiesListUiState.artistsListUiState.lazyListState,
                    )

                    Tab.EVENTS -> EntitiesPagingListUiState(
                        lazyPagingItems = entityLazyPagingItems.eventsLazyPagingItems,
                        lazyListState = state.entitiesListUiState.eventsListUiState.lazyListState,
                    )

                    Tab.LABELS -> EntitiesPagingListUiState(
                        lazyPagingItems = entityLazyPagingItems.labelsLazyPagingItems,
                        lazyListState = state.entitiesListUiState.labelsListUiState.lazyListState,
                    )

                    Tab.PLACES -> EntitiesPagingListUiState(
                        lazyPagingItems = entityLazyPagingItems.placesLazyPagingItems,
                        lazyListState = state.entitiesListUiState.placesListUiState.lazyListState,
                    )

                    Tab.RECORDINGS -> EntitiesPagingListUiState(
                        lazyPagingItems = entityLazyPagingItems.recordingsLazyPagingItems,
                        lazyListState = state.entitiesListUiState.recordingsListUiState.lazyListState,
                    )

                    Tab.RELEASES -> EntitiesPagingListUiState(
                        lazyPagingItems = entityLazyPagingItems.releasesLazyPagingItems,
                        lazyListState = state.entitiesListUiState.releasesListUiState.lazyListState,
                        showMoreInfo = state.entitiesListUiState.releasesListUiState.showMoreInfo,
                    )

                    Tab.RELEASE_GROUPS -> EntitiesPagingListUiState(
                        lazyPagingItems = entityLazyPagingItems.releaseGroupsLazyPagingItems,
                        lazyListState = state.entitiesListUiState.releaseGroupsListUiState.lazyListState,
                    )

                    Tab.WORKS -> EntitiesPagingListUiState(
                        lazyPagingItems = entityLazyPagingItems.worksLazyPagingItems,
                        lazyListState = state.entitiesListUiState.worksListUiState.lazyListState,
                    )

                    Tab.RELATIONSHIPS -> EntitiesPagingListUiState(
                        lazyPagingItems = entityLazyPagingItems.relationsLazyPagingItems,
                        lazyListState = state.entitiesListUiState.relationsUiState.lazyListState,
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
                    selectedIds = state.selectedIds,
                    onSelect = {
                        eventSink(
                            DetailsUiEvent.ToggleSelectItem(collectableId = it),
                        )
                    },
                    requestForMissingCoverArtUrl = { entityId ->
                        requestForMissingCoverArtUrl(entityId, tabEntity)
                    },
                )
            }
        }
    }
}
