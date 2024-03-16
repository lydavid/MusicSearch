package ly.david.musicsearch.feature.stats

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import kotlinx.collections.immutable.persistentListOf
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.core.models.relation.RelationTypeCount
import ly.david.musicsearch.core.models.releasegroup.ReleaseGroupTypeCount
import ly.david.ui.common.topappbar.Tab
import ly.david.ui.core.preview.DefaultPreviews
import ly.david.ui.core.theme.PreviewTheme

@DefaultPreviews
@Composable
internal fun PreviewStatsScreen() {
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
                    releaseGroupStats = ReleaseGroupStats(
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
                    ),
                    releaseStats = ReleaseStats(
                        totalRemote = 20,
                        totalLocal = 15,
                    ),
                ),
            )
        }
    }
}
