package ly.david.mbjc.ui.place.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import ly.david.data.room.relation.RelationTypeCount
import ly.david.ui.common.topappbar.Tab
import ly.david.ui.stats.Stats
import ly.david.ui.stats.StatsScreen
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun PlaceStatsScreen(
    placeId: String,
    modifier: Modifier = Modifier,
    tabs: ImmutableList<Tab>,
    viewModel: PlaceStatsViewModel = koinViewModel(),
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
            relationTypeCounts = relationTypeCounts.toImmutableList(),
            totalRemoteEvents = totalRemoteEvents,
            totalLocalEvents = totalLocalEvents,
        )
    )
}
