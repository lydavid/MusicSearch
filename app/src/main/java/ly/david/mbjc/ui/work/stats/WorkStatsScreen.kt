package ly.david.mbjc.ui.work.stats

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
import ly.david.ui.stats.Stats
import ly.david.ui.stats.StatsScreen
import ly.david.ui.common.topappbar.Tab

@Composable
internal fun WorkGroupStatsScreen(
    workId: String,
    modifier: Modifier = Modifier,
    tabs: List<Tab>,
    viewModel: WorkStatsViewModel = hiltViewModel(),
) {
    var totalRelations: Int? by rememberSaveable { mutableStateOf(null) }
    var relationTypeCounts by remember { mutableStateOf(listOf<RelationTypeCount>()) }
    var totalRemoteRecordings: Int? by rememberSaveable { mutableStateOf(0) }
    var totalLocalRecordings by rememberSaveable { mutableStateOf(0) }

    LaunchedEffect(key1 = totalRemoteRecordings, key2 = totalLocalRecordings) {
        totalRelations = viewModel.getNumberOfRelationsByEntity(workId)
        relationTypeCounts = viewModel.getCountOfEachRelationshipType(workId)
        totalRemoteRecordings = viewModel.getTotalRemoteRecordings(workId)
        totalLocalRecordings = viewModel.getTotalLocalRecordings(workId)
    }

    StatsScreen(
        modifier = modifier,
        tabs = tabs,
        stats = Stats(
            totalRelations = totalRelations,
            relationTypeCounts = relationTypeCounts,
            totalLocalRecordings = totalLocalRecordings,
            totalRemoteRecordings = totalRemoteRecordings,
        )
    )
}
