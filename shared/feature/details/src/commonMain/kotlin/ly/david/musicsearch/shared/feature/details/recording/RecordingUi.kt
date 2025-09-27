package ly.david.musicsearch.shared.feature.details.recording

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import app.cash.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.overlay.LocalOverlayHost
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.details.RecordingDetailsModel
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.feature.details.utils.DetailsHorizontalPager
import ly.david.musicsearch.shared.feature.details.utils.DetailsUiEvent
import ly.david.musicsearch.shared.feature.details.utils.DetailsUiState
import ly.david.musicsearch.shared.strings.AppStrings
import ly.david.musicsearch.ui.common.EntityIcon
import ly.david.musicsearch.ui.common.collection.showAddToCollectionSheet
import ly.david.musicsearch.ui.common.list.EntitiesListUiEvent
import ly.david.musicsearch.ui.common.locale.getAnnotatedName
import ly.david.musicsearch.ui.common.musicbrainz.MusicBrainzLoginUiEvent
import ly.david.musicsearch.ui.common.paging.EntitiesLazyPagingItems
import ly.david.musicsearch.ui.common.paging.getLoadedIdsForTab
import ly.david.musicsearch.ui.common.screen.ListensScreen
import ly.david.musicsearch.ui.common.theme.LocalStrings
import ly.david.musicsearch.ui.common.topappbar.AddAllToCollectionMenuItem
import ly.david.musicsearch.ui.common.topappbar.AddToCollectionActionToggle
import ly.david.musicsearch.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.musicsearch.ui.common.topappbar.MoreInfoToggleMenuItem
import ly.david.musicsearch.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.musicsearch.ui.common.topappbar.OverflowMenuScope
import ly.david.musicsearch.ui.common.topappbar.RefreshMenuItem
import ly.david.musicsearch.ui.common.topappbar.SortToggleMenuItem
import ly.david.musicsearch.ui.common.topappbar.Tab
import ly.david.musicsearch.ui.common.topappbar.TabsBar
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import ly.david.musicsearch.ui.common.topappbar.getTitle
import ly.david.musicsearch.ui.common.topappbar.toMusicBrainzEntity

@Composable
internal fun RecordingUi(
    state: DetailsUiState<RecordingDetailsModel>,
    entityId: String,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current
    val overlayHost = LocalOverlayHost.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val loginEventSink = state.musicBrainzLoginUiState.eventSink

    state.snackbarMessage?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message = message)
        }
    }

    RecordingUiInternal(
        state = state,
        entityId = entityId,
        modifier = modifier,
        snackbarHostState = snackbarHostState,
        strings = strings,
        additionalActions = {
            AddToCollectionActionToggle(
                collected = state.collected,
                entity = MusicBrainzEntityType.AREA,
                entityId = entityId,
                overlayHost = overlayHost,
                coroutineScope = coroutineScope,
                snackbarHostState = snackbarHostState,
                onLoginClick = {
                    loginEventSink(MusicBrainzLoginUiEvent.StartLogin)
                },
                nameWithDisambiguation = state.detailsModel.getAnnotatedName().text,
            )
        },
        additionalOverflowDropdownMenuItems = {
            AddAllToCollectionMenuItem(
                tab = state.selectedTab,
                entityIds = state.selectionState.selectedIds,
                overlayHost = overlayHost,
                coroutineScope = coroutineScope,
                snackbarHostState = snackbarHostState,
                onLoginClick = {
                    loginEventSink(MusicBrainzLoginUiEvent.StartLogin)
                },
            )
        },
        onEditCollectionClick = {
            showAddToCollectionSheet(
                coroutineScope = coroutineScope,
                overlayHost = overlayHost,
                entity = state.selectedTab.toMusicBrainzEntity() ?: return@RecordingUiInternal,
                entityIds = setOf(it),
                snackbarHostState = snackbarHostState,
                onLoginClick = {
                    loginEventSink(MusicBrainzLoginUiEvent.StartLogin)
                },
            )
        },
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
internal fun RecordingUiInternal(
    state: DetailsUiState<RecordingDetailsModel>,
    entityId: String,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    strings: AppStrings = LocalStrings.current,
    additionalActions: @Composable () -> Unit = {},
    additionalOverflowDropdownMenuItems: @Composable (OverflowMenuScope.() -> Unit) = {},
    onEditCollectionClick: (String) -> Unit = {},
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val entityType = MusicBrainzEntityType.RECORDING
    val browseMethod = BrowseMethod.ByEntity(entityId, entityType)
    val eventSink = state.eventSink
    val pagerState = rememberPagerState(
        initialPage = state.tabs.indexOf(state.selectedTab),
        pageCount = state.tabs::size,
    )

    val areasLazyPagingItems =
        state.allEntitiesListUiState.areasListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val artistsLazyPagingItems =
        state.allEntitiesListUiState.artistsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val eventsLazyPagingItems =
        state.allEntitiesListUiState.eventsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val genresLazyPagingItems =
        state.allEntitiesListUiState.genresListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val instrumentsLazyPagingItems =
        state.allEntitiesListUiState.instrumentsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val labelsLazyPagingItems =
        state.allEntitiesListUiState.labelsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val placesLazyPagingItems =
        state.allEntitiesListUiState.placesListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val recordingsLazyPagingItems =
        state.allEntitiesListUiState.recordingsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val releasesLazyPagingItems =
        state.allEntitiesListUiState.releasesListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val releaseGroupsLazyPagingItems =
        state.allEntitiesListUiState.releaseGroupsListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val seriesLazyPagingItems =
        state.allEntitiesListUiState.seriesListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val worksLazyPagingItems =
        state.allEntitiesListUiState.worksListUiState.pagingDataFlow.collectAsLazyPagingItems()
    val relationsLazyPagingItems =
        state.allEntitiesListUiState.relationsUiState.pagingDataFlow.collectAsLazyPagingItems()
    val tracksLazyPagingItems =
        state.allEntitiesListUiState.tracksByReleaseUiState.pagingDataFlow.collectAsLazyPagingItems()
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
        relationsLazyPagingItems = relationsLazyPagingItems,
        tracksLazyPagingItems = tracksLazyPagingItems,
    )

    val releasesByEntityEventSink = state.allEntitiesListUiState.releasesListUiState.eventSink

    LaunchedEffect(key1 = pagerState.currentPage) {
        eventSink(DetailsUiEvent.UpdateTab(state.tabs[pagerState.currentPage]))
    }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                SwipeToDismissBox(
                    state = rememberSwipeToDismissBoxState(),
                    backgroundContent = {},
                    content = { Snackbar(snackbarData) },
                )
            }
        },
        topBar = {
            val annotatedName = state.detailsModel.getAnnotatedName()
            TopAppBarWithFilter(
                onBack = {
                    eventSink(DetailsUiEvent.NavigateUp)
                },
                entity = entityType,
                annotatedString = annotatedName,
                subtitle = state.subtitle,
                scrollBehavior = scrollBehavior,
                additionalActions = additionalActions,
                overflowDropdownMenuItems = {
                    val selectedTab = state.selectedTab
                    RefreshMenuItem(
                        show = selectedTab != Tab.STATS,
                        tab = selectedTab,
                        onClick = {
                            when (selectedTab) {
                                Tab.RELEASES -> releasesLazyPagingItems.refresh()
                                Tab.RELATIONSHIPS -> relationsLazyPagingItems.refresh()
                                else -> eventSink(DetailsUiEvent.ForceRefreshDetails)
                            }
                        },
                    )
                    OpenInBrowserMenuItem(
                        url = state.url,
                    )
                    CopyToClipboardMenuItem(entityId)
                    if (state.selectedTab == Tab.RELEASES) {
                        SortToggleMenuItem(
                            sorted = state.allEntitiesListUiState.releasesListUiState.sort,
                            onToggle = {
                                releasesByEntityEventSink(
                                    EntitiesListUiEvent.UpdateSortReleaseListItem(it),
                                )
                            },
                        )
                        MoreInfoToggleMenuItem(
                            showMoreInfo = state.allEntitiesListUiState.releasesListUiState.showMoreInfo,
                            onToggle = {
                                releasesByEntityEventSink(
                                    EntitiesListUiEvent.UpdateShowMoreInfoInReleaseListItem(it),
                                )
                            },
                        )
                    }

                    additionalOverflowDropdownMenuItems()
                },
                subtitleDropdownMenuItems = {
                    state.detailsModel?.artistCredits?.forEach { artistCredit ->
                        DropdownMenuItem(
                            text = { Text(artistCredit.name) },
                            leadingIcon = { EntityIcon(entity = MusicBrainzEntityType.ARTIST) },
                            onClick = {
                                closeMenu()
                                eventSink(
                                    DetailsUiEvent.ClickItem(
                                        entity = MusicBrainzEntityType.ARTIST,
                                        id = artistCredit.artistId,
                                    ),
                                )
                            },
                        )
                    }
                },
                topAppBarFilterState = state.topAppBarFilterState,
                selectionState = state.selectionState,
                onSelectAllToggle = {
                    eventSink(
                        DetailsUiEvent.ToggleSelectAllItems(
                            collectableIds = entitiesLazyPagingItems.getLoadedIdsForTab(
                                tab = state.selectedTab,
                            ),
                        ),
                    )
                },
                additionalBar = {
                    TabsBar(
                        tabsTitle = state.tabs.map { it.getTitle(strings) },
                        selectedTabIndex = state.tabs.indexOf(state.selectedTab),
                        onSelectTabIndex = { coroutineScope.launch { pagerState.animateScrollToPage(it) } },
                    )
                },
            )
        },
    ) { innerPadding ->

        DetailsHorizontalPager(
            pagerState = pagerState,
            state = state,
            innerPadding = innerPadding,
            scrollBehavior = scrollBehavior,
            browseMethod = browseMethod,
            entitiesLazyPagingItems = entitiesLazyPagingItems,
            onEditCollectionClick = onEditCollectionClick,
            requestForMissingCoverArtUrl = { id, entity ->
                releasesByEntityEventSink(EntitiesListUiEvent.RequestForMissingCoverArtUrl(id))
            },
            detailsScreen = { detailsModel ->
                RecordingDetailsTabUi(
                    recording = detailsModel,
                    detailsTabUiState = state.detailsTabUiState,
                    filterText = state.topAppBarFilterState.filterText,
                    onSeeAllListensClick = {
                        eventSink(
                            DetailsUiEvent.GoToScreen(
                                screen = ListensScreen(
                                    entityFacet = MusicBrainzEntity(
                                        id = entityId,
                                        type = entityType,
                                    ),
                                ),
                            ),
                        )
                    },
                    onCollapseExpandExternalLinks = {
                        eventSink(DetailsUiEvent.ToggleCollapseExpandExternalLinks)
                    },
                    onCollapseExpandAliases = {
                        eventSink(DetailsUiEvent.ToggleCollapseExpandAliases)
                    },
                )
            },
        )
    }
}
