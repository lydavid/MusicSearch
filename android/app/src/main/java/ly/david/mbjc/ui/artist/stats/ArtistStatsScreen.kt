package ly.david.mbjc.ui.artist.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableList
import ly.david.ui.common.topappbar.Tab
import ly.david.ui.stats.Stats
import ly.david.ui.stats.StatsScreen
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun ArtistStatsScreen(
    artistId: String,
    tabs: ImmutableList<Tab>,
    modifier: Modifier = Modifier,
    viewModel: ArtistStatsViewModel = koinViewModel(),
) {
    val stats by viewModel.getStats(entityId = artistId).collectAsState(Stats())

    StatsScreen(
        modifier = modifier,
        tabs = tabs,
        stats = stats,
    )
}
