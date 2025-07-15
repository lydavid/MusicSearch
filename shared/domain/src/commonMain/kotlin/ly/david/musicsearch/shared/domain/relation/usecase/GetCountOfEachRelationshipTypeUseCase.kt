package ly.david.musicsearch.shared.domain.relation.usecase

import kotlinx.coroutines.flow.Flow
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import ly.david.musicsearch.shared.domain.relation.RelationTypeCount

interface GetCountOfEachRelationshipTypeUseCase {
    operator fun invoke(entityId: String): Flow<List<RelationTypeCount>>
}

class GetCountOfEachRelationshipTypeUseCaseImpl(
    private val relationRepository: RelationRepository,
) : GetCountOfEachRelationshipTypeUseCase {
    override operator fun invoke(entityId: String): Flow<List<RelationTypeCount>> {
        return relationRepository.getCountOfEachRelationshipType(entityId)
    }
}
