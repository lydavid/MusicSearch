package ly.david.musicsearch.shared.feature.collections.single

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import app.cash.paging.compose.LazyPagingItems
import ly.david.musicsearch.core.models.getNameWithDisambiguation
import ly.david.musicsearch.core.models.listitem.AreaListItemModel
import ly.david.musicsearch.core.models.listitem.InstrumentListItemModel
import ly.david.musicsearch.core.models.listitem.LabelListItemModel
import ly.david.musicsearch.core.models.listitem.ListItemModel
import ly.david.musicsearch.core.models.listitem.ListSeparator
import ly.david.musicsearch.core.models.listitem.PlaceListItemModel
import ly.david.musicsearch.core.models.listitem.RecordingListItemModel
import ly.david.musicsearch.core.models.listitem.SeriesListItemModel
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.ui.core.LocalStrings
import ly.david.ui.common.area.AreaListItem
import ly.david.ui.common.artist.ArtistsByEntityUiState
import ly.david.ui.common.artist.ArtistsListScreen
import ly.david.ui.common.event.EventsByEntityUiState
import ly.david.ui.common.event.EventsListScreen
import ly.david.ui.common.fullscreen.FullScreenText
import ly.david.ui.common.instrument.InstrumentListItem
import ly.david.ui.common.label.LabelListItem
import ly.david.ui.common.listitem.ListSeparatorHeader
import ly.david.ui.common.listitem.SwipeToDeleteListItem
import ly.david.ui.common.paging.ScreenWithPagingLoadingAndError
import ly.david.ui.common.place.PlaceListItem
import ly.david.ui.common.recording.RecordingListItem
import ly.david.ui.common.release.ReleasesByEntityUiEvent
import ly.david.ui.common.release.ReleasesByEntityUiState
import ly.david.ui.common.release.ReleasesListScreen
import ly.david.ui.common.releasegroup.ReleaseGroupsByEntityUiEvent
import ly.david.ui.common.releasegroup.ReleaseGroupsByEntityUiState
import ly.david.ui.common.releasegroup.ReleaseGroupsListScreen
import ly.david.ui.common.series.SeriesListItem
import ly.david.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.ui.common.topappbar.ToggleMenuItem
import ly.david.ui.common.topappbar.TopAppBarWithFilter
import ly.david.ui.common.work.WorksByEntityUiState
import ly.david.ui.common.work.WorksListScreen

/**
 * A single MusicBrainz collection.
 * Displays all items in this collection.
 *
 * User must be authenticated to view non-cached private collections.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CollectionUi(
    state: CollectionUiState,
    modifier: Modifier = Modifier,
) {
    val collection = state.collection
    val eventSink = state.eventSink
    val releasesEventSink = state.releasesByEntityUiState.eventSink
    val releaseGroupsEventSink = state.releaseGroupsByEntityUiState.eventSink
    val strings = LocalStrings.current
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = remember { SnackbarHostState() }

    state.actionableResult?.let { result ->
        LaunchedEffect(result) {
            val snackbarResult = snackbarHostState.showSnackbar(
                message = result.message,
                actionLabel = result.actionLabel,
                duration = SnackbarDuration.Short,
                withDismissAction = true,
            )

            // TODO: support undoing deletion from collection
            when (snackbarResult) {
                SnackbarResult.ActionPerformed -> {
                    // TODO: support login if not logged in
//                    eventSink(CollectionUiEvent.UnMarkItemForDeletion(collectableId))
                }

                SnackbarResult.Dismissed -> {
//                    eventSink(CollectionUiEvent.DeleteItem(
//                        collectableId = collectableId,
//                        name = name
//                    ))
                }
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBarWithFilter(
                onBack = {
                    eventSink(CollectionUiEvent.NavigateUp)
                },
                title = collection?.name.orEmpty(),
                scrollBehavior = scrollBehavior,
                showFilterIcon = true,
                overflowDropdownMenuItems = {
                    if (collection?.isRemote == true) {
                        OpenInBrowserMenuItem(
                            entity = MusicBrainzEntity.COLLECTION,
                            entityId = collection.id,
                        )
                    }
                    CopyToClipboardMenuItem(collection?.id.orEmpty())
                    if (collection?.entity == MusicBrainzEntity.RELEASE_GROUP) {
                        ToggleMenuItem(
                            toggleOnText = strings.sort,
                            toggleOffText = strings.unsort,
                            toggled = state.releaseGroupsByEntityUiState.sort,
                            onToggle = {
                                releaseGroupsEventSink(ReleaseGroupsByEntityUiEvent.UpdateSortReleaseGroupListItem(it))
                            },
                        )
                    }
                    if (collection?.entity == MusicBrainzEntity.RELEASE) {
                        ToggleMenuItem(
                            toggleOnText = strings.showMoreInfo,
                            toggleOffText = strings.showLessInfo,
                            toggled = state.releasesByEntityUiState.showMoreInfo,
                            onToggle = {
                                releasesEventSink(ReleasesByEntityUiEvent.UpdateShowMoreInfoInReleaseListItem(it))
                            },
                        )
                    }
                },
                filterText = state.query,
                onFilterTextChange = {
                    eventSink(CollectionUiEvent.UpdateQuery(it))
                },
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        if (collection == null) {
            FullScreenText(
                text = "Cannot find collection.",
                modifier = Modifier
                    .padding(innerPadding),
            )
        } else {
            CollectionUi(
                lazyPagingItems = state.lazyPagingItems,
                artistsByEntityUiState = state.artistsByEntityUiState,
                eventsByEntityUiState = state.eventsByEntityUiState,
                releasesByEntityUiState = state.releasesByEntityUiState,
                releaseGroupsByEntityUiState = state.releaseGroupsByEntityUiState,
                worksByEntityUiState = state.worksByEntityUiState,
                entity = collection.entity,
                snackbarHostState = snackbarHostState,
                innerPadding = innerPadding,
                scrollBehavior = scrollBehavior,
                onItemClick = { entity, id, title ->
                    eventSink(
                        CollectionUiEvent.ClickItem(
                            entity = entity,
                            id = id,
                            title = title,
                        ),
                    )
                },
                onDeleteFromCollection = { collectableId, name ->
                    eventSink(
                        CollectionUiEvent.MarkItemForDeletion(
                            collectableId = collectableId,
                            name = name,
                        ),
                    )
                },
                requestForMissingCoverArtUrl = { entityId ->
                    when (collection.entity) {
                        MusicBrainzEntity.RELEASE -> {
                            releasesEventSink(ReleasesByEntityUiEvent.RequestForMissingCoverArtUrl(entityId))
                        }

                        MusicBrainzEntity.RELEASE_GROUP -> {
                            releaseGroupsEventSink(ReleaseGroupsByEntityUiEvent.RequestForMissingCoverArtUrl(entityId))
                        }

                        else -> {
                            // no-op
                        }
                    }
                },
            )
        }
    }
}

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
private fun CollectionUi(
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    artistsByEntityUiState: ArtistsByEntityUiState,
    eventsByEntityUiState: EventsByEntityUiState,
    releasesByEntityUiState: ReleasesByEntityUiState,
    releaseGroupsByEntityUiState: ReleaseGroupsByEntityUiState,
    worksByEntityUiState: WorksByEntityUiState,
    entity: MusicBrainzEntity,
    snackbarHostState: SnackbarHostState,
    innerPadding: PaddingValues,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    onItemClick: (entity: MusicBrainzEntity, String, String) -> Unit = { _, _, _ -> },
    onDeleteFromCollection: (entityId: String, name: String) -> Unit = { _, _ -> },
    requestForMissingCoverArtUrl: (entityId: String) -> Unit = {},
) {
    val lazyListState = rememberLazyListState()

    when (entity) {
        MusicBrainzEntity.ARTIST -> {
            ArtistsListScreen(
                lazyListState = lazyListState,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                snackbarHostState = snackbarHostState,
                lazyPagingItems = artistsByEntityUiState.lazyPagingItems,
                onItemClick = onItemClick,
                onDeleteFromCollection = { entityId, name ->
                    onDeleteFromCollection(
                        entityId,
                        name,
                    )
                },
            )
        }

        MusicBrainzEntity.EVENT -> {
            EventsListScreen(
                lazyListState = lazyListState,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                snackbarHostState = snackbarHostState,
                lazyPagingItems = eventsByEntityUiState.lazyPagingItems,
                onEventClick = onItemClick,
                onDeleteFromCollection = { entityId, name ->
                    onDeleteFromCollection(
                        entityId,
                        name,
                    )
                },
            )
        }

        MusicBrainzEntity.RELEASE -> {
            ReleasesListScreen(
                lazyListState = lazyListState,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                snackbarHostState = snackbarHostState,
                lazyPagingItems = releasesByEntityUiState.lazyPagingItems,
                showMoreInfo = releasesByEntityUiState.showMoreInfo,
                onReleaseClick = onItemClick,
                onDeleteFromCollection = { entityId, name ->
                    onDeleteFromCollection(
                        entityId,
                        name,
                    )
                },
                requestForMissingCoverArtUrl = { id ->
                    requestForMissingCoverArtUrl(
                        id,
                    )
                },
            )
        }

        MusicBrainzEntity.RELEASE_GROUP -> {
            ReleaseGroupsListScreen(
                lazyListState = lazyListState,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                snackbarHostState = snackbarHostState,
                lazyPagingItems = releaseGroupsByEntityUiState.lazyPagingItems,
                onReleaseGroupClick = onItemClick,
                onDeleteFromCollection = { entityId, name ->
                    onDeleteFromCollection(
                        entityId,
                        name,
                    )
                },
                requestForMissingCoverArtUrl = { id ->
                    requestForMissingCoverArtUrl(
                        id,
                    )
                },
            )
        }

        MusicBrainzEntity.WORK -> {
            WorksListScreen(
                lazyListState = lazyListState,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                snackbarHostState = snackbarHostState,
                lazyPagingItems = worksByEntityUiState.lazyPagingItems,
                onWorkClick = onItemClick,
                onDeleteFromCollection = { entityId, name ->
                    onDeleteFromCollection(
                        entityId,
                        name,
                    )
                },
            )
        }

        else -> {
            CollectionUi(
                lazyPagingItems = lazyPagingItems,
                lazyListState = lazyListState,
                entity = entity,
                snackbarHostState = snackbarHostState,
                innerPadding = innerPadding,
                scrollBehavior = scrollBehavior,
                modifier = modifier,
                onItemClick = onItemClick,
                onDeleteFromCollection = onDeleteFromCollection,
            )
        }
    }
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
)
@Composable
internal fun CollectionUi(
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    lazyListState: LazyListState,
    entity: MusicBrainzEntity,
    snackbarHostState: SnackbarHostState,
    innerPadding: PaddingValues,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    onItemClick: (entity: MusicBrainzEntity, String, String) -> Unit = { _, _, _ -> },
    onDeleteFromCollection: (entityId: String, name: String) -> Unit = { _, _ -> },
) {
    ScreenWithPagingLoadingAndError(
        modifier = modifier.padding(innerPadding)
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        lazyListState = lazyListState,
        lazyPagingItems = lazyPagingItems,
        snackbarHostState = snackbarHostState,
    ) { listItemModel: ListItemModel? ->
        when (listItemModel) {
            is ListSeparator -> {
                ListSeparatorHeader(text = listItemModel.text)
            }

            is AreaListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        AreaListItem(
                            area = listItemModel,
                            modifier = Modifier.animateItemPlacement(),
                        ) {
                            onItemClick(
                                entity,
                                id,
                                getNameWithDisambiguation(),
                            )
                        }
                    },
                    onDelete = {
                        onDeleteFromCollection(
                            listItemModel.id,
                            listItemModel.name,
                        )
                    },
                )
            }

            is InstrumentListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        InstrumentListItem(
                            instrument = listItemModel,
                            modifier = Modifier.animateItemPlacement(),
                        ) {
                            onItemClick(
                                entity,
                                id,
                                getNameWithDisambiguation(),
                            )
                        }
                    },
                    onDelete = {
                        onDeleteFromCollection(
                            listItemModel.id,
                            listItemModel.name,
                        )
                    },
                )
            }

            is LabelListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        LabelListItem(
                            label = listItemModel,
                            modifier = Modifier.animateItemPlacement(),
                        ) {
                            onItemClick(
                                entity,
                                id,
                                getNameWithDisambiguation(),
                            )
                        }
                    },
                    onDelete = {
                        onDeleteFromCollection(
                            listItemModel.id,
                            listItemModel.name,
                        )
                    },
                )
            }

            is PlaceListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        PlaceListItem(
                            place = listItemModel,
                            modifier = Modifier.animateItemPlacement(),
                        ) {
                            onItemClick(
                                entity,
                                id,
                                getNameWithDisambiguation(),
                            )
                        }
                    },
                    onDelete = {
                        onDeleteFromCollection(
                            listItemModel.id,
                            listItemModel.name,
                        )
                    },
                )
            }

            is RecordingListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        RecordingListItem(
                            recording = listItemModel,
                            modifier = Modifier.animateItemPlacement(),
                        ) {
                            onItemClick(
                                entity,
                                id,
                                getNameWithDisambiguation(),
                            )
                        }
                    },
                    onDelete = {
                        onDeleteFromCollection(
                            listItemModel.id,
                            listItemModel.name,
                        )
                    },
                )
            }

            is SeriesListItemModel -> {
                SwipeToDeleteListItem(
                    content = {
                        SeriesListItem(
                            series = listItemModel,
                            modifier = Modifier.animateItemPlacement(),
                        ) {
                            onItemClick(
                                entity,
                                id,
                                getNameWithDisambiguation(),
                            )
                        }
                    },
                    onDelete = {
                        onDeleteFromCollection(
                            listItemModel.id,
                            listItemModel.name,
                        )
                    },
                )
            }

            else -> {
                // Do nothing.
            }
        }
    }
}
