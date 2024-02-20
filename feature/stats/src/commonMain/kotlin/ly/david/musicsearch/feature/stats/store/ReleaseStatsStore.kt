package ly.david.musicsearch.feature.stats.store

import com.arkivanov.mvikotlin.core.store.Store
import ly.david.musicsearch.feature.stats.Stats
import ly.david.musicsearch.feature.stats.store.ReleaseStatsStore.State

internal interface ReleaseStatsStore : Store<Nothing, State, Nothing> {
//    sealed class Intent {
//
//    }

    data class State(
        val stats: Stats = Stats(),
    )

//    sealed class Label {
//
//    }
}

// @KoinViewModel
// internal class ReleaseStatsViewModel(
//    private val getCountOfEachRelationshipTypeUseCase: GetCountOfEachRelationshipTypeUseCase,
// ) : ViewModel() {
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    fun getStats(entityId: String): Flow<Stats> =
//        getCountOfEachRelationshipTypeUseCase(entityId).mapLatest { relationTypeCounts ->
//            Stats(
//                totalRelations = relationTypeCounts.sumOf { it.count },
//                relationTypeCounts = relationTypeCounts.toImmutableList(),
//            )
//        }
//            .distinctUntilChanged()
// }
