package ly.david.musicsearch.feature.stats

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableList
import ly.david.musicsearch.feature.stats.internal.addEntitiesStatsSection
import ly.david.musicsearch.feature.stats.internal.addRelationshipsSection
import ly.david.musicsearch.feature.stats.internal.addReleaseGroupsSection
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.topappbar.Tab

/**
 * Display all [stats] ordered/shown based on [tabs].
 */
@Composable
fun StatsScreen(
    tabs: ImmutableList<Tab>,
    stats: Stats,
    modifier: Modifier = Modifier,
) {
    val strings = LocalStrings.current

    LazyColumn(modifier = modifier) {
        tabs.forEach { tab ->
            when (tab) {
                Tab.EVENTS -> {
                    addEntitiesStatsSection(
                        totalRemote = stats.eventStats.totalRemote,
                        totalLocal = stats.eventStats.totalLocal,
                        header = strings.events,
                        cachedLocalOfRemote = strings.cachedEvents,
                    )
                }

                Tab.PLACES -> {
                    addEntitiesStatsSection(
                        totalRemote = stats.placeStats.totalRemote,
                        totalLocal = stats.placeStats.totalLocal,
                        header = strings.places,
                        cachedLocalOfRemote = strings.cachedPlaces,
                    )
                }

                Tab.RECORDINGS -> {
                    addEntitiesStatsSection(
                        totalRemote = stats.recordingStats.totalRemote,
                        totalLocal = stats.recordingStats.totalLocal,
                        header = strings.recordings,
                        cachedLocalOfRemote = strings.cachedRecordings,
                    )
                }

                Tab.RELATIONSHIPS -> {
                    addRelationshipsSection(
                        totalRelations = stats.totalRelations,
                        relationTypeCounts = stats.relationTypeCounts,
                    )
                }

                Tab.RELEASES -> {
                    addEntitiesStatsSection(
                        totalRemote = stats.releaseStats.totalRemote,
                        totalLocal = stats.releaseStats.totalLocal,
                        header = strings.releases,
                        cachedLocalOfRemote = strings.cachedReleases,
                    )
                }

                Tab.RELEASE_GROUPS -> {
                    addReleaseGroupsSection(
                        totalRemote = stats.releaseGroupStats.totalRemote,
                        totalLocal = stats.releaseGroupStats.totalLocal,
                        releaseGroupTypeCounts = stats.releaseGroupStats.releaseGroupTypeCounts,
                    )
                }

                else -> {
                    // No stats for these tabs yet.
                }
            }
        }
    }
}
