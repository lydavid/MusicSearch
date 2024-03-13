package ly.david.musicsearch.feature.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest
import ly.david.musicsearch.domain.relation.usecase.GetCountOfEachRelationshipTypeUseCase
import ly.david.musicsearch.feature.stats.internal.StatsUiState
import ly.david.ui.common.screen.StatsScreen

internal class ReleaseStatsPresenter(
    private val screen: StatsScreen,
    private val getCountOfEachRelationshipTypeUseCase: GetCountOfEachRelationshipTypeUseCase,
) : Presenter<StatsUiState> {

    @Composable
    override fun present(): StatsUiState {
        val stats = getStats(screen.id).collectAsState(Stats())

        return StatsUiState(
            stats = stats.value,
            tabs = screen.tabs.toImmutableList(),
        )
    }

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
