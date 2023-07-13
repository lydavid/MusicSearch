package ly.david.mbjc.ui.release.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import ly.david.data.room.relation.RelationTypeCount
import ly.david.mbjc.ui.stats.Stats
import ly.david.mbjc.ui.stats.StatsScreen
import ly.david.ui.common.topappbar.Tab

@Composable
internal fun ReleaseStatsScreen(
    releaseId: String,
    modifier: Modifier = Modifier,
    tabs: List<Tab>,
    viewModel: ReleaseStatsViewModel = hiltViewModel(),
) {
    var totalRelations: Int? by rememberSaveable { mutableStateOf(null) }
    var relationTypeCounts by remember { mutableStateOf(listOf<RelationTypeCount>()) }

    LaunchedEffect(key1 = Unit) {
        totalRelations = viewModel.getNumberOfRelationsByEntity(releaseId)
        relationTypeCounts = viewModel.getCountOfEachRelationshipType(releaseId)
    }

    StatsScreen(
        modifier = modifier,
        tabs = tabs,
        stats = Stats(
            totalRelations = totalRelations,
            relationTypeCounts = relationTypeCounts
        )
    )
}
