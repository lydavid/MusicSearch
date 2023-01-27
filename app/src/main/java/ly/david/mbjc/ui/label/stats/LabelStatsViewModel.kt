package ly.david.mbjc.ui.label.stats

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
import ly.david.data.persistence.label.ReleaseLabelDao
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.relation.RelationTypeCount
import ly.david.mbjc.ui.stats.RelationsStats
import ly.david.mbjc.ui.stats.ReleasesStats
import ly.david.mbjc.ui.stats.Stats

interface LabelStatsRepository : ReleasesStats, RelationsStats

@HiltViewModel
class LabelStatsViewModel @Inject constructor(
    override val relationDao: RelationDao,
    private val releaseLabelDao: ReleaseLabelDao
) : ViewModel(), LabelStatsRepository {

    override suspend fun getTotalLocalReleases(resourceId: String): Int =
        releaseLabelDao.getNumberOfReleasesByLabel(resourceId)

    fun getStats(resourceId: String, scope: CoroutineScope): StateFlow<Stats> {
        return scope.launchMolecule(RecompositionClock.ContextClock) {
            getLabelStatsPresenter(
                resourceId = resourceId,
                repository = this
            )
        }
    }
}

@Composable
fun getLabelStatsPresenter(
    resourceId: String,
    repository: LabelStatsRepository
): Stats {
    var totalRelations: Int? by remember { mutableStateOf(null) }
    var relationTypeCounts by remember { mutableStateOf(listOf<RelationTypeCount>()) }
    var totalRemoteReleases: Int? by remember { mutableStateOf(0) }
    var totalLocalReleases by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        totalRelations = repository.getNumberOfRelationsByResource(resourceId)
        relationTypeCounts = repository.getCountOfEachRelationshipType(resourceId)
        totalRemoteReleases = repository.getTotalRemoteReleases(resourceId)
        totalLocalReleases = repository.getTotalLocalReleases(resourceId)
    }

    return Stats(
        totalRelations = totalRelations,
        relationTypeCounts = relationTypeCounts,
        totalRemoteReleases = totalRemoteReleases,
        totalLocalReleases = totalLocalReleases,
    )
}
