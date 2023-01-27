package ly.david.mbjc.ui.event.stats

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
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.relation.RelationTypeCount
import ly.david.mbjc.ui.stats.RelationsStats
import ly.david.mbjc.ui.stats.Stats

interface EventStatsRepository : RelationsStats

@HiltViewModel
class EventStatsViewModel @Inject constructor(
    override val relationDao: RelationDao,
) : ViewModel(), EventStatsRepository {

    fun getStats(resourceId: String, scope: CoroutineScope): StateFlow<Stats> {
        return scope.launchMolecule(RecompositionClock.ContextClock) {
            getEventStatsPresenter(
                resourceId = resourceId,
                repository = this
            )
        }
    }
}

@Composable
fun getEventStatsPresenter(
    resourceId: String,
    repository: EventStatsRepository
): Stats {
    var totalRelations: Int? by remember { mutableStateOf(null) }
    var relationTypeCounts by remember { mutableStateOf(listOf<RelationTypeCount>()) }

    LaunchedEffect(Unit) {
        totalRelations = repository.getNumberOfRelationsByResource(resourceId)
        relationTypeCounts = repository.getCountOfEachRelationshipType(resourceId)
    }

    return Stats(
        totalRelations = totalRelations,
        relationTypeCounts = relationTypeCounts,
    )
}
