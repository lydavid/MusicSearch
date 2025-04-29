package ly.david.musicsearch.shared.feature.stats

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.persistentHashMapOf
import kotlinx.collections.immutable.persistentListOf
import kotlinx.datetime.Instant
import ly.david.musicsearch.shared.domain.network.MusicBrainzEntity
import ly.david.musicsearch.shared.domain.relation.RelationTypeCount
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupTypeCount
import ly.david.musicsearch.ui.common.topappbar.Tab
import ly.david.musicsearch.ui.core.theme.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewStatsUi() {
    PreviewTheme {
        Surface {
            StatsUi(
                tabs = persistentListOf(
                    Tab.RELATIONSHIPS,
                    Tab.RELEASE_GROUPS,
                    Tab.RELEASES,
                    Tab.PLACES,
                ),
                stats = Stats(
                    totalRelations = 696,
                    relationTypeCounts = persistentListOf(
                        RelationTypeCount(
                            linkedEntity = MusicBrainzEntity.ARTIST,
                            count = 17,
                        ),
                        RelationTypeCount(
                            linkedEntity = MusicBrainzEntity.RECORDING,
                            count = 397,
                        ),
                    ),
                    persistentHashMapOf(
                        Tab.RELEASE_GROUPS to EntityStats(
                            totalRemote = 280,
                            totalLocal = 279,
                            releaseGroupTypeCounts = persistentListOf(
                                ReleaseGroupTypeCount(
                                    primaryType = "Album",
                                    count = 13,
                                ),
                                ReleaseGroupTypeCount(
                                    primaryType = "Album",
                                    secondaryTypes = listOf(
                                        "Compilation",
                                        "Demo",
                                    ),
                                    count = 1,
                                ),
                            ),
                            lastUpdated = Instant.parse("2025-03-26T06:42:20Z"),
                        ),
                        Tab.RELEASES to EntityStats(
                            totalRemote = 20,
                            totalLocal = 15,
                            lastUpdated = Instant.parse("2024-04-26T06:42:20Z"),
                        ),
                    ),
                ),
                now = Instant.parse("2025-04-26T16:42:20Z"),
            )
        }
    }
}
