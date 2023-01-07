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
import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.artist.ArtistReleaseGroupDao
import ly.david.data.persistence.artist.release.ArtistReleaseDao
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.relation.RelationTypeCount
import ly.david.data.persistence.releasegroup.ReleaseGroupTypeCount
import ly.david.mbjc.ui.stats.RelationsStats
import ly.david.mbjc.ui.stats.ReleasesStats

@HiltViewModel
internal class ArtistStatsViewModel @Inject constructor(
    private val artistReleaseGroupDao: ArtistReleaseGroupDao,
    private val artistReleaseDao: ArtistReleaseDao,
    override val relationDao: RelationDao
) : ViewModel(), RelationsStats, ReleasesStats {

    @Composable
    private fun getArtistStatsPresenter(
        resourceId: String
    ): ArtistStats {
        var totalRemoteReleaseGroups by remember { mutableStateOf(0) }
        var totalLocalReleaseGroups by remember { mutableStateOf(0) }
        var releaseGroupTypeCounts by remember { mutableStateOf(listOf<ReleaseGroupTypeCount>()) }
        var totalRemoteReleases by remember { mutableStateOf(0) }
        var totalLocalReleases by remember { mutableStateOf(0) }
        var totalRelations: Int? by remember { mutableStateOf(null) }
        var relationTypeCounts by remember { mutableStateOf(listOf<RelationTypeCount>()) }

        // TODO: use methods and generalize with interfaces
        LaunchedEffect(Unit) {
            totalRemoteReleaseGroups =
                relationDao.getBrowseResourceCount(resourceId, MusicBrainzResource.RELEASE_GROUP)?.remoteCount ?: 0
            totalLocalReleaseGroups = artistReleaseGroupDao.getNumberOfReleaseGroupsByArtist(resourceId)
            releaseGroupTypeCounts = artistReleaseGroupDao.getCountOfEachAlbumType(resourceId)
            totalRemoteReleases = getTotalLocalReleases(resourceId)
            totalLocalReleases = getTotalLocalReleases(resourceId)
            totalRelations = getNumberOfRelationsByResource(resourceId)
            relationTypeCounts = getCountOfEachRelationshipType(resourceId)
        }

        return ArtistStats(
            totalRemoteReleaseGroups = totalRemoteReleaseGroups,
            totalLocalReleaseGroups = totalLocalReleaseGroups,
            releaseGroupTypeCounts = releaseGroupTypeCounts,
            totalLocalReleases = totalRemoteReleases,
            totalRemoteReleases = totalLocalReleases,
            totalRelations = totalRelations,
            relationTypeCounts = relationTypeCounts
        )
    }

    fun getStats(resourceId: String, scope: CoroutineScope): StateFlow<ArtistStats> {
        return scope.launchMolecule(RecompositionClock.ContextClock) {
            getArtistStatsPresenter(resourceId)
        }
    }

    override suspend fun getTotalLocalReleases(resourceId: String): Int {
        return artistReleaseDao.getNumberOfReleasesByArtist(resourceId)
    }

//    suspend fun getTotalReleaseGroups(artistId: String) =
//        relationDao.getBrowseResourceCount(artistId, MusicBrainzResource.RELEASE_GROUP)?.remoteCount ?: 0

//    suspend fun getNumberOfReleaseGroupsByArtist(artistId: String) =
//        artistReleaseGroupDao.getNumberOfReleaseGroupsByArtist(artistId)

//    suspend fun getCountOfEachAlbumType(artistId: String) =
//        artistReleaseGroupDao.getCountOfEachAlbumType(artistId)
}
