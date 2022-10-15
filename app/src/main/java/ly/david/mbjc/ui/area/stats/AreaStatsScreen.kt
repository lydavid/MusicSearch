package ly.david.mbjc.ui.area.stats

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ly.david.mbjc.data.network.MusicBrainzResource
import ly.david.mbjc.data.persistence.relation.RelationTypeCount
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.relation.stats.addRelationshipsSection
import ly.david.mbjc.ui.release.stats.addReleasesSection
import ly.david.mbjc.ui.theme.PreviewTheme

@Composable
internal fun AreaStatsScreen(
    areaId: String,
    showReleases: Boolean,
    viewModel: AreaStatsViewModel = hiltViewModel()
) {
    var totalRemote by rememberSaveable { mutableStateOf(0) }
    var totalLocal by rememberSaveable { mutableStateOf(0) }

    var totalRelations: Int? by rememberSaveable { mutableStateOf(null) }
    var relationTypeCounts by rememberSaveable { mutableStateOf(listOf<RelationTypeCount>()) }

    LaunchedEffect(key1 = Unit) {
        totalRemote = viewModel.getTotalReleases(areaId)
        totalLocal = viewModel.getNumberOfReleasesByLabel(areaId)

        totalRelations = viewModel.getNumberOfRelationsByResource(areaId)
        relationTypeCounts = viewModel.getCountOfEachRelationshipType(areaId)
    }

    AreaStatsScreen(
        showReleases = showReleases,
        totalRemote = totalRemote,
        totalLocal = totalLocal,
        totalRelations = totalRelations,
        relationTypeCounts = relationTypeCounts
    )
}

internal fun LazyListScope.addSpacer(spacing: Dp = 16.dp) {
    item {
        Spacer(modifier = Modifier.padding(top = spacing))
    }
}

@Composable
private fun AreaStatsScreen(
    showReleases: Boolean,
    totalRemote: Int,
    totalLocal: Int,
    totalRelations: Int?,
    relationTypeCounts: List<RelationTypeCount>
) {
    LazyColumn {
        if (showReleases) {
            addReleasesSection(
                totalRemote = totalRemote,
                totalLocal = totalLocal,
            )
            addSpacer()
        }

        addRelationshipsSection(
            totalRelations = totalRelations,
            relationTypeCounts = relationTypeCounts
        )
    }
}

@DefaultPreviews
@Composable
private fun Preview() {
    PreviewTheme {
        Surface {
            AreaStatsScreen(
                showReleases = true,
                totalRemote = 1,
                totalLocal = 2,
                totalRelations = 3,
                relationTypeCounts = listOf(
                    RelationTypeCount(MusicBrainzResource.URL, 3)
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
                totalRemote = 1,
                totalLocal = 2,
                totalRelations = 3,
                relationTypeCounts = listOf(
                    RelationTypeCount(MusicBrainzResource.URL, 3)
                )
            )
        }
    }
}
