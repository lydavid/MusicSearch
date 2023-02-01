package ly.david.mbjc.ui.place.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import ly.david.mbjc.ui.common.Tab
import ly.david.mbjc.ui.stats.StatsScreen

@Composable
internal fun PlaceStatsScreen(
    placeId: String,
    modifier: Modifier = Modifier,
    tabs: List<Tab>,
    viewModel: PlaceStatsViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val stats by remember { viewModel.getStats(placeId, coroutineScope) }.collectAsState()

    StatsScreen(
        modifier = modifier,
        tabs = tabs,
        stats = stats
    )
}
