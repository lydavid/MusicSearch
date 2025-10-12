package ly.david.musicsearch.shared.feature.details.artist

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.details.ArtistDetailsModel
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.feature.details.utils.DetailsHorizontalPager
import ly.david.musicsearch.shared.feature.details.utils.DetailsUiEvent
import ly.david.musicsearch.shared.feature.details.utils.DetailsUiState
import ly.david.musicsearch.ui.common.collection.showAddToCollectionSheet
import ly.david.musicsearch.ui.common.icons.CustomIcons
import ly.david.musicsearch.ui.common.icons.Group
import ly.david.musicsearch.ui.common.list.EntitiesListUiEvent
import ly.david.musicsearch.ui.common.locale.getAnnotatedName
import ly.david.musicsearch.ui.common.musicbrainz.MusicBrainzLoginUiEvent
import ly.david.musicsearch.ui.common.paging.EntitiesLazyPagingItems
import ly.david.musicsearch.ui.common.paging.getLazyPagingItemsForTab
import ly.david.musicsearch.ui.common.paging.getLoadedIdsForTab
import ly.david.musicsearch.ui.common.recording.RecordingSortMenuItem
import ly.david.musicsearch.ui.common.screen.ListensScreen
import ly.david.musicsearch.ui.common.screen.StatsScreen
import ly.david.musicsearch.ui.common.theme.LocalStrings
import ly.david.musicsearch.ui.common.topappbar.AddAllToCollectionMenuItem
import ly.david.musicsearch.ui.common.topappbar.AddToCollectionActionToggle
import ly.david.musicsearch.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.musicsearch.ui.common.topappbar.MoreInfoToggleMenuItem
import ly.david.musicsearch.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.musicsearch.ui.common.topappbar.RefreshMenuItem
import ly.david.musicsearch.ui.common.topappbar.SortToggleMenuItem
import ly.david.musicsearch.ui.common.topappbar.StatsMenuItem
import ly.david.musicsearch.ui.common.topappbar.Tab
import ly.david.musicsearch.ui.common.topappbar.TabsBar
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import ly.david.musicsearch.ui.common.topappbar.getTitle
import ly.david.musicsearch.ui.common.topappbar.toMusicBrainzEntityType

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
internal fun ArtistUi(
    state: DetailsUiState<ArtistDetailsModel>,
    modifier: Modifier = Modifier,
) {
    val browseMethod = state.browseMethod
    val entityId = browseMethod.entityId
    val entityType = browseMethod.entityType
    val strings = LocalStrings.current
    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val overlayHost = LocalOverlayHost.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val pagerState: PagerState = rememberPagerState(
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

    val eventSink = state.eventSink
    val loginEventSink = state.musicBrainzLoginUiState.eventSink
    val recordingsByEntityEventSink =
        state.allEntitiesListUiState.recordingsListUiState.eventSink
    val releasesByEntityEventSink =
        state.allEntitiesListUiState.releasesListUiState.eventSink
    val releaseGroupsByEntityEventSink =
        state.allEntitiesListUiState.releaseGroupsListUiState.eventSink

    LaunchedEffect(key1 = pagerState.currentPage) {
        eventSink(DetailsUiEvent.UpdateTab(state.tabs[pagerState.currentPage]))
    }

    state.snackbarMessage?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message = message)
        }
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
                scrollBehavior = scrollBehavior,
                additionalActions = {
                    AddToCollectionActionToggle(
                        collected = state.collected,
                        entityType = entityType,
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
                overflowDropdownMenuItems = {
                    val selectedTab = state.selectedTab

                    RefreshMenuItem(
                        tab = selectedTab,
                        onClick = {
                            when (selectedTab) {
                                Tab.DETAILS -> eventSink(DetailsUiEvent.ForceRefreshDetails)
                                else -> entitiesLazyPagingItems.getLazyPagingItemsForTab(selectedTab)?.refresh()
                            }
                        },
                    )
                    OpenInBrowserMenuItem(
                        url = state.url,
                    )
                    DropdownMenuItem(
                        text = { Text(strings.seeCollaborators) },
                        leadingIcon = {
                            Icon(
                                imageVector = CustomIcons.Group,
                                contentDescription = null,
                            )
                        },
                        onClick = {
                            eventSink(DetailsUiEvent.NavigateToCollaboratorsGraph)
                            closeMenu()
                        },
                    )
                    StatsMenuItem(
                        statsScreen = StatsScreen(
                            browseMethod = state.browseMethod,
                            tabs = state.tabs,
                        ),
                        overlayHost = overlayHost,
                        coroutineScope = coroutineScope,
                    )
                    CopyToClipboardMenuItem(entityId)
                    when (selectedTab) {
                        Tab.RECORDINGS -> {
                            RecordingSortMenuItem(
                                sortOption = state.allEntitiesListUiState.recordingsListUiState.recordingSortOption,
                                onSortOptionClick = {
                                    recordingsByEntityEventSink(
                                        EntitiesListUiEvent.UpdateSortRecordingListItem(it),
                                    )
                                },
                            )
                        }

                        Tab.RELEASE_GROUPS -> {
                            SortToggleMenuItem(
                                sorted = state.allEntitiesListUiState.releaseGroupsListUiState.sort,
                                onToggle = {
                                    releaseGroupsByEntityEventSink(
                                        EntitiesListUiEvent.UpdateSortReleaseGroupListItem(it),
                                    )
                                },
                            )
                        }

                        Tab.RELEASES -> {
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

                        else -> {
                            // Nothing
                        }
                    }
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
            entitiesLazyPagingItems = entitiesLazyPagingItems,
            onEditCollectionClick = {
                showAddToCollectionSheet(
                    coroutineScope = coroutineScope,
                    overlayHost = overlayHost,
                    entityType = state.selectedTab.toMusicBrainzEntityType() ?: return@DetailsHorizontalPager,
                    entityIds = setOf(it),
                    snackbarHostState = snackbarHostState,
                    onLoginClick = {
                        loginEventSink(MusicBrainzLoginUiEvent.StartLogin)
                    },
                )
            },
            requestForMissingCoverArtUrl = { id, entity ->
                when (entity) {
                    MusicBrainzEntityType.RELEASE -> {
                        releasesByEntityEventSink(EntitiesListUiEvent.RequestForMissingCoverArtUrl(id))
                    }

                    MusicBrainzEntityType.RELEASE_GROUP -> {
                        releaseGroupsByEntityEventSink(EntitiesListUiEvent.RequestForMissingCoverArtUrl(id))
                    }

                    else -> {
                        // no-op
                    }
                }
            },
            detailsScreen = { detailsModel ->
                ArtistDetailsTabUi(
                    artist = detailsModel,
                    detailsTabUiState = state.detailsTabUiState,
                    filterText = state.topAppBarFilterState.filterText,
                    onImageClick = {
                        eventSink(DetailsUiEvent.ClickImage)
                    },
                    onCollapseExpandExternalLinks = {
                        eventSink(DetailsUiEvent.ToggleCollapseExpandExternalLinks)
                    },
                    onCollapseExpandAliases = {
                        eventSink(DetailsUiEvent.ToggleCollapseExpandAliases)
                    },
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
                    onItemClick = { entity, id ->
                        eventSink(
                            DetailsUiEvent.ClickItem(
                                entityType = entity,
                                id = id,
                            ),
                        )
                    },
                )
            },
        )
    }
}
