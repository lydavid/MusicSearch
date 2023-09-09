package ly.david.mbjc.ui.instrument.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ly.david.data.room.relation.RelationTypeCount
import ly.david.ui.common.topappbar.Tab
import ly.david.ui.stats.Stats
import ly.david.ui.stats.StatsScreen
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun InstrumentStatsScreen(
    instrumentId: String,
    modifier: Modifier = Modifier,
    tabs: List<Tab>,
    viewModel: InstrumentStatsViewModel = koinViewModel(),
) {
    var totalRelations: Int? by remember { mutableStateOf(null) }
    var relationTypeCounts by remember { mutableStateOf(listOf<RelationTypeCount>()) }

    LaunchedEffect(Unit) {
        totalRelations = viewModel.getNumberOfRelationsByEntity(instrumentId)
        relationTypeCounts = viewModel.getCountOfEachRelationshipType(instrumentId)
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
