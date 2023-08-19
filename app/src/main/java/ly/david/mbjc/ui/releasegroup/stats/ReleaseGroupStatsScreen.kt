package ly.david.mbjc.ui.releasegroup.stats

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
internal fun ReleaseGroupStatsScreen(
    releaseGroupId: String,
    modifier: Modifier = Modifier,
    tabs: List<Tab>,
    viewModel: ReleaseGroupStatsViewModel = hiltViewModel(),
) {
    var totalRemote: Int? by rememberSaveable { mutableStateOf(0) }
    var totalLocal by rememberSaveable { mutableStateOf(0) }

    var totalRelations: Int? by rememberSaveable { mutableStateOf(null) }
    var relationTypeCounts by remember { mutableStateOf(listOf<RelationTypeCount>()) }

    LaunchedEffect(key1 = totalRemote, key2 = totalLocal) {
        totalRemote = viewModel.getTotalRemoteReleases(releaseGroupId)
        totalLocal = viewModel.getTotalLocalReleases(releaseGroupId)

        totalRelations = viewModel.getNumberOfRelationsByEntity(releaseGroupId)
        relationTypeCounts = viewModel.getCountOfEachRelationshipType(releaseGroupId)
    }

    StatsScreen(
        modifier = modifier,
        tabs = tabs,
        stats = Stats(
            totalRemoteReleases = totalRemote,
            totalLocalReleases = totalLocal,
            totalRelations = totalRelations,
            relationTypeCounts = relationTypeCounts
        )
    )
}
