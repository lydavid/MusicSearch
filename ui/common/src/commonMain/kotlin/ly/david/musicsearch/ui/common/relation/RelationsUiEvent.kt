package ly.david.musicsearch.ui.common.relation

import com.slack.circuit.runtime.CircuitUiEvent
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.network.relatableEntities

sealed interface RelationsUiEvent : CircuitUiEvent {
    data class GetRelations(
        val byEntityId: String,
        val byEntity: MusicBrainzEntityType,
        val relatedEntities: Set<MusicBrainzEntityType> = relatableEntities subtract setOf(MusicBrainzEntityType.URL),
    ) : RelationsUiEvent

    data class UpdateQuery(
        val query: String,
    ) : RelationsUiEvent
}
