package ly.david.musicsearch.domain.relation

import kotlinx.coroutines.flow.Flow
import ly.david.data.core.relation.RelationTypeCount
import org.koin.core.annotation.Single

@Single
class GetCountOfEachRelationshipTypeUseCase(
    private val relationRepository: RelationRepository,
) {
    operator fun invoke(entityId: String): Flow<List<RelationTypeCount>> {
        return relationRepository.getCountOfEachRelationshipType(entityId)
    }
}
