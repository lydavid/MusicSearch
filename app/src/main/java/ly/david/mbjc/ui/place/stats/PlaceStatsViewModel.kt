package ly.david.mbjc.ui.place.stats

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
import ly.david.data.persistence.event.EventPlaceDao
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.relation.RelationTypeCount
import ly.david.mbjc.ui.stats.EventStats
import ly.david.mbjc.ui.stats.RelationsStats
import ly.david.mbjc.ui.stats.Stats

interface PlaceStatsRepository : RelationsStats, EventStats

@HiltViewModel
class PlaceStatsViewModel @Inject constructor(
    override val relationDao: RelationDao,
    private val eventPlaceDao: EventPlaceDao
) : ViewModel(), PlaceStatsRepository {

    override suspend fun getTotalLocalEvents(resourceId: String): Int =
        eventPlaceDao.getNumberOfEventsByPlace(resourceId)

    fun getStats(resourceId: String, scope: CoroutineScope): StateFlow<Stats> {
        return scope.launchMolecule(RecompositionClock.ContextClock) {
            getPlaceStatsPresenter(
                resourceId = resourceId,
                repository = this
            )
        }
    }
}

@Composable
fun getPlaceStatsPresenter(
    resourceId: String,
    repository: PlaceStatsRepository
): Stats {
    var totalRelations: Int? by remember { mutableStateOf(null) }
    var relationTypeCounts by remember { mutableStateOf(listOf<RelationTypeCount>()) }
    var totalRemoteEvents: Int? by remember { mutableStateOf(0) }
    var totalLocalEvents by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        totalRelations = repository.getNumberOfRelationsByResource(resourceId)
        relationTypeCounts = repository.getCountOfEachRelationshipType(resourceId)
        totalRemoteEvents = repository.getTotalRemoteEvents(resourceId)
        totalLocalEvents = repository.getTotalLocalEvents(resourceId)
    }

    return Stats(
        totalRelations = totalRelations,
        relationTypeCounts = relationTypeCounts,
        totalRemoteEvents = totalRemoteEvents,
        totalLocalEvents = totalLocalEvents,
    )
}
