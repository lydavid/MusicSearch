package ly.david.mbjc.ui.area.stats

import androidx.lifecycle.ViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.domain.browse.GetBrowseEntityCountFlowUseCase
import ly.david.data.domain.relation.GetCountOfEachRelationshipTypeUseCase
import ly.david.musicsearch.data.database.dao.AreaPlaceDao
import ly.david.musicsearch.data.database.dao.ReleaseCountryDao
import ly.david.ui.stats.Stats
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class AreaStatsViewModel(
    private val getCountOfEachRelationshipTypeUseCase: GetCountOfEachRelationshipTypeUseCase,
    private val getBrowseEntityCountFlowUseCase: GetBrowseEntityCountFlowUseCase,
    private val releaseCountryDao: ReleaseCountryDao,
    private val areaPlaceDao: AreaPlaceDao,
) : ViewModel() {

    fun getStats(entityId: String): Flow<Stats> =
        combine(
            getCountOfEachRelationshipTypeUseCase(entityId),
            getBrowseEntityCountFlowUseCase(entityId, MusicBrainzEntity.RELEASE),
            releaseCountryDao.getNumberOfReleasesByCountry(entityId),
            getBrowseEntityCountFlowUseCase(entityId, MusicBrainzEntity.PLACE),
            areaPlaceDao.getNumberOfPlacesByArea(entityId),
        ) { relationTypeCounts, browseReleaseCount, localReleases, browsePlaceCount, localPlaces ->
            Stats(
                totalRelations = relationTypeCounts.sumOf { it.count },
                relationTypeCounts = relationTypeCounts.toImmutableList(),
                totalRemoteReleases = browseReleaseCount?.remoteCount,
                totalLocalReleases = localReleases,
                totalRemotePlaces = browsePlaceCount?.remoteCount,
                totalLocalPlaces = localPlaces,
            )
        }
            .distinctUntilChanged()
}
