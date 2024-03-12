package ly.david.musicsearch.shared.feature.details.recording.stats

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
internal fun RecordingStatsScreen(
    recordingId: String,
    modifier: Modifier = Modifier,
    tabs: ImmutableList<Tab>,
    viewModel: RecordingStatsViewModel = koinViewModel(),
) {
    val stats by viewModel.getStats(entityId = recordingId).collectAsState(Stats())

    StatsUi(
        modifier = modifier,
        tabs = tabs,
        stats = stats,
    )
}
