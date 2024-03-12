package ly.david.musicsearch.shared.feature.details.event.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableList
import ly.david.ui.common.topappbar.Tab
import ly.david.musicsearch.feature.stats.Stats
import ly.david.musicsearch.feature.stats.StatsUi
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun EventStatsScreen(
    modifier: Modifier = Modifier,
    eventId: String,
    tabs: ImmutableList<Tab>,
    viewModel: EventStatsViewModel = koinViewModel(),
) {
    val stats by viewModel.getStats(entityId = eventId).collectAsState(Stats())

    StatsUi(
        modifier = modifier,
        tabs = tabs,
        stats = stats,
    )
}
