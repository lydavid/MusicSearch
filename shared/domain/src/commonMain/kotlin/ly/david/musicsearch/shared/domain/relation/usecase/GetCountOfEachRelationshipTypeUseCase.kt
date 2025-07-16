package ly.david.musicsearch.shared.domain.relation.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import ly.david.musicsearch.shared.domain.relation.RelationTypeCount

interface GetCountOfEachRelationshipTypeUseCase {
    operator fun invoke(browseMethod: BrowseMethod): Flow<List<RelationTypeCount>>
}

class GetCountOfEachRelationshipTypeUseCaseImpl(
    private val relationRepository: RelationRepository,
) : GetCountOfEachRelationshipTypeUseCase {
    override operator fun invoke(browseMethod: BrowseMethod): Flow<List<RelationTypeCount>> {
        return when (browseMethod) {
            is BrowseMethod.ByEntity -> {
                relationRepository.getCountOfEachRelationshipType(browseMethod.entityId)
            }

            BrowseMethod.All -> {
                flowOf(listOf())
            }
        }
    }
}
