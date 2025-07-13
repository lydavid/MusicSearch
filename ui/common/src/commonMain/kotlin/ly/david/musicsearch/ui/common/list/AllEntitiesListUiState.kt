package ly.david.musicsearch.ui.common.list

import androidx.compose.runtime.Stable
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.ui.common.relation.RelationsUiState
import ly.david.musicsearch.ui.common.track.TracksByReleaseUiState

@Stable
data class AllEntitiesListUiState(
    val areasListUiState: EntitiesListUiState = EntitiesListUiState(),
    val artistsListUiState: EntitiesListUiState = EntitiesListUiState(),
    val eventsListUiState: EntitiesListUiState = EntitiesListUiState(),
    val genresListUiState: EntitiesListUiState = EntitiesListUiState(),
    val instrumentsListUiState: EntitiesListUiState = EntitiesListUiState(),
    val labelsListUiState: EntitiesListUiState = EntitiesListUiState(),
    val placesListUiState: EntitiesListUiState = EntitiesListUiState(),
    val recordingsListUiState: EntitiesListUiState = EntitiesListUiState(),
    val releasesListUiState: EntitiesListUiState = EntitiesListUiState(),
    val releaseGroupsListUiState: EntitiesListUiState = EntitiesListUiState(),
    val seriesListUiState: EntitiesListUiState = EntitiesListUiState(),
    val worksListUiState: EntitiesListUiState = EntitiesListUiState(),
    val relationsUiState: RelationsUiState = RelationsUiState(),
    val tracksByReleaseUiState: TracksByReleaseUiState = TracksByReleaseUiState(),
    val eventSink: (AllEntitiesListUiEvent) -> Unit = {},
) : CircuitUiState

fun AllEntitiesListUiState.getTotalLocalCount(entity: MusicBrainzEntity?): Int {
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
