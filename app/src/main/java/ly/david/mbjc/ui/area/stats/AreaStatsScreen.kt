package ly.david.mbjc.ui.area.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
internal fun AreaStatsScreen(
    areaId: String,
    tabs: ImmutableList<Tab>,
    modifier: Modifier = Modifier,
    viewModel: AreaStatsViewModel = koinViewModel(),
) {
    var totalRelations: Int? by remember { mutableStateOf(null) }
    var relationTypeCounts by remember { mutableStateOf(listOf<RelationTypeCount>()) }
    var totalRemoteReleases: Int? by remember { mutableStateOf(0) }
    var totalLocalReleases by remember { mutableIntStateOf(0) }
    var totalRemotePlaces: Int? by remember { mutableStateOf(0) }
    var totalLocalPlaces by remember { mutableIntStateOf(0) }

    LaunchedEffect(key1 = Unit) {
        totalRelations = viewModel.getNumberOfRelationsByEntity(areaId)
        relationTypeCounts = viewModel.getCountOfEachRelationshipType(areaId)
        totalRemoteReleases = viewModel.getTotalRemoteReleases(areaId)
        totalLocalReleases = viewModel.getTotalLocalReleases(areaId)
        totalRemotePlaces = viewModel.getTotalRemotePlaces(areaId)
        totalLocalPlaces = viewModel.getTotalLocalPlaces(areaId)
    }

    StatsScreen(
        tabs = tabs,
        stats = Stats(
            totalRelations = totalRelations,
            relationTypeCounts = relationTypeCounts.toImmutableList(),
            totalRemoteReleases = totalRemoteReleases,
            totalLocalReleases = totalLocalReleases,
            totalRemotePlaces = totalRemotePlaces,
            totalLocalPlaces = totalLocalPlaces
        ),
        modifier = modifier
    )
}
