package ly.david.musicsearch.ui.common.place

import com.slack.circuit.runtime.CircuitUiEvent
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

sealed interface PlacesByEntityUiEvent : CircuitUiEvent {
    data class Get(
        val byEntityId: String,
        val byEntity: MusicBrainzEntity,
    ) : PlacesByEntityUiEvent

    data class UpdateQuery(
        val query: String,
    ) : PlacesByEntityUiEvent
}
