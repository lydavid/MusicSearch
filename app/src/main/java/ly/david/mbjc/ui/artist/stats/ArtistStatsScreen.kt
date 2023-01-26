package ly.david.mbjc.ui.artist.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import ly.david.mbjc.ui.common.Tab
import ly.david.mbjc.ui.stats.StatsScreen

@Composable
internal fun ArtistStatsScreen(
    artistId: String,
    tabs: List<Tab>,
    viewModel: ArtistStatsViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val stats by remember { viewModel.getStats(artistId, coroutineScope) }.collectAsState()

    StatsScreen(
        tabs = tabs,
        stats = stats
    )
}
