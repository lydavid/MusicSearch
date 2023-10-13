package ly.david.ui.stats

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.data.core.relation.RelationTypeCount
import ly.david.musicsearch.data.core.releasegroup.ReleaseGroupTypeCount
import ly.david.musicsearch.strings.LocalStrings
import ly.david.ui.common.topappbar.Tab
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

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

// region Previews
@DefaultPreviews
@Composable
internal fun PreviewStatsScreen() {
    PreviewTheme {
        Surface {
            StatsScreen(
                tabs = persistentListOf(Tab.RELATIONSHIPS, Tab.RELEASE_GROUPS, Tab.RELEASES, Tab.PLACES),
                stats = Stats(
                    totalRelations = 696,
                    relationTypeCounts = persistentListOf(
                        RelationTypeCount(linkedEntity = MusicBrainzEntity.ARTIST, count = 17),
                        RelationTypeCount(linkedEntity = MusicBrainzEntity.RECORDING, count = 397),
                    ),
                    releaseGroupStats = ReleaseGroupStats(
                        totalRemote = 280,
                        totalLocal = 279,
                        releaseGroupTypeCounts = persistentListOf(
                            ReleaseGroupTypeCount(primaryType = "Album", count = 13),
                            ReleaseGroupTypeCount(
                                primaryType = "Album",
                                secondaryTypes = listOf("Compilation", "Demo"),
                                count = 1
                            ),
                        ),
                    ),
                    releaseStats = ReleaseStats(
                        totalRemote = 20,
                        totalLocal = 15
                    ),
                )
            )
        }
    }
}
// endregion
