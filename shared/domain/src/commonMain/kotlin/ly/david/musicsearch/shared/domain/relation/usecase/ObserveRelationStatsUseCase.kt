package ly.david.musicsearch.shared.domain.relation.usecase

import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import ly.david.musicsearch.shared.domain.BrowseMethod
import ly.david.musicsearch.shared.domain.relation.RelationRepository
import ly.david.musicsearch.shared.domain.relation.RelationStats

interface ObserveRelationStatsUseCase {
    operator fun invoke(browseMethod: BrowseMethod): Flow<RelationStats>
}

class ObserveRelationStatsUseCaseImpl(
    private val relationRepository: RelationRepository,
) : ObserveRelationStatsUseCase {
    override operator fun invoke(browseMethod: BrowseMethod): Flow<RelationStats> {
        return when (browseMethod) {
            is BrowseMethod.ByEntity -> {
                combine(
                    relationRepository.observeCountOfEachRelationshipType(browseMethod.entityId),
                    relationRepository.observeLastUpdated(entityId = browseMethod.entityId),
                ) { relationTypeCounts, lastUpdated ->
                    RelationStats(
                        relationTypeCounts = relationTypeCounts.toPersistentList(),
                        lastUpdated = lastUpdated,
                    )
                }
            }

            BrowseMethod.All -> {
                flowOf(RelationStats())
            }
        }
    }
}
