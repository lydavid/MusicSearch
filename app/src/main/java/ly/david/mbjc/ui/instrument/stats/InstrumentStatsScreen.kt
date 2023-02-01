package ly.david.mbjc.ui.instrument.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import ly.david.mbjc.ui.common.Tab
import ly.david.mbjc.ui.event.stats.EventStatsViewModel
import ly.david.mbjc.ui.stats.StatsScreen

@Composable
internal fun InstrumentStatsScreen(
    instrumentId: String,
    modifier: Modifier = Modifier,
    tabs: List<Tab>,
    viewModel: EventStatsViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val stats by remember { viewModel.getStats(instrumentId, coroutineScope) }.collectAsState()

    StatsScreen(
        modifier = modifier,
        tabs = tabs,
        stats = stats
    )
}
