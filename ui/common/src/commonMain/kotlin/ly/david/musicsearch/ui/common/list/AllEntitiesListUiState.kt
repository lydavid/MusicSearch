package ly.david.musicsearch.ui.common.list

import androidx.compose.runtime.Stable
import com.slack.circuit.runtime.CircuitUiState
import ly.david.musicsearch.shared.domain.list.ListFilters
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.ui.common.relation.RelationsUiState
import ly.david.musicsearch.ui.common.topappbar.Tab
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

fun AllEntitiesListUiState.getTotalLocalCount(tab: Tab?): Int {
    return when (tab) {
        Tab.AREAS -> areasListUiState.totalCount
        Tab.ARTISTS -> artistsListUiState.totalCount
        Tab.EVENTS -> eventsListUiState.totalCount
        Tab.GENRES -> genresListUiState.totalCount
        Tab.INSTRUMENTS -> instrumentsListUiState.totalCount
        Tab.LABELS -> labelsListUiState.totalCount
        Tab.PLACES -> placesListUiState.totalCount
        Tab.RECORDINGS -> recordingsListUiState.totalCount
        Tab.RELEASES -> releasesListUiState.totalCount
        Tab.RELEASE_GROUPS -> releaseGroupsListUiState.totalCount
        Tab.SERIES -> seriesListUiState.totalCount
        Tab.WORKS -> worksListUiState.totalCount
        Tab.TRACKS -> tracksByReleaseUiState.trackIds.size
        Tab.DETAILS,
        Tab.RELATIONSHIPS,
        null,
        -> 0
    }
}

fun AllEntitiesListUiState.getListFilters(entity: MusicBrainzEntityType?): ListFilters {
    return when (entity) {
        MusicBrainzEntityType.AREA -> areasListUiState.listFilters
        MusicBrainzEntityType.ARTIST -> artistsListUiState.listFilters
        MusicBrainzEntityType.EVENT -> eventsListUiState.listFilters
        MusicBrainzEntityType.GENRE -> genresListUiState.listFilters
        MusicBrainzEntityType.INSTRUMENT -> instrumentsListUiState.listFilters
        MusicBrainzEntityType.LABEL -> labelsListUiState.listFilters
        MusicBrainzEntityType.PLACE -> placesListUiState.listFilters
        MusicBrainzEntityType.RECORDING -> recordingsListUiState.listFilters
        MusicBrainzEntityType.RELEASE -> releasesListUiState.listFilters
        MusicBrainzEntityType.RELEASE_GROUP -> releaseGroupsListUiState.listFilters
        MusicBrainzEntityType.SERIES -> seriesListUiState.listFilters
        MusicBrainzEntityType.WORK -> worksListUiState.listFilters
        MusicBrainzEntityType.COLLECTION,
        MusicBrainzEntityType.URL,
        null,
        -> ListFilters.Base()
    }
}
