package ly.david.mbjc.ui.event.stats

import androidx.lifecycle.ViewModel
import ly.david.data.domain.relation.RelationRepository
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class EventStatsViewModel(
    private val relationRepository: RelationRepository,
) : ViewModel() {
    suspend fun getNumberOfRelationsByEntity(entityId: String) =
        relationRepository.getNumberOfRelationsByEntity(entityId)

    suspend fun getCountOfEachRelationshipType(entityId: String) =
        relationRepository.getCountOfEachRelationshipType(entityId)
    }
