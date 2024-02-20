package ly.david.mbjc.ui.release.stats

import androidx.lifecycle.ViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest
import ly.david.musicsearch.domain.relation.usecase.GetCountOfEachRelationshipTypeUseCase
import ly.david.musicsearch.feature.stats.Stats
import org.koin.android.annotation.KoinViewModel

// TODO: simpler to do these first than common-legacy
@KoinViewModel
internal class ReleaseStatsViewModel(
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
