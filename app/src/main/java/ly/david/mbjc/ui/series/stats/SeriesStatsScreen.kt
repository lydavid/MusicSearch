package ly.david.mbjc.ui.series.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ly.david.data.room.relation.RelationTypeCount
import ly.david.ui.common.topappbar.Tab
import ly.david.ui.stats.Stats
import ly.david.ui.stats.StatsScreen
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun SeriesStatsScreen(
    seriesId: String,
    modifier: Modifier = Modifier,
    tabs: List<Tab>,
    viewModel: SeriesStatsViewModel = koinViewModel(),
) {
    var totalRelations: Int? by rememberSaveable { mutableStateOf(null) }
    var relationTypeCounts by remember { mutableStateOf(listOf<RelationTypeCount>()) }

    LaunchedEffect(key1 = Unit) {
        totalRelations = viewModel.getNumberOfRelationsByEntity(seriesId)
        relationTypeCounts = viewModel.getCountOfEachRelationshipType(seriesId)
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
