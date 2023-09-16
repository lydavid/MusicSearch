package ly.david.mbjc.ui.event.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import ly.david.ui.common.topappbar.Tab
import ly.david.ui.stats.Stats
import ly.david.ui.stats.StatsScreen
import org.koin.androidx.compose.koinViewModel

@Composable
internal fun EventStatsScreen(
    modifier: Modifier = Modifier,
    eventId: String,
    tabs: ImmutableList<Tab>,
    viewModel: EventStatsViewModel = koinViewModel(),
) {
    val totalRelations by viewModel.getNumberOfRelationsByEntity(eventId).collectAsState()
    val relationTypeCounts by viewModel.getCountOfEachRelationshipType(eventId).collectAsState()

    StatsScreen(
        modifier = modifier,
        tabs = tabs,
        stats = Stats(
            totalRelations = totalRelations,
            relationTypeCounts = relationTypeCounts.toImmutableList(),
        )
    )
}
