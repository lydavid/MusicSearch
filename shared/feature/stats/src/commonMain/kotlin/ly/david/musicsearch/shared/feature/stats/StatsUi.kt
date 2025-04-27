package ly.david.musicsearch.shared.feature.stats

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableList
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import ly.david.musicsearch.shared.feature.stats.internal.StatsUiState
import ly.david.musicsearch.shared.feature.stats.internal.addEntityStatsSection
import ly.david.musicsearch.shared.feature.stats.internal.addRelationshipsSection
import ly.david.musicsearch.ui.common.topappbar.Tab
import ly.david.musicsearch.ui.common.topappbar.getCachedLocalOfRemoteStringFunction
import ly.david.musicsearch.ui.common.topappbar.getTitle
import ly.david.musicsearch.ui.core.LocalStrings

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
    val strings = LocalStrings.current

    LazyColumn(modifier = modifier) {
        tabs.forEach { tab ->
            when (tab) {
                Tab.RELATIONSHIPS -> {
                    addRelationshipsSection(
                        totalRelations = stats.totalRelations,
                        relationTypeCounts = stats.relationTypeCounts,
                    )
                }

                else -> {
                    stats.tabToStats[tab]?.let { entityStats ->
                        addEntityStatsSection(
                            entityStats = entityStats,
                            header = tab.getTitle(strings),
                            cachedLocalOfRemote = tab.getCachedLocalOfRemoteStringFunction(strings),
                            now = now,
                        )
                    }
                }
            }
        }
    }
}
