package ly.david.musicsearch.shared.feature.details.area.stats

import androidx.lifecycle.ViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import ly.david.musicsearch.core.models.network.MusicBrainzEntity
import ly.david.musicsearch.data.database.dao.AreaPlaceDao
import ly.david.musicsearch.data.database.dao.ReleaseCountryDao
import ly.david.musicsearch.domain.browse.usecase.ObserveBrowseEntityCount
import ly.david.musicsearch.domain.relation.usecase.GetCountOfEachRelationshipTypeUseCase
import ly.david.musicsearch.feature.stats.PlaceStats
import ly.david.musicsearch.feature.stats.ReleaseStats
import ly.david.musicsearch.feature.stats.Stats
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class AreaStatsViewModel(
    private val getCountOfEachRelationshipTypeUseCase: GetCountOfEachRelationshipTypeUseCase,
    private val observeBrowseEntityCount: ObserveBrowseEntityCount,
    private val releaseCountryDao: ReleaseCountryDao,
    private val areaPlaceDao: AreaPlaceDao,
) : ViewModel() {

    fun getStats(entityId: String): Flow<Stats> =
        combine(
            getCountOfEachRelationshipTypeUseCase(entityId),
            observeBrowseEntityCount(entityId, MusicBrainzEntity.RELEASE),
            releaseCountryDao.getNumberOfReleasesByCountry(entityId),
            observeBrowseEntityCount(entityId, MusicBrainzEntity.PLACE),
            areaPlaceDao.getNumberOfPlacesByArea(entityId),
        ) { relationTypeCounts, browseReleaseCount, localReleases, browsePlaceCount, localPlaces ->
            Stats(
                totalRelations = relationTypeCounts.sumOf { it.count },
                relationTypeCounts = relationTypeCounts.toImmutableList(),
                releaseStats = ReleaseStats(
                    totalRemote = browseReleaseCount?.remoteCount,
                    totalLocal = localReleases,
                ),
                placeStats = PlaceStats(
                    totalRemote = browsePlaceCount?.remoteCount,
                    totalLocal = localPlaces,
                ),
            )
        }
            .distinctUntilChanged()
}
