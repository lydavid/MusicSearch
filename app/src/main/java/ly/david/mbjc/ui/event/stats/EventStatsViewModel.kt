package ly.david.mbjc.ui.event.stats

import androidx.lifecycle.ViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import ly.david.data.domain.relation.RelationRepository
import ly.david.ui.stats.Stats
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class EventStatsViewModel(
    private val relationRepository: RelationRepository,
) : ViewModel() {

    fun getStats(entityId: String): Flow<Stats> = combine(
        relationRepository.getNumberOfRelationsByEntity(entityId),
        relationRepository.getCountOfEachRelationshipType(entityId)
    ) { numberOfRelationsByEntity, countOfEachRelationshipType ->
        Stats(
            totalRelations = numberOfRelationsByEntity,
            relationTypeCounts = countOfEachRelationshipType.toImmutableList(),
        )
    }
        .distinctUntilChanged()
}
