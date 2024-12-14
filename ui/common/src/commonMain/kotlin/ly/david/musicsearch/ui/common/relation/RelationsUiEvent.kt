package ly.david.musicsearch.ui.common.relation

import com.slack.circuit.runtime.CircuitUiEvent
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.network.relatableEntities

sealed interface RelationsUiEvent : CircuitUiEvent {
    data class GetRelations(
        val byEntityId: String,
        val byEntity: MusicBrainzEntity,
        val relatedEntities: Set<MusicBrainzEntity> = relatableEntities subtract setOf(MusicBrainzEntity.URL),
    ) : RelationsUiEvent

    data class UpdateQuery(
        val query: String,
    ) : RelationsUiEvent
}
