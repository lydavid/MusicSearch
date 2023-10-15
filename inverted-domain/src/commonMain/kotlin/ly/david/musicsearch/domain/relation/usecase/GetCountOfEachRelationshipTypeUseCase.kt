package ly.david.musicsearch.domain.relation.usecase

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.core.models.relation.RelationTypeCount
import ly.david.musicsearch.domain.relation.RelationRepository
import org.koin.core.annotation.Single

@Single
class GetCountOfEachRelationshipTypeUseCase(
    private val relationRepository: RelationRepository,
) {
    operator fun invoke(entityId: String): Flow<List<RelationTypeCount>> {
        return relationRepository.getCountOfEachRelationshipType(entityId)
    }
}
