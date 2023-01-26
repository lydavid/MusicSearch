package ly.david.mbjc.ui.area.stats

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
import ly.david.data.persistence.area.AreaPlaceDao
import ly.david.data.persistence.area.ReleasesCountriesDao
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.relation.RelationTypeCount
import ly.david.mbjc.ui.stats.PlacesStats
import ly.david.mbjc.ui.stats.RelationsStats
import ly.david.mbjc.ui.stats.ReleasesStats
import ly.david.mbjc.ui.stats.Stats

interface AreaStatsRepository : RelationsStats, ReleasesStats, PlacesStats

@HiltViewModel
class AreaStatsViewModel @Inject constructor(
    override val relationDao: RelationDao,
    private val releasesCountriesDao: ReleasesCountriesDao,
    private val areaPlaceDao: AreaPlaceDao
) : ViewModel(), AreaStatsRepository {

    override suspend fun getTotalLocalReleases(resourceId: String): Int =
        releasesCountriesDao.getNumberOfReleasesByCountry(resourceId)

    override suspend fun getTotalLocalPlaces(resourceId: String): Int =
        areaPlaceDao.getNumberOfPlacesByArea(resourceId)

    fun getStats(resourceId: String, scope: CoroutineScope): StateFlow<Stats> {
        return scope.launchMolecule(RecompositionClock.ContextClock) {
            getAreaStatsPresenter(
                resourceId = resourceId,
                repository = this
            )
        }
    }
}

// TODO: could not test this with turbine, revisit later
@Composable
fun getAreaStatsPresenter(
    resourceId: String,
    repository: AreaStatsRepository
): Stats {
    var totalRelations: Int? by remember { mutableStateOf(null) }
    var relationTypeCounts by remember { mutableStateOf(listOf<RelationTypeCount>()) }
    var totalRemoteReleases: Int? by remember { mutableStateOf(0) }
    var totalLocalReleases by remember { mutableStateOf(0) }
    var totalRemotePlaces: Int? by remember { mutableStateOf(0) }
    var totalLocalPlaces by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        totalRelations = repository.getNumberOfRelationsByResource(resourceId)
        relationTypeCounts = repository.getCountOfEachRelationshipType(resourceId)
        totalRemoteReleases = repository.getTotalRemoteReleases(resourceId)
        totalLocalReleases = repository.getTotalLocalReleases(resourceId)
        totalRemotePlaces = repository.getTotalRemotePlaces(resourceId)
        totalLocalPlaces = repository.getTotalLocalPlaces(resourceId)
    }

    return Stats(
        totalRelations = totalRelations,
        relationTypeCounts = relationTypeCounts,
        totalRemoteReleases = totalRemoteReleases,
        totalLocalReleases = totalLocalReleases,
        totalRemotePlaces = totalRemotePlaces,
        totalLocalPlaces = totalLocalPlaces
    )
}
