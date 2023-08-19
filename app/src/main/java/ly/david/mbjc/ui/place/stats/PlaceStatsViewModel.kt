package ly.david.mbjc.ui.place.stats

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.room.place.events.EventPlaceDao
import ly.david.data.room.relation.RelationDao
import ly.david.ui.stats.EventStats
import ly.david.ui.stats.RelationsStats

@HiltViewModel
class PlaceStatsViewModel @Inject constructor(
    override val relationDao: RelationDao,
    private val eventPlaceDao: EventPlaceDao,
) : ViewModel(),
    RelationsStats,
    EventStats {

    override suspend fun getTotalLocalEvents(entityId: String): Int =
        eventPlaceDao.getNumberOfEventsByPlace(entityId)
}
