package ly.david.musicsearch.ui.common.releasegroup

import com.slack.circuit.runtime.CircuitUiEvent
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

sealed interface ReleaseGroupsByEntityUiEvent : CircuitUiEvent {
    data class Get(
        val byEntityId: String,
        val byEntity: MusicBrainzEntity,
        val isRemote: Boolean,
    ) : ReleaseGroupsByEntityUiEvent

    data class UpdateQuery(
        val query: String,
    ) : ReleaseGroupsByEntityUiEvent

    data class UpdateSortReleaseGroupListItem(
        val sort: Boolean,
    ) : ReleaseGroupsByEntityUiEvent

    data class RequestForMissingCoverArtUrl(
        val entityId: String,
    ) : ReleaseGroupsByEntityUiEvent
}
