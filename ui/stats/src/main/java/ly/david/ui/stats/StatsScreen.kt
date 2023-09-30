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
                        totalRemote = stats.totalRemoteEvents,
                        totalLocal = stats.totalLocalEvents,
                        headerRes = R.string.events,
                        cachedLocalOfRemoteRes = R.string.cached_events,
                    )
                }
                Tab.PLACES -> {
                    addEntitiesStatsSection(
                        totalRemote = stats.totalRemotePlaces,
                        totalLocal = stats.totalLocalPlaces,
                        headerRes = R.string.places,
                        cachedLocalOfRemoteRes = R.string.cached_places,
                    )
                }
                Tab.RECORDINGS -> {
                    addEntitiesStatsSection(
                        totalRemote = stats.totalRemoteRecordings,
                        totalLocal = stats.totalLocalRecordings,
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
                        totalRemote = stats.releaseStats.totalRemoteReleases,
                        totalLocal = stats.releaseStats.totalLocalReleases,
                        headerRes = R.string.releases,
                        cachedLocalOfRemoteRes = R.string.cached_releases,
                    )
                }
                Tab.RELEASE_GROUPS -> {
                    addReleaseGroupsSection(
                        totalRemote = stats.releaseGroupStats.totalRemoteReleaseGroups,
                        totalLocal = stats.releaseGroupStats.totalLocalReleaseGroups,
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
                    totalRemoteReleaseGroups = 280,
                    totalLocalReleaseGroups = 279,
                    releaseGroupTypeCounts = persistentListOf(
                        RoomReleaseGroupTypeCount(primaryType = "Album", count = 13),
                        RoomReleaseGroupTypeCount(
                            primaryType = "Album",
                            secondaryTypes = listOf("Compilation", "Demo"),
                            count = 1
                        ),
                    ),
                    totalRelations = 696,
                    relationTypeCounts = persistentListOf(
                        RelationTypeCount(linkedEntity = MusicBrainzEntity.ARTIST, count = 17),
                        RelationTypeCount(linkedEntity = MusicBrainzEntity.RECORDING, count = 397),
                    ),
                    totalRemoteReleases = 20,
                    totalLocalReleases = 15
                )
            )
        }
    }
}
// endregion
