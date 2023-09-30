package ly.david.ui.stats

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.room.relation.RelationTypeCount
import ly.david.data.room.releasegroup.RoomReleaseGroupTypeCount
import ly.david.ui.common.R
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
    LazyColumn(modifier = modifier) {
        tabs.forEach { tab ->
            when (tab) {
                Tab.EVENTS -> {
                    addEntitiesStatsSection(
                        totalRemote = stats.eventStats.totalRemote,
                        totalLocal = stats.eventStats.totalLocal,
                        headerRes = R.string.events,
                        cachedLocalOfRemoteRes = R.string.cached_events,
                    )
                }

                Tab.PLACES -> {
                    addEntitiesStatsSection(
                        totalRemote = stats.placeStats.totalRemote,
                        totalLocal = stats.placeStats.totalLocal,
                        headerRes = R.string.places,
                        cachedLocalOfRemoteRes = R.string.cached_places,
                    )
                }

                Tab.RECORDINGS -> {
                    addEntitiesStatsSection(
                        totalRemote = stats.recordingStats.totalRemote,
                        totalLocal = stats.recordingStats.totalLocal,
                        headerRes = R.string.recordings,
                        cachedLocalOfRemoteRes = R.string.cached_recordings,
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
                        headerRes = R.string.releases,
                        cachedLocalOfRemoteRes = R.string.cached_releases,
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
                            RoomReleaseGroupTypeCount(primaryType = "Album", count = 13),
                            RoomReleaseGroupTypeCount(
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
