package ly.david.mbjc.ui.label.stats

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.relation.RelationTypeCount
import ly.david.mbjc.R
import ly.david.mbjc.ui.common.addSpacer
import ly.david.mbjc.ui.common.preview.DefaultPreviews
import ly.david.mbjc.ui.stats.addRelationshipsSection
import ly.david.mbjc.ui.stats.addResourcesStatsSection
import ly.david.mbjc.ui.theme.PreviewTheme

@Composable
internal fun LabelStatsScreen(
    labelId: String,
    viewModel: LabelStatsViewModel = hiltViewModel()
) {
    var totalRemote: Int? by rememberSaveable { mutableStateOf(0) }
    var totalLocal by rememberSaveable { mutableStateOf(0) }

    var totalRelations: Int? by rememberSaveable { mutableStateOf(null) }
    var relationTypeCounts by remember { mutableStateOf(listOf<RelationTypeCount>()) }

    LaunchedEffect(key1 = Unit) {
        totalRemote = viewModel.getTotalRemoteReleases(labelId)
        totalLocal = viewModel.getTotalLocalReleases(labelId)

        totalRelations = viewModel.getNumberOfRelationsByResource(labelId)
        relationTypeCounts = viewModel.getCountOfEachRelationshipType(labelId)
    }

    LabelStatsScreen(
        totalRemote = totalRemote,
        totalLocal = totalLocal,
        totalRelations = totalRelations,
        relationTypeCounts = relationTypeCounts
    )
}

@Composable
private fun LabelStatsScreen(
    totalRemote: Int?,
    totalLocal: Int,
    totalRelations: Int?,
    relationTypeCounts: List<RelationTypeCount>
) {
    LazyColumn {
        addResourcesStatsSection(
            totalRemote = totalRemote,
            totalLocal = totalLocal,
            headerRes = R.string.releases,
            cachedLocalOfRemoteRes = R.string.cached_releases
        )
        addSpacer()
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
            LabelStatsScreen(
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
