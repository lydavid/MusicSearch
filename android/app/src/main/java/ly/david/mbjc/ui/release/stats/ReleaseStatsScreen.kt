package ly.david.mbjc.ui.release.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.toImmutableList
import ly.david.musicsearch.feature.stats.StatsScreen
import ly.david.musicsearch.feature.stats.circuit.ReleaseStatsScreen

@Composable
internal fun ReleaseStatsUi(
    state: ReleaseStatsScreen.State,
//    releaseId: String,
    modifier: Modifier = Modifier,
//    tabs: ImmutableList<Tab>,
//    viewModel: ReleaseStatsViewModel = koinViewModel(),
) {
//    val stats by viewModel.getStats(entityId = releaseId).collectAsState(Stats())

    StatsScreen(
        modifier = modifier,
        tabs = state.tabs.toImmutableList(),
        stats = state.stats,
    )
}
