package ly.david.mbjc.ui.place.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import ly.david.data.room.relation.RelationTypeCount
import ly.david.ui.common.topappbar.Tab
import ly.david.mbjc.ui.stats.Stats
import ly.david.mbjc.ui.stats.StatsScreen

@Composable
internal fun PlaceStatsScreen(
    placeId: String,
    modifier: Modifier = Modifier,
    tabs: List<Tab>,
    viewModel: PlaceStatsViewModel = hiltViewModel()
) {
    var totalRelations: Int? by remember { mutableStateOf(null) }
    var relationTypeCounts by remember { mutableStateOf(listOf<RelationTypeCount>()) }
    var totalRemoteEvents: Int? by remember { mutableStateOf(0) }
    var totalLocalEvents by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        totalRelations = viewModel.getNumberOfRelationsByEntity(placeId)
        relationTypeCounts = viewModel.getCountOfEachRelationshipType(placeId)
        totalRemoteEvents = viewModel.getTotalRemoteEvents(placeId)
        totalLocalEvents = viewModel.getTotalLocalEvents(placeId)
    }

    StatsScreen(
        modifier = modifier,
        tabs = tabs,
        stats = Stats(
            totalRelations = totalRelations,
            relationTypeCounts = relationTypeCounts,
            totalRemoteEvents = totalRemoteEvents,
            totalLocalEvents = totalLocalEvents,
        )
    )
}
