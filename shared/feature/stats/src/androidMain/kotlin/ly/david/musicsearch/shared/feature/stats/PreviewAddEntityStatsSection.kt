package ly.david.musicsearch.shared.feature.stats

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import kotlinx.collections.immutable.persistentListOf
import kotlin.time.Instant
import ly.david.musicsearch.shared.domain.releasegroup.ReleaseGroupTypeCount
import ly.david.musicsearch.ui.common.preview.PreviewTheme

@PreviewLightDark
@Composable
internal fun PreviewAddEntityStatsSection() {
    PreviewTheme {
        Surface {
            LazyColumn {
                addEntityStatsSection(
                    entityStats = EntityStats(
                        totalLocal = 100,
                        totalRemote = 200,
                        totalVisited = 18,
                        totalCollected = 9,
                        lastUpdated = Instant.parse("2025-04-26T06:42:20Z"),
                    ),
                    header = "Releases",
                    now = Instant.parse("2025-04-26T16:42:20Z"),
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
internal fun PreviewAddEntityStatsSectionReleaseGroup() {
    PreviewTheme {
        Surface {
            LazyColumn {
                addEntityStatsSection(
                    entityStats = EntityStats(
                        totalRemote = 280,
                        totalLocal = 280,
                        totalVisited = 140,
                        totalCollected = 200,
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
                        lastUpdated = Instant.parse("2025-04-26T06:42:20Z"),
                    ),
                    header = "Release Groups",
                    now = Instant.parse("2025-04-26T16:42:20Z"),
                )
            }
        }
    }
}
