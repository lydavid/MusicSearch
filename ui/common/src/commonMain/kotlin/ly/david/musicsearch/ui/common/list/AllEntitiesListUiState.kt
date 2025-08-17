package ly.david.musicsearch.ui.common.list

import androidx.compose.runtime.Stable
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
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

fun AllEntitiesListUiState.getTotalLocalCount(entity: MusicBrainzEntityType?): Int {
    return when (entity) {
        MusicBrainzEntityType.AREA -> areasListUiState.count
        MusicBrainzEntityType.ARTIST -> artistsListUiState.count
        MusicBrainzEntityType.EVENT -> eventsListUiState.count
        MusicBrainzEntityType.GENRE -> genresListUiState.count
        MusicBrainzEntityType.INSTRUMENT -> instrumentsListUiState.count
        MusicBrainzEntityType.LABEL -> labelsListUiState.count
        MusicBrainzEntityType.PLACE -> placesListUiState.count
        MusicBrainzEntityType.RECORDING -> recordingsListUiState.count
        MusicBrainzEntityType.RELEASE -> releasesListUiState.count
        MusicBrainzEntityType.RELEASE_GROUP -> releaseGroupsListUiState.count
        MusicBrainzEntityType.SERIES -> seriesListUiState.count
        MusicBrainzEntityType.WORK -> worksListUiState.count
        MusicBrainzEntityType.COLLECTION,
        MusicBrainzEntityType.URL,
        null,
        -> 0
    }
}
