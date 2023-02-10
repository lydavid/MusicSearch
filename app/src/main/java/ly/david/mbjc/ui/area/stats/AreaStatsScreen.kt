package ly.david.mbjc.ui.area.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import ly.david.data.persistence.relation.RelationTypeCount
import ly.david.mbjc.ui.common.Tab
import ly.david.mbjc.ui.stats.Stats
import ly.david.mbjc.ui.stats.StatsScreen

@Composable
internal fun AreaStatsScreen(
    areaId: String,
    modifier: Modifier = Modifier,
    tabs: List<Tab>,
    viewModel: AreaStatsViewModel = hiltViewModel()
) {
    var totalRelations: Int? by remember { mutableStateOf(null) }
    var relationTypeCounts by remember { mutableStateOf(listOf<RelationTypeCount>()) }
    var totalRemoteReleases: Int? by remember { mutableStateOf(0) }
    var totalLocalReleases by remember { mutableStateOf(0) }
    var totalRemotePlaces: Int? by remember { mutableStateOf(0) }
    var totalLocalPlaces by remember { mutableStateOf(0) }

    LaunchedEffect(key1 = Unit) {
        totalRelations = viewModel.getNumberOfRelationsByResource(areaId)
        relationTypeCounts = viewModel.getCountOfEachRelationshipType(areaId)
        totalRemoteReleases = viewModel.getTotalRemoteReleases(areaId)
        totalLocalReleases = viewModel.getTotalLocalReleases(areaId)
        totalRemotePlaces = viewModel.getTotalRemotePlaces(areaId)
        totalLocalPlaces = viewModel.getTotalLocalPlaces(areaId)
    }

    StatsScreen(
        modifier = modifier,
        tabs = tabs,
        stats = Stats(
            totalRelations = totalRelations,
            relationTypeCounts = relationTypeCounts,
            totalRemoteReleases = totalRemoteReleases,
            totalLocalReleases = totalLocalReleases,
            totalRemotePlaces = totalRemotePlaces,
            totalLocalPlaces = totalLocalPlaces
        )
    )
}
