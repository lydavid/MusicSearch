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
import ly.david.data.persistence.area.ReleasesCountriesDao
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.relation.RelationTypeCount
import ly.david.mbjc.ui.stats.RelationsStats
import ly.david.mbjc.ui.stats.ReleasesStats

interface AreaStatsRepository : RelationsStats, ReleasesStats

@HiltViewModel
class AreaStatsViewModel @Inject constructor(
    override val relationDao: RelationDao,
    private val releasesCountriesDao: ReleasesCountriesDao
) : ViewModel(), AreaStatsRepository {

    override suspend fun getTotalLocalReleases(resourceId: String) =
        releasesCountriesDao.getNumberOfReleasesByCountry(resourceId)

    fun getStats(resourceId: String, scope: CoroutineScope): StateFlow<AreaStats> {
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
): AreaStats {
    var totalRemoteReleases: Int? by remember { mutableStateOf(0) }
    var totalLocalReleases by remember { mutableStateOf(0) }
    var totalRelations: Int? by remember { mutableStateOf(null) }
    var relationTypeCounts by remember { mutableStateOf(listOf<RelationTypeCount>()) }

    LaunchedEffect(Unit) {
        totalRemoteReleases = repository.getTotalRemoteReleases(resourceId)
        totalLocalReleases = repository.getTotalLocalReleases(resourceId)
        totalRelations = repository.getNumberOfRelationsByResource(resourceId)
        relationTypeCounts = repository.getCountOfEachRelationshipType(resourceId)
    }

    return AreaStats(
        totalRemoteReleases = totalRemoteReleases,
        totalLocalReleases = totalLocalReleases,
        totalRelations = totalRelations,
        relationTypeCounts = relationTypeCounts
    )
}
