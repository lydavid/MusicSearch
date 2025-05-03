package ly.david.musicsearch.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import app.cash.paging.compose.collectAsLazyPagingItems
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler
import ly.david.musicsearch.ui.common.area.AreasListUiState
import ly.david.musicsearch.ui.common.artist.ArtistsListUiState
import ly.david.musicsearch.ui.common.event.EventsListUiState
import ly.david.musicsearch.ui.common.genre.GenresListUiState
import ly.david.musicsearch.ui.common.instrument.InstrumentsListUiState
import ly.david.musicsearch.ui.common.label.LabelsListUiState
import ly.david.musicsearch.ui.common.list.EntitiesListScreen
import ly.david.musicsearch.ui.common.place.PlacesListUiState
import ly.david.musicsearch.ui.common.recording.RecordingsListUiState
import ly.david.musicsearch.ui.common.release.ReleasesListUiState
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsListUiState
import ly.david.musicsearch.ui.common.series.SeriesListUiState
import ly.david.musicsearch.ui.common.work.WorksListUiState

@OptIn(
    ExperimentalMaterial3Api::class,
)
@Composable
fun EntitiesListUi(
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
    requestForMissingCoverArtUrl: (entityId: String) -> Unit = {},
    onDeleteFromCollection: (entityId: String, name: String) -> Unit = { _, _ -> },
) {
    when (entity) {
        MusicBrainzEntity.AREA -> {
            EntitiesListScreen(
                lazyPagingItems = areasListUiState.pagingDataFlow.collectAsLazyPagingItems(),
                lazyListState = areasListUiState.lazyListState,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                isEditMode = isEditMode,
                onItemClick = onItemClick,
                onDeleteFromCollection = onDeleteFromCollection,
            )
        }

        MusicBrainzEntity.ARTIST -> {
            EntitiesListScreen(
                lazyPagingItems = artistsListUiState.pagingDataFlow.collectAsLazyPagingItems(),
                lazyListState = artistsListUiState.lazyListState,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                isEditMode = isEditMode,
                onItemClick = onItemClick,
                onDeleteFromCollection = onDeleteFromCollection,
            )
        }

        MusicBrainzEntity.EVENT -> {
            EntitiesListScreen(
                lazyPagingItems = eventsListUiState.pagingDataFlow.collectAsLazyPagingItems(),
                lazyListState = eventsListUiState.lazyListState,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                isEditMode = isEditMode,
                onItemClick = onItemClick,
                onDeleteFromCollection = onDeleteFromCollection,
            )
        }

        MusicBrainzEntity.INSTRUMENT -> {
            EntitiesListScreen(
                lazyPagingItems = instrumentsListUiState.lazyPagingItems,
                lazyListState = instrumentsListUiState.lazyListState,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                isEditMode = isEditMode,
                onItemClick = onItemClick,
                onDeleteFromCollection = onDeleteFromCollection,
            )
        }

        MusicBrainzEntity.GENRE -> {
            EntitiesListScreen(
                lazyPagingItems = genresListUiState.lazyPagingItems,
                lazyListState = genresListUiState.lazyListState,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                isEditMode = isEditMode,
                onItemClick = onItemClick,
                onDeleteFromCollection = onDeleteFromCollection,
            )
        }

        MusicBrainzEntity.LABEL -> {
            EntitiesListScreen(
                lazyPagingItems = labelsListUiState.lazyPagingItems,
                lazyListState = labelsListUiState.lazyListState,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                isEditMode = isEditMode,
                onItemClick = onItemClick,
                onDeleteFromCollection = onDeleteFromCollection,
            )
        }

        MusicBrainzEntity.PLACE -> {
            EntitiesListScreen(
                lazyPagingItems = placesListUiState.lazyPagingItems,
                lazyListState = placesListUiState.lazyListState,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                isEditMode = isEditMode,
                onItemClick = onItemClick,
                onDeleteFromCollection = onDeleteFromCollection,
            )
        }

        MusicBrainzEntity.RECORDING -> {
            EntitiesListScreen(
                lazyPagingItems = recordingsListUiState.lazyPagingItems,
                lazyListState = recordingsListUiState.lazyListState,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                isEditMode = isEditMode,
                onItemClick = onItemClick,
                onDeleteFromCollection = onDeleteFromCollection,
            )
        }

        MusicBrainzEntity.RELEASE -> {
            EntitiesListScreen(
                lazyPagingItems = releasesListUiState.lazyPagingItems,
                lazyListState = releasesListUiState.lazyListState,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                isEditMode = isEditMode,
                showMoreInfo = releasesListUiState.showMoreInfo,
                onItemClick = onItemClick,
                onDeleteFromCollection = onDeleteFromCollection,
                requestForMissingCoverArtUrl = { id ->
                    requestForMissingCoverArtUrl(
                        id,
                    )
                },
            )
        }

        MusicBrainzEntity.RELEASE_GROUP -> {
            EntitiesListScreen(
                lazyPagingItems = releaseGroupsListUiState.pagingDataFlow.collectAsLazyPagingItems(),
                lazyListState = releaseGroupsListUiState.lazyListState,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                isEditMode = isEditMode,
                onItemClick = onItemClick,
                onDeleteFromCollection = onDeleteFromCollection,
                requestForMissingCoverArtUrl = { id ->
                    requestForMissingCoverArtUrl(
                        id,
                    )
                },
            )
        }

        MusicBrainzEntity.SERIES -> {
            EntitiesListScreen(
                lazyPagingItems = seriesListUiState.lazyPagingItems,
                lazyListState = seriesListUiState.lazyListState,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                isEditMode = isEditMode,
                onItemClick = onItemClick,
                onDeleteFromCollection = onDeleteFromCollection,
            )
        }

        MusicBrainzEntity.WORK -> {
            EntitiesListScreen(
                lazyPagingItems = worksListUiState.lazyPagingItems,
                lazyListState = worksListUiState.lazyListState,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                isEditMode = isEditMode,
                onItemClick = onItemClick,
                onDeleteFromCollection = onDeleteFromCollection,
            )
        }

        MusicBrainzEntity.COLLECTION,
        MusicBrainzEntity.URL,
        -> {
            // No-op.
        }
    }
}
