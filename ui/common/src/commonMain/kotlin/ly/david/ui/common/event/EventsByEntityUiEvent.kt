package ly.david.ui.common.event

import com.slack.circuit.runtime.CircuitUiEvent
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

sealed interface EventsByEntityUiEvent : CircuitUiEvent {
    data class Get(
        val byEntityId: String,
        val byEntity: MusicBrainzEntity,
    ) : EventsByEntityUiEvent

    data class UpdateQuery(
        val query: String,
    ) : EventsByEntityUiEvent
}
