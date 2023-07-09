package ly.david.mbjc.ui.stats

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ly.david.data.network.MusicBrainzEntity
import ly.david.data.room.relation.RelationTypeCount
import ly.david.data.room.releasegroup.ReleaseGroupTypeCount
import ly.david.ui.common.R
import ly.david.ui.common.topappbar.Tab
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

/**
 * Display all [stats] ordered/shown based on [tabs].
 */
@Composable
internal fun StatsScreen(
    tabs: List<Tab>,
    stats: Stats,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        tabs.forEach { tab ->
            when (tab) {
                Tab.EVENTS -> {
                    addEntitiesStatsSection(
                        totalRemote = stats.totalRemoteEvents,
                        totalLocal = stats.totalLocalEvents,
                        headerRes = R.string.events,
                        cachedLocalOfRemoteRes = R.string.cached_events
                    )
                }
                Tab.PLACES -> {
                    addEntitiesStatsSection(
                        totalRemote = stats.totalRemotePlaces,
                        totalLocal = stats.totalLocalPlaces,
                        headerRes = R.string.places,
                        cachedLocalOfRemoteRes = R.string.cached_places
                    )
                }
                Tab.RECORDINGS -> {
                    addEntitiesStatsSection(
                        totalRemote = stats.totalRemoteRecordings,
                        totalLocal = stats.totalLocalRecordings,
                        headerRes = R.string.recordings,
                        cachedLocalOfRemoteRes = R.string.cached_recordings
                    )
                }
                Tab.RELATIONSHIPS -> {
                    addRelationshipsSection(
                        totalRelations = stats.totalRelations,
                        relationTypeCounts = stats.relationTypeCounts
                    )
                }
                Tab.RELEASES -> {
                    addEntitiesStatsSection(
                        totalRemote = stats.totalRemoteReleases,
                        totalLocal = stats.totalLocalReleases,
                        headerRes = R.string.releases,
                        cachedLocalOfRemoteRes = R.string.cached_releases
                    )
                }
                Tab.RELEASE_GROUPS -> {
                    addReleaseGroupsSection(
                        totalRemote = stats.totalRemoteReleaseGroups,
                        totalLocal = stats.totalLocalReleaseGroups,
                        releaseGroupTypeCounts = stats.releaseGroupTypeCounts
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
private fun Preview() {
    PreviewTheme {
        Surface {
            StatsScreen(
                tabs = listOf(Tab.RELATIONSHIPS, Tab.RELEASE_GROUPS, Tab.RELEASES, Tab.PLACES),
                stats = Stats(
                    totalRemoteReleaseGroups = 280,
                    totalLocalReleaseGroups = 279,
                    releaseGroupTypeCounts = listOf(
                        ReleaseGroupTypeCount(primaryType = "Album", count = 13),
                        ReleaseGroupTypeCount(
                            primaryType = "Album",
                            secondaryTypes = listOf("Compilation", "Demo"),
                            count = 1
                        ),
                    ),
                    totalRelations = 696,
                    relationTypeCounts = listOf(
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
