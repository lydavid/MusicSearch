package ly.david.musicsearch.ui.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.MusicBrainzItemClickHandler
import ly.david.musicsearch.ui.common.area.AreasListScreen
import ly.david.musicsearch.ui.common.area.AreasListUiState
import ly.david.musicsearch.ui.common.artist.ArtistsListScreen
import ly.david.musicsearch.ui.common.artist.ArtistsListUiState
import ly.david.musicsearch.ui.common.event.EventsListScreen
import ly.david.musicsearch.ui.common.event.EventsListUiState
import ly.david.musicsearch.ui.common.genre.GenresListScreen
import ly.david.musicsearch.ui.common.genre.GenresListUiState
import ly.david.musicsearch.ui.common.instrument.InstrumentsListScreen
import ly.david.musicsearch.ui.common.instrument.InstrumentsListUiState
import ly.david.musicsearch.ui.common.label.LabelsListScreen
import ly.david.musicsearch.ui.common.label.LabelsListUiState
import ly.david.musicsearch.ui.common.place.PlacesListScreen
import ly.david.musicsearch.ui.common.place.PlacesListUiState
import ly.david.musicsearch.ui.common.recording.RecordingsListScreen
import ly.david.musicsearch.ui.common.recording.RecordingsListUiState
import ly.david.musicsearch.ui.common.release.ReleasesListScreen
import ly.david.musicsearch.ui.common.release.ReleasesListUiState
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsListScreen
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsListUiState
import ly.david.musicsearch.ui.common.series.SeriesListScreen
import ly.david.musicsearch.ui.common.series.SeriesListUiState
import ly.david.musicsearch.ui.common.work.WorksListScreen
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
            AreasListScreen(
                state = areasListUiState,
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
            ArtistsListScreen(
                state = artistsListUiState,
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
            EventsListScreen(
                state = eventsListUiState,
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
            InstrumentsListScreen(
                state = instrumentsListUiState,
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
            GenresListScreen(
                state = genresListUiState,
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
            LabelsListScreen(
                state = labelsListUiState,
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
            PlacesListScreen(
                state = placesListUiState,
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
            RecordingsListScreen(
                state = recordingsListUiState,
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
            ReleasesListScreen(
                state = releasesListUiState,
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
            ReleaseGroupsListScreen(
                state = releaseGroupsListUiState,
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
            SeriesListScreen(
                state = seriesListUiState,
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
            WorksListScreen(
                state = worksListUiState,
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
