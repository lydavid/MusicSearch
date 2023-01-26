package ly.david.mbjc.ui.stats

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.relation.RelationTypeCount
import ly.david.data.persistence.releasegroup.ReleaseGroupTypeCount
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.Tab
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.theme.PreviewTheme

/**
 * Display all [stats] ordered/shown based on [tabs].
 */
@Composable
internal fun StatsScreen(
    tabs: List<Tab>,
    stats: Stats
) {
    LazyColumn {
        tabs.forEach { tab ->
            when (tab) {
                Tab.PLACES -> {
                    addResourcesStatsSection(
                        totalRemote = stats.totalRemotePlaces,
                        totalLocal = stats.totalLocalPlaces,
                        headerRes = R.string.places,
                        cachedLocalOfRemoteRes = R.string.cached_places
                    )
                }
                Tab.RELATIONSHIPS -> {
                    addRelationshipsSection(
                        totalRelations = stats.totalRelations,
                        relationTypeCounts = stats.relationTypeCounts
                    )
                }
                Tab.RELEASES -> {
                    // TODO: map tab to res, then places/releases can use same case
                    addResourcesStatsSection(
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
                // TODO: rest
                else -> {
                    // Do nothing.
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
                        RelationTypeCount(linkedResource = MusicBrainzResource.ARTIST, 17),
                        RelationTypeCount(linkedResource = MusicBrainzResource.RECORDING, 397),
                    ),
                    totalRemoteReleases = 20,
                    totalLocalReleases = 15
                )
            )
        }
    }
}
// endregion
