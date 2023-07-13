package ly.david.mbjc.ui.label.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import ly.david.data.room.relation.RelationTypeCount
import ly.david.mbjc.ui.stats.Stats
import ly.david.mbjc.ui.stats.StatsScreen
import ly.david.ui.common.topappbar.Tab

@Composable
internal fun LabelStatsScreen(
    labelId: String,
    modifier: Modifier = Modifier,
    tabs: List<Tab>,
    viewModel: LabelStatsViewModel = hiltViewModel(),
) {
    var totalRelations: Int? by remember { mutableStateOf(null) }
    var relationTypeCounts by remember { mutableStateOf(listOf<RelationTypeCount>()) }
    var totalRemoteReleases: Int? by remember { mutableStateOf(0) }
    var totalLocalReleases by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        totalRelations = viewModel.getNumberOfRelationsByEntity(labelId)
        relationTypeCounts = viewModel.getCountOfEachRelationshipType(labelId)
        totalRemoteReleases = viewModel.getTotalRemoteReleases(labelId)
        totalLocalReleases = viewModel.getTotalLocalReleases(labelId)
    }

    StatsScreen(
        modifier = modifier,
        tabs = tabs,
        stats = Stats(
            totalRelations = totalRelations,
            relationTypeCounts = relationTypeCounts,
            totalRemoteReleases = totalRemoteReleases,
            totalLocalReleases = totalLocalReleases,
        )
    )
}
