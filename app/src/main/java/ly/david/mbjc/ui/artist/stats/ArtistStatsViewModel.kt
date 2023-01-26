package ly.david.mbjc.ui.artist.stats

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import app.cash.molecule.RecompositionClock
import app.cash.molecule.launchMolecule
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import ly.david.data.persistence.artist.ArtistReleaseGroupDao
import ly.david.data.persistence.artist.release.ArtistReleaseDao
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.relation.RelationTypeCount
import ly.david.data.persistence.releasegroup.ReleaseGroupTypeCount
import ly.david.mbjc.ui.stats.RelationsStats
import ly.david.mbjc.ui.stats.ReleaseGroupsStats
import ly.david.mbjc.ui.stats.ReleasesStats
import ly.david.mbjc.ui.stats.Stats

@HiltViewModel
internal class ArtistStatsViewModel @Inject constructor(
    private val artistReleaseGroupDao: ArtistReleaseGroupDao,
    private val artistReleaseDao: ArtistReleaseDao,
    override val relationDao: RelationDao
) : ViewModel(), ReleaseGroupsStats, ReleasesStats, RelationsStats {

    override suspend fun getTotalLocalReleaseGroups(resourceId: String) =
        artistReleaseGroupDao.getNumberOfReleaseGroupsByArtist(resourceId)

    override suspend fun getCountOfEachAlbumType(resourceId: String) =
        artistReleaseGroupDao.getCountOfEachAlbumType(resourceId)

    override suspend fun getTotalLocalReleases(resourceId: String): Int {
        return artistReleaseDao.getNumberOfReleasesByArtist(resourceId)
    }

    @Composable
    private fun getArtistStatsPresenter(
        resourceId: String
    ): Stats {
        var totalRemoteReleaseGroups: Int? by remember { mutableStateOf(null) }
        var totalLocalReleaseGroups by remember { mutableStateOf(0) }
        var releaseGroupTypeCounts by remember { mutableStateOf(listOf<ReleaseGroupTypeCount>()) }
        var totalRemoteReleases: Int? by remember { mutableStateOf(0) }
        var totalLocalReleases by remember { mutableStateOf(0) }
        var totalRelations: Int? by remember { mutableStateOf(null) }
        var relationTypeCounts by remember { mutableStateOf(listOf<RelationTypeCount>()) }

        LaunchedEffect(Unit) {
            totalRemoteReleaseGroups = getTotalRemoteReleaseGroups(resourceId)
            totalLocalReleaseGroups = getTotalLocalReleaseGroups(resourceId)
            releaseGroupTypeCounts = getCountOfEachAlbumType(resourceId)
            totalRemoteReleases = getTotalRemoteReleases(resourceId)
            totalLocalReleases = getTotalLocalReleases(resourceId)
            totalRelations = getNumberOfRelationsByResource(resourceId)
            relationTypeCounts = getCountOfEachRelationshipType(resourceId)
        }

        return Stats(
            totalRemoteReleaseGroups = totalRemoteReleaseGroups,
            totalLocalReleaseGroups = totalLocalReleaseGroups,
            releaseGroupTypeCounts = releaseGroupTypeCounts,
            totalRemoteReleases = totalRemoteReleases,
            totalLocalReleases = totalLocalReleases,
            totalRelations = totalRelations,
            relationTypeCounts = relationTypeCounts
        )
    }

    fun getStats(resourceId: String, scope: CoroutineScope): StateFlow<Stats> {
        return scope.launchMolecule(RecompositionClock.ContextClock) {
            getArtistStatsPresenter(resourceId)
        }
    }
}
