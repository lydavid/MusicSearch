package ly.david.musicsearch.feature.stats.store

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import ly.david.musicsearch.domain.relation.usecase.GetCountOfEachRelationshipTypeUseCase
import ly.david.musicsearch.feature.stats.Stats
import ly.david.musicsearch.feature.stats.store.ReleaseStatsStore.State
import kotlin.coroutines.CoroutineContext

internal class ReleaseStatsStoreFactory(
    private val storeFactory: StoreFactory,
    private val entityId: String,
    private val getCountOfEachRelationshipTypeUseCase: GetCountOfEachRelationshipTypeUseCase,
    private val mainContext: CoroutineContext,
    private val ioContext: CoroutineContext,
) {
    fun create(): ReleaseStatsStore =
        object :
            ReleaseStatsStore,
            Store<Nothing, State, Nothing> by storeFactory.create(
                name = "ReleaseStatsStore",
                initialState = State(),
                bootstrapper = SimpleBootstrapper(Action.Init),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}

    private sealed interface Action {
        data object Init : Action
    }

    private sealed class Msg {
        data class StatsLoaded(val stats: Stats) : Msg()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private inner class ExecutorImpl : CoroutineExecutor<Nothing, Action, State, Msg, Nothing>(mainContext) {
        override fun executeAction(
            action: Action,
            getState: () -> State,
        ) {
            when (action) {
                Action.Init -> init()
            }
        }

        private fun init() {
            scope.launch {
                val stats = getCountOfEachRelationshipTypeUseCase(entityId).mapLatest { relationTypeCounts ->
                    Stats(
                        totalRelations = relationTypeCounts.sumOf { it.count },
                        relationTypeCounts = relationTypeCounts.toImmutableList(),
                    )
                }.last()
                dispatch(Msg.StatsLoaded(stats))
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.StatsLoaded -> copy(stats = msg.stats)
            }
    }
}
