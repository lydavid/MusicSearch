package ly.david.mbjc.ui.area.stats

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.relation.RelationTypeCount
import ly.david.mbjc.ui.common.addSpacer
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.stats.addRelationshipsSection
import ly.david.mbjc.ui.stats.addReleasesSection
import ly.david.mbjc.ui.theme.PreviewTheme

// TODO: instead of making a stats screen for every scaffold
//  can we have one and based on whether it has relationships/BrowseResourceCount
//  show the relevant stats?
@Composable
internal fun AreaStatsScreen(
    areaId: String,
    showReleases: Boolean,
    viewModel: AreaStatsViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val areaStats by remember { viewModel.getStats(areaId, coroutineScope) }.collectAsState()

    AreaStatsScreen(
        showReleases = showReleases,
        stats = areaStats
    )
}

@Composable
private fun AreaStatsScreen(
    showReleases: Boolean,
    stats: AreaStats
) {
    LazyColumn {
        if (showReleases) {
            addReleasesSection(
                totalRemote = stats.totalRemoteReleases,
                totalLocal = stats.totalLocalReleases,
            )
            addSpacer()
        }

        addRelationshipsSection(
            totalRelations = stats.totalRelations,
            relationTypeCounts = stats.relationTypeCounts
        )
    }
}

// region Previews
@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            AreaStatsScreen(
                showReleases = true,
                stats = AreaStats(
                    totalRemoteReleases = 1,
                    totalLocalReleases = 2,
                    totalRelations = 3,
                    relationTypeCounts = listOf(
                        RelationTypeCount(MusicBrainzResource.URL, 3)
                    )
                )
            )
        }
    }
}

@DefaultPreviews
@Composable
private fun PreviewNoReleases() {
    PreviewTheme {
        Surface {
            AreaStatsScreen(
                showReleases = false,
                stats = AreaStats(
                    totalRemoteReleases = 1,
                    totalLocalReleases = 2,
                    totalRelations = 3,
                    relationTypeCounts = listOf(
                        RelationTypeCount(MusicBrainzResource.URL, 3)
                    )
                )
            )
        }
    }
}
// endregion
