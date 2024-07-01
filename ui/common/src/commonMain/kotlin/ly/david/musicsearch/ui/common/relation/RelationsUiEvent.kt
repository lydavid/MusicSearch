package ly.david.musicsearch.ui.common.relation

import com.slack.circuit.runtime.CircuitUiEvent
import ly.david.musicsearch.core.models.network.MusicBrainzEntity

sealed interface RelationsUiEvent : CircuitUiEvent {
    data class GetRelations(
        val byEntityId: String,
        val byEntity: MusicBrainzEntity,
    ) : RelationsUiEvent

    data class UpdateQuery(
        val query: String,
    ) : RelationsUiEvent
}
