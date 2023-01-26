package ly.david.mbjc.ui.area.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import ly.david.mbjc.ui.common.Tab
import ly.david.mbjc.ui.stats.StatsScreen

@Composable
internal fun AreaStatsScreen(
    areaId: String,
    tabs: List<Tab>,
    viewModel: AreaStatsViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val stats by remember { viewModel.getStats(areaId, coroutineScope) }.collectAsState()

    StatsScreen(
        tabs = tabs,
        stats = stats
    )
}
