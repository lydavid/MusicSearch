package ly.david.musicsearch.shared.feature.details.release

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.overlay.LocalOverlayHost
import kotlinx.coroutines.launch
import ly.david.musicsearch.shared.domain.details.ReleaseDetailsModel
import ly.david.musicsearch.shared.domain.listen.SubmitListenType
import ly.david.musicsearch.shared.domain.listen.TrackInfo
import ly.david.musicsearch.shared.domain.musicbrainz.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.feature.details.utils.DetailsHorizontalPager
import ly.david.musicsearch.shared.feature.details.utils.DetailsUiEvent
import ly.david.musicsearch.shared.feature.details.utils.DetailsUiState
import ly.david.musicsearch.ui.common.EntityIcon
import ly.david.musicsearch.ui.common.collection.showEditCollectionSheet
import ly.david.musicsearch.ui.common.list.getListFilters
import ly.david.musicsearch.ui.common.locale.getAnnotatedName
import ly.david.musicsearch.ui.common.musicbrainz.MusicBrainzLoginUiEvent
import ly.david.musicsearch.ui.common.paging.EntitiesLazyPagingItems
import ly.david.musicsearch.ui.common.paging.getLazyPagingItemsForTab
import ly.david.musicsearch.ui.common.paging.getLoadedIdsForTab
import ly.david.musicsearch.ui.common.scaffold.AppScaffold
import ly.david.musicsearch.ui.common.screen.ListensScreen
import ly.david.musicsearch.ui.common.screen.StatsScreen
import ly.david.musicsearch.ui.common.sort.ListFiltersMenuItems
import ly.david.musicsearch.ui.common.topappbar.AddAllToCollectionMenuItem
import ly.david.musicsearch.ui.common.topappbar.AddToCollectionActionToggle
import ly.david.musicsearch.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.musicsearch.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.musicsearch.ui.common.topappbar.RefreshMenuItem
import ly.david.musicsearch.ui.common.topappbar.StatsMenuItem
import ly.david.musicsearch.ui.common.topappbar.SubmitListenMenuItem
import ly.david.musicsearch.ui.common.topappbar.Tab
import ly.david.musicsearch.ui.common.topappbar.TabsBar
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import ly.david.musicsearch.ui.common.topappbar.getTitle
import ly.david.musicsearch.ui.common.topappbar.showSubmitListenDialog
import ly.david.musicsearch.ui.common.topappbar.toMusicBrainzEntityType
import ly.david.musicsearch.ui.common.topappbar.toMusicBrainzEntityTypeWhereTracksAreRecordings

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
internal fun ReleaseUi(
    state: DetailsUiState<ReleaseDetailsModel>,
    modifier: Modifier = Modifier,
) {
    val browseMethod = state.browseMethod
    val entityId = browseMethod.entityId
    val entityType = browseMethod.entityType
    val releaseDetailsModel = state.detailsModel
    val overlayHost = LocalOverlayHost.current
    val snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val selectedTab = state.selectedTab
    val selectionState = state.selectionState
    val pagerState = rememberPagerState(
        initialPage = state.tabs.indexOf(selectedTab),
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
    val entitiesListEventSink = state.allEntitiesListUiState.areasListUiState.eventSink
    val loginEventSink = state.musicBrainzLoginUiState.eventSink

    LaunchedEffect(key1 = pagerState.currentPage) {
        eventSink(DetailsUiEvent.UpdateTab(state.tabs[pagerState.currentPage]))
    }

    state.snackbarMessage?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(
                message = message,
                withDismissAction = true,
            )
        }
    }

    AppScaffold(
        modifier = modifier,
        scrollToHideTopAppBar = state.scrollToHideTopAppBar,
        snackbarHostState = snackbarHostState,
        topBar = { scrollBehavior ->
            val annotatedName = releaseDetailsModel.getAnnotatedName()
            TopAppBarWithFilter(
                onBack = {
                    eventSink(DetailsUiEvent.NavigateUp)
                },
                entity = entityType,
                annotatedString = annotatedName,
                subtitle = state.subtitle,
                scrollBehavior = scrollBehavior,
                topAppBarFilterState = state.topAppBarFilterState,
                selectionState = selectionState,
                onSelectAllToggle = {
                    eventSink(
                        DetailsUiEvent.ToggleSelectAllItems(
                            collectableIds = if (selectedTab == Tab.TRACKS) {
                                state.allEntitiesListUiState.tracksByReleaseUiState.trackIds
                            } else {
                                entitiesLazyPagingItems.getLoadedIdsForTab(
                                    tab = selectedTab,
                                )
                            },
                        ),
                    )
                },
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
                        nameWithDisambiguation = annotatedName.text,
                    )
                },
                overflowDropdownMenuItems = {
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

                    StatsMenuItem(
                        statsScreen = StatsScreen(
                            browseMethod = browseMethod,
                            tabs = state.tabs,
                        ),
                        overlayHost = overlayHost,
                        coroutineScope = coroutineScope,
                    )

                    if (state.showListenSubmission &&
                        selectionState.selectedRecordingIds.isNotEmpty() &&
                        releaseDetailsModel != null
                    ) {
                        SubmitListenMenuItem(
                            submitListenType = SubmitListenType.Album(
                                recordingIds = selectionState.selectedRecordingIds,
                                releaseName = releaseDetailsModel.name,
                                // prefer to get it from details model, as the id can change after a fetch
                                releaseId = releaseDetailsModel.id,
                                releaseArtists = releaseDetailsModel.artistCredits,
                            ),
                            overlayHost = overlayHost,
                            coroutineScope = coroutineScope,
                            snackbarHostState = snackbarHostState,
                            onSuccess = {
                                eventSink(DetailsUiEvent.RefreshLocalDetails)
                            },
                        )
                    }

                    CopyToClipboardMenuItem(entityId)

                    ListFiltersMenuItems(
                        listFilters = state.allEntitiesListUiState.getListFilters(
                            entity = selectedTab.toMusicBrainzEntityType(),
                        ),
                        eventSink = entitiesListEventSink,
                    )

                    AddAllToCollectionMenuItem(
                        tab = selectedTab,
                        entityIds = if (selectedTab == Tab.TRACKS) {
                            selectionState.selectedRecordingIds
                        } else {
                            selectionState.selectedIds
                        },
                        overlayHost = overlayHost,
                        coroutineScope = coroutineScope,
                        snackbarHostState = snackbarHostState,
                        onLoginClick = {
                            loginEventSink(MusicBrainzLoginUiEvent.StartLogin)
                        },
                    )
                },
                subtitleDropdownMenuItems = {
                    releaseDetailsModel?.artistCredits?.forEach { artistCredit ->
                        DropdownMenuItem(
                            text = { Text(artistCredit.name) },
                            leadingIcon = { EntityIcon(entityType = MusicBrainzEntityType.ARTIST) },
                            onClick = {
                                closeMenu()
                                eventSink(
                                    DetailsUiEvent.ClickItem(
                                        entityType = MusicBrainzEntityType.ARTIST,
                                        id = artistCredit.artistId,
                                    ),
                                )
                            },
                        )
                    }
                    releaseDetailsModel?.releaseGroup?.let { releaseGroup ->
                        DropdownMenuItem(
                            text = { Text(text = releaseGroup.name) },
                            leadingIcon = { EntityIcon(entityType = MusicBrainzEntityType.RELEASE_GROUP) },
                            onClick = {
                                closeMenu()
                                eventSink(
                                    DetailsUiEvent.ClickItem(
                                        entityType = MusicBrainzEntityType.RELEASE_GROUP,
                                        id = releaseGroup.id,
                                    ),
                                )
                            },
                        )
                    }
                },
                additionalBar = {
                    TabsBar(
                        tabsTitle = state.tabs.map { it.getTitle() },
                        selectedTabIndex = state.tabs.indexOf(selectedTab),
                        onSelectTabIndex = { coroutineScope.launch { pagerState.animateScrollToPage(it) } },
                    )
                },
            )
        },
    ) { innerPadding, scrollBehavior ->

        DetailsHorizontalPager(
            pagerState = pagerState,
            state = state,
            innerPadding = innerPadding,
            scrollBehavior = scrollBehavior,
            entitiesLazyPagingItems = entitiesLazyPagingItems,
            filterText = state.topAppBarFilterState.filterText,
            onEditCollectionClick = {
                val entityType =
                    selectedTab.toMusicBrainzEntityTypeWhereTracksAreRecordings() ?: return@DetailsHorizontalPager
                showEditCollectionSheet(
                    coroutineScope = coroutineScope,
                    overlayHost = overlayHost,
                    entityType = entityType,
                    entityIds = listOf(it),
                    snackbarHostState = snackbarHostState,
                    onLoginClick = {
                        loginEventSink(MusicBrainzLoginUiEvent.StartLogin)
                    },
                )
            },
            onSubmitListenClick = { track ->
                showSubmitListenDialog(
                    coroutineScope = coroutineScope,
                    overlayHost = overlayHost,
                    submitListenType = SubmitListenType.Track(
                        info = TrackInfo(
                            name = track.name,
                            disambiguation = track.disambiguation,
                            aliases = track.aliases,
                            recordingId = track.recordingId,
                            lengthMilliseconds = track.length?.toLong(),
                            artists = track.artists,
                        ),
                        releaseName = releaseDetailsModel?.name,
                        releaseId = releaseDetailsModel?.id,
                    ),
                    snackbarHostState = snackbarHostState,
                    onSuccess = {
                        // Don't need to do anything because tracks are a flow, so they will be updated.
                    },
                )
            },
            detailsScreen = { detailsModel ->
                ReleaseDetailsTabUi(
                    release = detailsModel,
                    detailsTabUiState = state.detailsTabUiState,
                    filterText = state.topAppBarFilterState.filterText,
                    onImageClick = {
                        eventSink(DetailsUiEvent.ClickImage)
                    },
                    onCollapseExpandReleaseEvents = {
                        eventSink(DetailsUiEvent.ToggleCollapseExpandReleaseEvents)
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
