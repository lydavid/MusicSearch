package ly.david.mbjc.ui.event.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import ly.david.data.domain.relation.RelationRepository
import ly.david.data.room.relation.RelationTypeCount
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class EventStatsViewModel(
    private val relationRepository: RelationRepository,
) : ViewModel() {
    fun getNumberOfRelationsByEntity(entityId: String): StateFlow<Int?> =
        relationRepository.getNumberOfRelationsByEntity(entityId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null,
            )

    fun getCountOfEachRelationshipType(entityId: String): StateFlow<List<RelationTypeCount>> =
        relationRepository.getCountOfEachRelationshipType(entityId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = listOf(),
            )
}
