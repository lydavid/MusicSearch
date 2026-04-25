package ly.david.musicsearch.shared.feature.collections.single

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.slack.circuit.overlay.LocalOverlayHost
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.flowOf
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.error.Feedback
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.SelectableId
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.collection.getMessage
import ly.david.musicsearch.ui.common.collection.showAddToCollectionSheet
import ly.david.musicsearch.ui.common.fullscreen.FullScreenText
import ly.david.musicsearch.ui.common.list.EntitiesListUiEvent
import ly.david.musicsearch.ui.common.list.getListFilters
import ly.david.musicsearch.ui.common.musicbrainz.MusicBrainzLoginUi
import ly.david.musicsearch.ui.common.musicbrainz.MusicBrainzLoginUiEvent
import ly.david.musicsearch.ui.common.paging.EntitiesLazyPagingItems
import ly.david.musicsearch.ui.common.paging.EntitiesPagingListUi
import ly.david.musicsearch.ui.common.paging.getLazyPagingItemsForTab
import ly.david.musicsearch.ui.common.paging.getLoadedIdsForTab
import ly.david.musicsearch.ui.common.paging.toEntitiesPagingListUiState
import ly.david.musicsearch.ui.common.scaffold.AppScaffold
import ly.david.musicsearch.ui.common.screen.StatsScreen
import ly.david.musicsearch.ui.common.snackbar.FeedbackSnackbarVisuals
import ly.david.musicsearch.ui.common.sort.ListFiltersMenuItems
import ly.david.musicsearch.ui.common.topappbar.AddAllToCollectionMenuItem
import ly.david.musicsearch.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.musicsearch.ui.common.topappbar.DeleteMenuItem
import ly.david.musicsearch.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.musicsearch.ui.common.topappbar.RefreshMenuItem
import ly.david.musicsearch.ui.common.topappbar.StatsMenuItem
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import ly.david.musicsearch.ui.common.topappbar.toTab
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Clock
import kotlin.time.Instant

/**
 * A single MusicBrainz collection.
 * Displays all items in this collection.
 *
 * User must be authenticated to view non-cached private collections.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("SwallowedException", "CyclomaticComplexMethod")
@Composable
internal fun CollectionUi(
    state: CollectionUiState,
    modifier: Modifier = Modifier,
    now: Instant = Clock.System.now(),
) {
    val collection = state.collection
    val entity = collection?.entity
    val tab = entity?.toTab()

    val eventSink = state.eventSink
    val loginEventSink = state.musicBrainzLoginUiState.eventSink
    val releasesByEntityEventSink = state.allEntitiesListUiState.releasesListUiState.eventSink
    val releaseGroupsByEntityEventSink = state.allEntitiesListUiState.releaseGroupsListUiState.eventSink

    val overlayHost = LocalOverlayHost.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

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
    val unusedLazyPagingItems =
        flowOf(PagingData.from(listOf<ListItemModel>())).collectAsLazyPagingItems()
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

    MusicBrainzLoginUi(
        state = state.musicBrainzLoginUiState,
        onSuccessfulLogin = {
            entitiesLazyPagingItems.getLazyPagingItemsForTab(tab)?.refresh()
        },
        onError = { message ->
            snackbarHostState.showSnackbar(message = message)
        },
    )

    state.softDeleteFeedback?.let { feedback ->
        LaunchedEffect(feedback) {
            try {
                val snackbarResult = snackbarHostState.showSnackbar(
                    visuals = FeedbackSnackbarVisuals(
                        message = feedback.getMessage(),
                        actionLabel = (feedback as? Feedback.Actionable)?.action?.name,
                        duration = SnackbarDuration.Short,
                        withDismissAction = false,
                        feedback = feedback,
                    ),
                )

                when (snackbarResult) {
                    SnackbarResult.ActionPerformed -> {
                        eventSink(CollectionUiEvent.UnMarkItemsAsDeleted)
                    }

                    SnackbarResult.Dismissed -> {
                        eventSink(CollectionUiEvent.DeleteItemsMarkedAsDeleted)
                    }
                }
            } catch (_: CancellationException) {
                eventSink(CollectionUiEvent.DeleteItemsMarkedAsDeleted)
            }
        }
    }
    state.finalFeedback?.let { feedback ->
        LaunchedEffect(feedback) {
            val snackbarResult = snackbarHostState.showSnackbar(
                visuals = FeedbackSnackbarVisuals(
                    message = feedback.getMessage(),
                    actionLabel = (feedback as? Feedback.Error)?.action?.name,
                    duration = when (feedback) {
                        is Feedback.Loading -> SnackbarDuration.Indefinite
                        is Feedback.Success,
                        is Feedback.Error,
                        is Feedback.Actionable,
                        -> SnackbarDuration.Short
                    },
                    withDismissAction = false,
                    feedback = feedback,
                ),
            )

            when (snackbarResult) {
                SnackbarResult.ActionPerformed -> {
                    loginEventSink(MusicBrainzLoginUiEvent.StartLogin)
                }

                SnackbarResult.Dismissed -> {
                    // no-op
                }
            }
        }
    }

    AppScaffold(
        modifier = modifier,
        scrollToHideTopAppBar = state.scrollToHideTopAppBar,
        snackbarHostState = snackbarHostState,
        topBar = { scrollBehavior ->
            TopAppBarWithFilter(
                onBack = {
                    eventSink(CollectionUiEvent.NavigateUp)
                },
                title = state.title,
                scrollBehavior = scrollBehavior,
                overflowDropdownMenuItems = {
                    tab?.let { tab ->
                        RefreshMenuItem(
                            tab = tab,
                            onClick = {
                                entitiesLazyPagingItems.getLazyPagingItemsForTab(tab)?.refresh()
                            },
                        )
                    }
                    StatsMenuItem(
                        statsScreen = StatsScreen(
                            browseMethod = BrowseMethod.ByEntity(
                                entityId = collection?.id.orEmpty(),
                                entityType = MusicBrainzEntityType.COLLECTION,
                            ),
                            tabs = listOfNotNull(tab).toPersistentList(),
                            isRemote = collection?.isRemote == true,
                        ),
                        overlayHost = overlayHost,
                        coroutineScope = coroutineScope,
                    )
                    if (collection?.isRemote == true) {
                        OpenInBrowserMenuItem(
                            url = state.url,
                        )
                    }
                    CopyToClipboardMenuItem(collection?.id.orEmpty())
                    ListFiltersMenuItems(
                        listFilters = state.allEntitiesListUiState.getListFilters(
                            entity = entity,
                        ),
                        eventSink = releasesByEntityEventSink,
                    )
                    AddAllToCollectionMenuItem(
                        tab = tab,
                        entityIds = state.selectionState.selectedIds,
                        overlayHost = overlayHost,
                        coroutineScope = coroutineScope,
                        snackbarHostState = snackbarHostState,
                        onLoginClick = {
                            loginEventSink(MusicBrainzLoginUiEvent.StartLogin)
                        },
                    )
                    if (state.selectionState.selectedIds.isNotEmpty()) {
                        DeleteMenuItem(
                            selectionState = state.selectionState,
                            onClick = {
                                eventSink(CollectionUiEvent.MarkSelectedItemsAsDeleted)
                            },
                        )
                    }
                },
                topAppBarFilterState = state.topAppBarFilterState,
                selectionState = state.selectionState,
                onSelectAllToggle = {
                    state.selectionState.toggleSelectAll(
                        items = entitiesLazyPagingItems.getLoadedIdsForTab(
                            tab = tab,
                        ),
                    )
                },
            )
        },
    ) { innerPadding, scrollBehavior ->
        if (collection == null) {
            FullScreenText(
                text = "Cannot find collection.",
                modifier = Modifier
                    .padding(innerPadding),
            )
        } else {
            val entity = collection.entity
            val uiState = state.allEntitiesListUiState.toEntitiesPagingListUiState(
                tab = entity.toTab(),
                entitiesLazyPagingItems = entitiesLazyPagingItems,
            )
            EntitiesPagingListUi(
                uiState = uiState,
                filterText = state.topAppBarFilterState.filterText,
                now = now,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                onItemClick = { entity, id ->
                    eventSink(
                        CollectionUiEvent.ClickItem(
                            entityType = entity,
                            id = id,
                        ),
                    )
                },
                selectedIds = state.selectionState.selectedIds,
                onSelect = {
                    state.selectionState.toggleSelection(
                        item = SelectableId(id = it),
                        totalLoadedCount = entitiesLazyPagingItems.getLoadedIdsForTab(
                            tab = entity.toTab(),
                        ).size,
                    )
                },
                onEditCollectionClick = {
                    showAddToCollectionSheet(
                        coroutineScope = coroutineScope,
                        overlayHost = overlayHost,
                        entityType = entity,
                        entityIds = listOf(it),
                        snackbarHostState = snackbarHostState,
                        onLoginClick = {
                            loginEventSink(MusicBrainzLoginUiEvent.StartLogin)
                        },
                    )
                },
                requestForMissingCoverArtUrl = { entityId ->
                    when (entity) {
                        MusicBrainzEntityType.RELEASE -> {
                            releasesByEntityEventSink(EntitiesListUiEvent.RequestForMissingCoverArtUrl(entityId))
                        }

                        MusicBrainzEntityType.RELEASE_GROUP -> {
                            releaseGroupsByEntityEventSink(EntitiesListUiEvent.RequestForMissingCoverArtUrl(entityId))
                        }

                        else -> {
                            // no-op
                        }
                    }
                },
                onLogin = {
                    loginEventSink(MusicBrainzLoginUiEvent.StartLogin)
                },
            )
        }
    }
}
