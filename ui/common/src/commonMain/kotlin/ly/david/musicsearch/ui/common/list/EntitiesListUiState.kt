package ly.david.musicsearch.ui.common.list

import androidx.compose.runtime.Stable
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.area.AreasListUiState
import ly.david.musicsearch.ui.common.artist.ArtistsListUiState
import ly.david.musicsearch.ui.common.event.EventsListUiState
import ly.david.musicsearch.ui.common.genre.GenresListUiState
import ly.david.musicsearch.ui.common.instrument.InstrumentsListUiState
import ly.david.musicsearch.ui.common.label.LabelsListUiState
import ly.david.musicsearch.ui.common.place.PlacesListUiState
import ly.david.musicsearch.ui.common.recording.RecordingsListUiState
import ly.david.musicsearch.ui.common.relation.RelationsUiState
import ly.david.musicsearch.ui.common.release.ReleasesListUiState
import ly.david.musicsearch.ui.common.releasegroup.ReleaseGroupsListUiState
import ly.david.musicsearch.ui.common.series.SeriesListUiState
import ly.david.musicsearch.ui.common.track.TracksByReleaseUiState
import ly.david.musicsearch.ui.common.work.WorksListUiState

@Stable
data class EntitiesListUiState(
    val areasListUiState: AreasListUiState = AreasListUiState(),
    val artistsListUiState: ArtistsListUiState = ArtistsListUiState(),
    val eventsListUiState: EventsListUiState = EventsListUiState(),
    val genresListUiState: GenresListUiState = GenresListUiState(),
    val instrumentsListUiState: InstrumentsListUiState = InstrumentsListUiState(),
    val labelsListUiState: LabelsListUiState = LabelsListUiState(),
    val placesListUiState: PlacesListUiState = PlacesListUiState(),
    val recordingsListUiState: RecordingsListUiState = RecordingsListUiState(),
    val releasesListUiState: ReleasesListUiState = ReleasesListUiState(),
    val releaseGroupsListUiState: ReleaseGroupsListUiState = ReleaseGroupsListUiState(),
    val seriesListUiState: SeriesListUiState = SeriesListUiState(),
    val worksListUiState: WorksListUiState = WorksListUiState(),
    val relationsUiState: RelationsUiState = RelationsUiState(),
    val tracksByReleaseUiState: TracksByReleaseUiState = TracksByReleaseUiState(),
    val eventSink: (EntitiesListUiEvent) -> Unit = {},
) : CircuitUiState

fun EntitiesListUiState.getTotalLocalCount(entity: MusicBrainzEntity?): Int {
    return when (entity) {
        MusicBrainzEntity.AREA -> areasListUiState.count
        MusicBrainzEntity.ARTIST -> artistsListUiState.count
        MusicBrainzEntity.EVENT -> eventsListUiState.count
        MusicBrainzEntity.GENRE -> genresListUiState.count
        MusicBrainzEntity.INSTRUMENT -> instrumentsListUiState.count
        MusicBrainzEntity.LABEL -> labelsListUiState.count
        MusicBrainzEntity.PLACE -> placesListUiState.count
        MusicBrainzEntity.RECORDING -> recordingsListUiState.count
        MusicBrainzEntity.RELEASE -> releasesListUiState.count
        MusicBrainzEntity.RELEASE_GROUP -> releaseGroupsListUiState.count
        MusicBrainzEntity.SERIES -> seriesListUiState.count
        MusicBrainzEntity.WORK -> worksListUiState.count
        MusicBrainzEntity.COLLECTION,
        MusicBrainzEntity.URL,
        null,
        -> 0
    }
}
