package ly.david.musicsearch.feature.stats.circuit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.mapLatest
import ly.david.musicsearch.domain.relation.usecase.GetCountOfEachRelationshipTypeUseCase
import ly.david.musicsearch.feature.stats.Stats



@OptIn(ExperimentalCoroutinesApi::class)
class ReleaseStatsPresenter(
    private val screen: ReleaseStatsScreen, // TODO: how to do without assisted inject?
    private val navigator: Navigator,
    private val getCountOfEachRelationshipTypeUseCase: GetCountOfEachRelationshipTypeUseCase,
) : Presenter<ReleaseStatsScreen.State> {
    @Composable
    override fun present(): ReleaseStatsScreen.State {
        // TODO: need to get entityId passed in
        //  and tabs, just pass through directly to UI
        val stats by getCountOfEachRelationshipTypeUseCase(screen.releaseId).mapLatest { relationTypeCounts ->
            Stats(
                totalRelations = relationTypeCounts.sumOf { it.count },
                relationTypeCounts = relationTypeCounts.toImmutableList(),
            )
        }.collectAsState(Stats())
        return ReleaseStatsScreen.State(
            stats = stats,
            tabs = screen.tabs,
        )
    }
}
