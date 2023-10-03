package ly.david.mbjc.ui.instrument.stats

import androidx.lifecycle.ViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest
import ly.david.musicsearch.domain.relation.GetCountOfEachRelationshipTypeUseCase
import ly.david.ui.stats.Stats
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class InstrumentStatsViewModel(
    private val getCountOfEachRelationshipTypeUseCase: GetCountOfEachRelationshipTypeUseCase,
) : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getStats(entityId: String): Flow<Stats> =
        getCountOfEachRelationshipTypeUseCase(entityId).mapLatest { relationTypeCounts ->
            Stats(
                totalRelations = relationTypeCounts.sumOf { it.count },
                relationTypeCounts = relationTypeCounts.toImmutableList(),
            )
        }
            .distinctUntilChanged()
}
