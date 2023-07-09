package ly.david.mbjc.ui.artist.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import ly.david.data.room.relation.RelationTypeCount
import ly.david.data.room.releasegroup.ReleaseGroupTypeCount
import ly.david.ui.common.topappbar.Tab
import ly.david.mbjc.ui.stats.Stats
import ly.david.mbjc.ui.stats.StatsScreen

@Composable
internal fun ArtistStatsScreen(
    artistId: String,
    tabs: List<Tab>,
    modifier: Modifier = Modifier,
    viewModel: ArtistStatsViewModel = hiltViewModel()
) {
    var totalRemoteReleaseGroups: Int? by remember { mutableStateOf(null) }
    var totalLocalReleaseGroups by remember { mutableStateOf(0) }
    var releaseGroupTypeCounts by remember { mutableStateOf(listOf<ReleaseGroupTypeCount>()) }
    var totalRemoteReleases: Int? by remember { mutableStateOf(0) }
    var totalLocalReleases by remember { mutableStateOf(0) }
    var totalRelations: Int? by remember { mutableStateOf(null) }
    var relationTypeCounts by remember { mutableStateOf(listOf<RelationTypeCount>()) }

    LaunchedEffect(Unit) {
        totalRemoteReleaseGroups = viewModel.getTotalRemoteReleaseGroups(artistId)
        totalLocalReleaseGroups = viewModel.getTotalLocalReleaseGroups(artistId)
        releaseGroupTypeCounts = viewModel.getCountOfEachAlbumType(artistId)
        totalRemoteReleases = viewModel.getTotalRemoteReleases(artistId)
        totalLocalReleases = viewModel.getTotalLocalReleases(artistId)
        totalRelations = viewModel.getNumberOfRelationsByEntity(artistId)
        relationTypeCounts = viewModel.getCountOfEachRelationshipType(artistId)
    }

    StatsScreen(
        modifier = modifier,
        tabs = tabs,
        stats = Stats(
            totalRemoteReleaseGroups = totalRemoteReleaseGroups,
            totalLocalReleaseGroups = totalLocalReleaseGroups,
            releaseGroupTypeCounts = releaseGroupTypeCounts,
            totalRemoteReleases = totalRemoteReleases,
            totalLocalReleases = totalLocalReleases,
            totalRelations = totalRelations,
            relationTypeCounts = relationTypeCounts
        )
    )
}
