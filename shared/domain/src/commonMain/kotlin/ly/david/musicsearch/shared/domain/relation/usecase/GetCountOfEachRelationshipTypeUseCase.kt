package ly.david.musicsearch.shared.domain.relation.usecase

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.core.models.relation.RelationTypeCount
import ly.david.musicsearch.shared.domain.relation.RelationRepository

class GetCountOfEachRelationshipTypeUseCase(
    private val relationRepository: RelationRepository,
) {
    operator fun invoke(entityId: String): Flow<List<RelationTypeCount>> {
        return relationRepository.getCountOfEachRelationshipType(entityId)
    }
}
