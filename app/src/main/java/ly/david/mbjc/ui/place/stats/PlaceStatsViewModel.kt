package ly.david.mbjc.ui.place.stats

import androidx.lifecycle.ViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import ly.david.musicsearch.data.core.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.dao.EventPlaceDao
import ly.david.musicsearch.domain.browse.GetBrowseEntityCountFlowUseCase
import ly.david.musicsearch.domain.relation.GetCountOfEachRelationshipTypeUseCase
import ly.david.ui.stats.EventStats
import ly.david.ui.stats.Stats
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class PlaceStatsViewModel(
    private val getCountOfEachRelationshipTypeUseCase: GetCountOfEachRelationshipTypeUseCase,
    private val getBrowseEntityCountFlowUseCase: GetBrowseEntityCountFlowUseCase,
    private val eventPlaceDao: EventPlaceDao,
) : ViewModel() {

    fun getStats(entityId: String): Flow<Stats> =
        combine(
            getCountOfEachRelationshipTypeUseCase(entityId),
            getBrowseEntityCountFlowUseCase(entityId, MusicBrainzEntity.EVENT),
            eventPlaceDao.getNumberOfEventsByPlace(entityId),
        ) { relationTypeCounts, browseEventCount, localEvents ->
            Stats(
                totalRelations = relationTypeCounts.sumOf { it.count },
                relationTypeCounts = relationTypeCounts.toImmutableList(),
                eventStats = EventStats(
                    totalRemote = browseEventCount?.remoteCount,
                    totalLocal = localEvents,
                ),
            )
        }
            .distinctUntilChanged()
}
