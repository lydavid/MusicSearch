package ly.david.musicsearch.shared.feature.collections.single

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
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
import ly.david.musicsearch.shared.domain.getNameWithDisambiguation
import ly.david.musicsearch.shared.domain.listitem.AreaListItemModel
import ly.david.musicsearch.shared.domain.listitem.InstrumentListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListItemModel
import ly.david.musicsearch.shared.domain.listitem.ListSeparator
import ly.david.musicsearch.shared.domain.listitem.PlaceListItemModel
import ly.david.musicsearch.shared.domain.listitem.RecordingListItemModel
import ly.david.musicsearch.shared.domain.listitem.SeriesListItemModel
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.area.AreaListItem
import ly.david.musicsearch.ui.common.artist.ArtistsByEntityUiState
import ly.david.musicsearch.ui.common.artist.ArtistsListScreen
import ly.david.musicsearch.ui.common.event.EventsByEntityUiState
import ly.david.musicsearch.ui.common.event.EventsListScreen
import ly.david.musicsearch.ui.common.fullscreen.FullScreenText
import ly.david.musicsearch.ui.common.instrument.InstrumentListItem
import ly.david.musicsearch.ui.common.label.LabelsByEntityUiState
import ly.david.musicsearch.ui.common.label.LabelsListScreen
import ly.david.musicsearch.ui.common.listitem.ListSeparatorHeader
import ly.david.musicsearch.ui.common.listitem.SwipeToDeleteListItem
import ly.david.musicsearch.ui.common.paging.ScreenWithPagingLoadingAndError
import ly.david.musicsearch.ui.common.place.PlaceListItem
import ly.david.musicsearch.ui.common.recording.RecordingListItem
import ly.david.musicsearch.ui.common.release.ReleasesByEntityUiEvent
import ly.david.musicsearch.ui.common.release.ReleasesByEntityUiState
import ly.david.musicsearch.ui.common.release.ReleasesListScreen
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsByEntityUiEvent
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsByEntityUiState
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsListScreen
import ly.david.musicsearch.ui.common.series.SeriesListItem
import ly.david.musicsearch.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.musicsearch.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.musicsearch.ui.common.topappbar.ToggleMenuItem
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import ly.david.musicsearch.ui.common.work.WorksByEntityUiState
import ly.david.musicsearch.ui.common.work.WorksListScreen
import ly.david.musicsearch.ui.core.LocalStrings

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
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBarWithFilter(
                onBack = {
                    eventSink(CollectionUiEvent.NavigateUp)
                },
                title = state.title,
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
                topAppBarFilterState = state.topAppBarFilterState,
                topAppBarEditState = state.topAppBarEditState,
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
                isEditMode = state.topAppBarEditState.isEditMode,
                lazyPagingItems = state.lazyPagingItems,
                artistsByEntityUiState = state.artistsByEntityUiState,
                eventsByEntityUiState = state.eventsByEntityUiState,
                labelsByEntityUiState = state.labelsByEntityUiState,
                releasesByEntityUiState = state.releasesByEntityUiState,
                releaseGroupsByEntityUiState = state.releaseGroupsByEntityUiState,
                worksByEntityUiState = state.worksByEntityUiState,
                entity = collection.entity,
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
    isEditMode: Boolean,
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    artistsByEntityUiState: ArtistsByEntityUiState,
    eventsByEntityUiState: EventsByEntityUiState,
    labelsByEntityUiState: LabelsByEntityUiState,
    releasesByEntityUiState: ReleasesByEntityUiState,
    releaseGroupsByEntityUiState: ReleaseGroupsByEntityUiState,
    worksByEntityUiState: WorksByEntityUiState,
    entity: MusicBrainzEntity,
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
                lazyListState = artistsByEntityUiState.lazyListState,
                lazyPagingItems = artistsByEntityUiState.lazyPagingItems,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                isEditMode = isEditMode,
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
                lazyListState = eventsByEntityUiState.lazyListState,
                lazyPagingItems = eventsByEntityUiState.lazyPagingItems,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                isEditMode = isEditMode,
                onEventClick = onItemClick,
                onDeleteFromCollection = { entityId, name ->
                    onDeleteFromCollection(
                        entityId,
                        name,
                    )
                },
            )
        }

        MusicBrainzEntity.LABEL -> {
            LabelsListScreen(
                lazyPagingItems = labelsByEntityUiState.lazyPagingItems,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                lazyListState = labelsByEntityUiState.lazyListState,
                isEditMode = isEditMode,
                onLabelClick = onItemClick,
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
                lazyPagingItems = releasesByEntityUiState.lazyPagingItems,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                lazyListState = releasesByEntityUiState.lazyListState,
                isEditMode = isEditMode,
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
                lazyPagingItems = releaseGroupsByEntityUiState.lazyPagingItems,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                lazyListState = releaseGroupsByEntityUiState.lazyListState,
                isEditMode = isEditMode,
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
                lazyPagingItems = worksByEntityUiState.lazyPagingItems,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                lazyListState = worksByEntityUiState.lazyListState,
                isEditMode = isEditMode,
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
                isEditMode = isEditMode,
                lazyPagingItems = lazyPagingItems,
                lazyListState = lazyListState,
                entity = entity,
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
)
@Composable
internal fun CollectionUi(
    isEditMode: Boolean,
    lazyPagingItems: LazyPagingItems<ListItemModel>,
    lazyListState: LazyListState,
    entity: MusicBrainzEntity,
    innerPadding: PaddingValues,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier,
    onItemClick: (entity: MusicBrainzEntity, String, String) -> Unit = { _, _, _ -> },
    onDeleteFromCollection: (entityId: String, name: String) -> Unit = { _, _ -> },
) {
    ScreenWithPagingLoadingAndError(
        lazyPagingItems = lazyPagingItems,
        modifier = modifier.padding(innerPadding)
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        lazyListState = lazyListState,
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
                        ) {
                            onItemClick(
                                entity,
                                id,
                                getNameWithDisambiguation(),
                            )
                        }
                    },
                    disable = !isEditMode,
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
                        ) {
                            onItemClick(
                                entity,
                                id,
                                getNameWithDisambiguation(),
                            )
                        }
                    },
                    disable = !isEditMode,
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
                        ) {
                            onItemClick(
                                entity,
                                id,
                                getNameWithDisambiguation(),
                            )
                        }
                    },
                    disable = !isEditMode,
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
                        ) {
                            onItemClick(
                                entity,
                                id,
                                getNameWithDisambiguation(),
                            )
                        }
                    },
                    disable = !isEditMode,
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
                        ) {
                            onItemClick(
                                entity,
                                id,
                                getNameWithDisambiguation(),
                            )
                        }
                    },
                    disable = !isEditMode,
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
