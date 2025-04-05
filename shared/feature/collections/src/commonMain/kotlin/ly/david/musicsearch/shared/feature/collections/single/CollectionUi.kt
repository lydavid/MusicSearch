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
import ly.david.musicsearch.ui.common.area.AreasListUiState
import ly.david.musicsearch.ui.common.area.AreasListScreen
import ly.david.musicsearch.ui.common.artist.ArtistsListUiState
import ly.david.musicsearch.ui.common.artist.ArtistsListScreen
import ly.david.musicsearch.ui.common.event.EventsListUiState
import ly.david.musicsearch.ui.common.event.EventsListScreen
import ly.david.musicsearch.ui.common.fullscreen.FullScreenText
import ly.david.musicsearch.ui.common.genre.GenresListUiState
import ly.david.musicsearch.ui.common.genre.GenresListScreen
import ly.david.musicsearch.ui.common.instrument.InstrumentsListUiState
import ly.david.musicsearch.ui.common.instrument.InstrumentsListScreen
import ly.david.musicsearch.ui.common.label.LabelsListUiState
import ly.david.musicsearch.ui.common.label.LabelsListScreen
import ly.david.musicsearch.ui.common.place.PlacesListUiState
import ly.david.musicsearch.ui.common.place.PlacesListScreen
import ly.david.musicsearch.ui.common.recording.RecordingsListUiState
import ly.david.musicsearch.ui.common.recording.RecordingsListScreen
import ly.david.musicsearch.ui.common.release.ReleasesListUiEvent
import ly.david.musicsearch.ui.common.release.ReleasesListUiState
import ly.david.musicsearch.ui.common.release.ReleasesListScreen
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsListUiEvent
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsListUiState
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsListScreen
import ly.david.musicsearch.ui.common.series.SeriesListUiState
import ly.david.musicsearch.ui.common.series.SeriesListScreen
import ly.david.musicsearch.ui.common.topappbar.CopyToClipboardMenuItem
import ly.david.musicsearch.ui.common.topappbar.EditToggle
import ly.david.musicsearch.ui.common.topappbar.OpenInBrowserMenuItem
import ly.david.musicsearch.ui.common.topappbar.ToggleMenuItem
import ly.david.musicsearch.ui.common.topappbar.TopAppBarWithFilter
import ly.david.musicsearch.ui.common.work.WorksListUiState
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
    val releasesEventSink = state.releasesListUiState.eventSink
    val releaseGroupsEventSink = state.releaseGroupsListUiState.eventSink
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
                            toggled = state.releaseGroupsListUiState.sort,
                            onToggle = {
                                releaseGroupsEventSink(ReleaseGroupsListUiEvent.UpdateSortReleaseGroupListItem(it))
                            },
                        )
                    }
                    if (collection?.entity == MusicBrainzEntity.RELEASE) {
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
        if (collection == null) {
            FullScreenText(
                text = "Cannot find collection.",
                modifier = Modifier
                    .padding(innerPadding),
            )
        } else {
            CollectionUi(
                isEditMode = state.topAppBarEditState.isEditMode,
                areasListUiState = state.areasListUiState,
                artistsListUiState = state.artistsListUiState,
                eventsListUiState = state.eventsListUiState,
                genresListUiState = state.genresListUiState,
                instrumentsListUiState = state.instrumentsListUiState,
                labelsListUiState = state.labelsListUiState,
                placesListUiState = state.placesListUiState,
                recordingsListUiState = state.recordingsListUiState,
                releasesListUiState = state.releasesListUiState,
                releaseGroupsListUiState = state.releaseGroupsListUiState,
                seriesListUiState = state.seriesListUiState,
                worksListUiState = state.worksListUiState,
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
}

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
private fun CollectionUi(
    isEditMode: Boolean,
    areasListUiState: AreasListUiState,
    artistsListUiState: ArtistsListUiState,
    eventsListUiState: EventsListUiState,
    genresListUiState: GenresListUiState,
    instrumentsListUiState: InstrumentsListUiState,
    labelsListUiState: LabelsListUiState,
    placesListUiState: PlacesListUiState,
    recordingsListUiState: RecordingsListUiState,
    releasesListUiState: ReleasesListUiState,
    releaseGroupsListUiState: ReleaseGroupsListUiState,
    seriesListUiState: SeriesListUiState,
    worksListUiState: WorksListUiState,
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
                lazyListState = areasListUiState.lazyListState,
                pagingDataFlow = areasListUiState.pagingDataFlow,
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
                state = artistsListUiState,
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
                lazyListState = eventsListUiState.lazyListState,
                lazyPagingItems = eventsListUiState.lazyPagingItems,
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
                lazyPagingItems = instrumentsListUiState.lazyPagingItems,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                lazyListState = instrumentsListUiState.lazyListState,
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

        MusicBrainzEntity.GENRE -> {
            GenresListScreen(
                lazyPagingItems = genresListUiState.lazyPagingItems,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                lazyListState = genresListUiState.lazyListState,
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
                lazyPagingItems = labelsListUiState.lazyPagingItems,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                lazyListState = labelsListUiState.lazyListState,
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
                lazyListState = placesListUiState.lazyListState,
                lazyPagingItems = placesListUiState.lazyPagingItems,
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
                lazyPagingItems = recordingsListUiState.lazyPagingItems,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                lazyListState = recordingsListUiState.lazyListState,
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
                lazyPagingItems = releasesListUiState.lazyPagingItems,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                lazyListState = releasesListUiState.lazyListState,
                isEditMode = isEditMode,
                showMoreInfo = releasesListUiState.showMoreInfo,
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
                lazyPagingItems = releaseGroupsListUiState.lazyPagingItems,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                lazyListState = releaseGroupsListUiState.lazyListState,
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
                lazyPagingItems = seriesListUiState.lazyPagingItems,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                lazyListState = seriesListUiState.lazyListState,
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
                lazyPagingItems = worksListUiState.lazyPagingItems,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                lazyListState = worksListUiState.lazyListState,
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
