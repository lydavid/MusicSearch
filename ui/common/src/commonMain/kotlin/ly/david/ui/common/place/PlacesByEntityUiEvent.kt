package ly.david.ui.common.place

import com.slack.circuit.runtime.CircuitUiEvent
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

sealed interface PlacesByEntityUiEvent : CircuitUiEvent {
    data class GetPlaces(
        val byEntityId: String,
        val byEntity: MusicBrainzEntity,
    ) : PlacesByEntityUiEvent

    data class UpdateQuery(
        val query: String,
    ) : PlacesByEntityUiEvent
}
