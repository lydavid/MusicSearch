package ly.david.ui.common.release

import com.slack.circuit.runtime.CircuitUiEvent
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

sealed interface ReleasesByEntityUiEvent : CircuitUiEvent {
    data class GetReleases(
        val byEntityId: String,
        val byEntity: MusicBrainzEntity,
    ) : ReleasesByEntityUiEvent

    data class UpdateQuery(
        val query: String,
    ) : ReleasesByEntityUiEvent

    data class UpdateShowMoreInfoInReleaseListItem(
        val showMore: Boolean,
    ) : ReleasesByEntityUiEvent

    data class RequestForMissingCoverArtUrl(
        val entityId: String,
    ) : ReleasesByEntityUiEvent
}
