package ly.david.musicsearch.shared.feature.details.release.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableList
import ly.david.ui.common.topappbar.Tab
import ly.david.musicsearch.feature.stats.Stats
import ly.david.musicsearch.feature.stats.StatsScreen
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun ReleaseStatsScreen(
    releaseId: String,
    modifier: Modifier = Modifier,
    tabs: ImmutableList<Tab>,
    viewModel: ReleaseStatsViewModel = koinViewModel(),
) {
    val stats by viewModel.getStats(entityId = releaseId).collectAsState(Stats())

    StatsScreen(
        modifier = modifier,
        tabs = tabs,
        stats = stats,
    )
}
