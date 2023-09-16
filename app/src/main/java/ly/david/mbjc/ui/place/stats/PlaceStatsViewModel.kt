package ly.david.mbjc.ui.place.stats

import androidx.lifecycle.ViewModel
import ly.david.data.room.place.events.EventPlaceDao
import ly.david.data.room.relation.RoomRelationDao
import ly.david.ui.stats.EventStats
import ly.david.ui.stats.RelationsStats
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class PlaceStatsViewModel(
    override val relationDao: RoomRelationDao,
    private val eventPlaceDao: EventPlaceDao,
) : ViewModel(),
    RelationsStats,
    EventStats {

    override suspend fun getTotalLocalEvents(entityId: String): Int =
        eventPlaceDao.getNumberOfEventsByPlace(entityId)
}
