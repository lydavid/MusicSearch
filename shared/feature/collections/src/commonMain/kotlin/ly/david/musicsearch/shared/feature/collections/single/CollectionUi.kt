package ly.david.musicsearch.shared.feature.collections.single

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
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
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler
import ly.david.musicsearch.ui.common.area.AreasByEntityUiState
import ly.david.musicsearch.ui.common.area.AreasListScreen
import ly.david.musicsearch.ui.common.artist.ArtistsByEntityUiState
import ly.david.musicsearch.ui.common.artist.ArtistsListScreen
import ly.david.musicsearch.ui.common.event.EventsByEntityUiState
import ly.david.musicsearch.ui.common.event.EventsListScreen
import ly.david.musicsearch.ui.common.fullscreen.FullScreenText
import ly.david.musicsearch.ui.common.instrument.InstrumentsByEntityUiState
import ly.david.musicsearch.ui.common.instrument.InstrumentsListScreen
import ly.david.musicsearch.ui.common.label.LabelsByEntityUiState
import ly.david.musicsearch.ui.common.label.LabelsListScreen
import ly.david.musicsearch.ui.common.place.PlacesByEntityUiState
import ly.david.musicsearch.ui.common.place.PlacesListScreen
import ly.david.musicsearch.ui.common.recording.RecordingsByEntityUiState
import ly.david.musicsearch.ui.common.recording.RecordingsListScreen
import ly.david.musicsearch.ui.common.release.ReleasesByEntityUiEvent
import ly.david.musicsearch.ui.common.release.ReleasesByEntityUiState
import ly.david.musicsearch.ui.common.release.ReleasesListScreen
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsByEntityUiEvent
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsByEntityUiState
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsListScreen
import ly.david.musicsearch.ui.common.series.SeriesByEntityUiState
import ly.david.musicsearch.ui.common.series.SeriesListScreen
import ly.david.musicsearch.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.musicsearch.ui.common.topappbar.EditToggle
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
                additionalActions = {
                    IconButton(onClick = {
                        state.topAppBarEditState.toggleEditMode()
                    }) {
                        EditToggle(state.topAppBarEditState)
                    }
                },
                overflowDropdownMenuItems = {
                    if (collection?.isRemote == true) {
                        OpenInBrowserMenuItem(
                            url = state.url,
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
                areasByEntityUiState = state.areasByEntityUiState,
                artistsByEntityUiState = state.artistsByEntityUiState,
                eventsByEntityUiState = state.eventsByEntityUiState,
                instrumentsByEntityUiState = state.instrumentsByEntityUiState,
                labelsByEntityUiState = state.labelsByEntityUiState,
                placesByEntityUiState = state.placesByEntityUiState,
                recordingsByEntityUiState = state.recordingsByEntityUiState,
                releasesByEntityUiState = state.releasesByEntityUiState,
                releaseGroupsByEntityUiState = state.releaseGroupsByEntityUiState,
                seriesByEntityUiState = state.seriesByEntityUiState,
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
    areasByEntityUiState: AreasByEntityUiState,
    artistsByEntityUiState: ArtistsByEntityUiState,
    eventsByEntityUiState: EventsByEntityUiState,
    instrumentsByEntityUiState: InstrumentsByEntityUiState,
    labelsByEntityUiState: LabelsByEntityUiState,
    placesByEntityUiState: PlacesByEntityUiState,
    recordingsByEntityUiState: RecordingsByEntityUiState,
    releasesByEntityUiState: ReleasesByEntityUiState,
    releaseGroupsByEntityUiState: ReleaseGroupsByEntityUiState,
    seriesByEntityUiState: SeriesByEntityUiState,
    worksByEntityUiState: WorksByEntityUiState,
    entity: MusicBrainzEntity,
    innerPadding: PaddingValues,
    scrollBehavior: TopAppBarScrollBehavior,
    onItemClick: MusicBrainzItemClickHandler = { _, _, _ -> },
    onDeleteFromCollection: (entityId: String, name: String) -> Unit = { _, _ -> },
    requestForMissingCoverArtUrl: (entityId: String) -> Unit = {},
) {
    when (entity) {
        MusicBrainzEntity.AREA -> {
            AreasListScreen(
                lazyListState = areasByEntityUiState.lazyListState,
                lazyPagingItems = areasByEntityUiState.lazyPagingItems,
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
                onItemClick = onItemClick,
                onDeleteFromCollection = { entityId, name ->
                    onDeleteFromCollection(
                        entityId,
                        name,
                    )
                },
            )
        }

        MusicBrainzEntity.INSTRUMENT -> {
            InstrumentsListScreen(
                lazyPagingItems = instrumentsByEntityUiState.lazyPagingItems,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                lazyListState = instrumentsByEntityUiState.lazyListState,
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

        MusicBrainzEntity.LABEL -> {
            LabelsListScreen(
                lazyPagingItems = labelsByEntityUiState.lazyPagingItems,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                lazyListState = labelsByEntityUiState.lazyListState,
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

        MusicBrainzEntity.PLACE -> {
            PlacesListScreen(
                lazyListState = placesByEntityUiState.lazyListState,
                lazyPagingItems = placesByEntityUiState.lazyPagingItems,
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

        MusicBrainzEntity.RECORDING -> {
            RecordingsListScreen(
                lazyPagingItems = recordingsByEntityUiState.lazyPagingItems,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                lazyListState = recordingsByEntityUiState.lazyListState,
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
                onItemClick = onItemClick,
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
                onItemClick = onItemClick,
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

        MusicBrainzEntity.SERIES -> {
            SeriesListScreen(
                lazyPagingItems = seriesByEntityUiState.lazyPagingItems,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                lazyListState = seriesByEntityUiState.lazyListState,
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

        MusicBrainzEntity.WORK -> {
            WorksListScreen(
                lazyPagingItems = worksByEntityUiState.lazyPagingItems,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                lazyListState = worksByEntityUiState.lazyListState,
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

        MusicBrainzEntity.COLLECTION,
        MusicBrainzEntity.GENRE,
        MusicBrainzEntity.URL,
        -> {
            // No-op.
        }
    }
}
