package ly.david.mbjc.ui.event.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import ly.david.data.persistence.relation.RelationTypeCount
import ly.david.ui.common.topappbar.Tab
import ly.david.mbjc.ui.stats.Stats
import ly.david.mbjc.ui.stats.StatsScreen

@Composable
internal fun EventStatsScreen(
    modifier: Modifier = Modifier,
    eventId: String,
    tabs: List<Tab>,
    viewModel: EventStatsViewModel = hiltViewModel()
) {
    var totalRelations: Int? by remember { mutableStateOf(null) }
    var relationTypeCounts by remember { mutableStateOf(listOf<RelationTypeCount>()) }

    LaunchedEffect(Unit) {
        totalRelations = viewModel.getNumberOfRelationsByResource(eventId)
        relationTypeCounts = viewModel.getCountOfEachRelationshipType(eventId)
    }

    StatsScreen(
        modifier = modifier,
        tabs = tabs,
        stats = Stats(
            totalRelations = totalRelations,
            relationTypeCounts = relationTypeCounts,
        )
    )
}
