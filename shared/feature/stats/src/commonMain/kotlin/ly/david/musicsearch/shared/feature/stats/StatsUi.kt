package ly.david.musicsearch.shared.feature.stats

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableList
import ly.david.musicsearch.ui.common.topappbar.Tab
import kotlin.time.Clock
import kotlin.time.Instant

@Composable
internal fun StatsUi(
    state: StatsUiState,
    modifier: Modifier = Modifier,
) {
    StatsUi(
        tabs = state.tabs,
        stats = state.stats,
        modifier = modifier,
    )
}

/**
 * Display all [stats] ordered/shown based on [tabs].
 */
@Composable
internal fun StatsUi(
    tabs: ImmutableList<Tab>,
    stats: Stats,
    modifier: Modifier = Modifier,
    now: Instant = Clock.System.now(),
) {
    LazyColumn(modifier = modifier) {
        tabs.forEach { tab ->
            when (tab) {
                Tab.RELATIONSHIPS -> {
                    addRelationshipsSection(
                        relationStats = stats.relationStats,
                        now = now,
                    )
                }

                else -> {
                    stats.tabToStats[tab]?.let { entityStats ->
                        addEntityStatsSection(
                            entityStats = entityStats,
                            tab = tab,
                            now = now,
                        )
                    }
                }
            }
        }
    }
}
