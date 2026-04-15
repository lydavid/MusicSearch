package ly.david.musicsearch.data.repository.relation

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntityType
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import ly.david.musicsearch.shared.domain.relation.usecase.ObserveCountOfRelationshipsByEntity

class ObserveCountOfRelationshipsByEntityImpl(
    private val relationRepository: RelationRepository,
) : ObserveCountOfRelationshipsByEntity {
    override fun invoke(
        entityId: String,
        relatedEntities: Set<MusicBrainzEntityType>,
        query: String,
    ): Flow<Int> {
        return relationRepository.observeCountOfRelationshipsByEntity(
            entityId = entityId,
            relatedEntityTypes = relatedEntities,
            query = query,
        )
    }
}
