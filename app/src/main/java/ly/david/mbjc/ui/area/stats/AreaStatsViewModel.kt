package ly.david.mbjc.ui.area.stats

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.room.area.places.AreaPlaceDao
import ly.david.data.room.area.releases.ReleaseCountryDao
import ly.david.data.room.relation.RelationDao
import ly.david.ui.stats.PlacesStats
import ly.david.ui.stats.RelationsStats
import ly.david.ui.stats.ReleasesStats

@HiltViewModel
class AreaStatsViewModel @Inject constructor(
    override val relationDao: RelationDao,
    private val releaseCountryDao: ReleaseCountryDao,
    private val areaPlaceDao: AreaPlaceDao,
) : ViewModel(),
    RelationsStats,
    ReleasesStats,
    PlacesStats {

    override suspend fun getTotalLocalReleases(entityId: String): Int =
        releaseCountryDao.getNumberOfReleasesByCountry(entityId)

    override suspend fun getTotalLocalPlaces(entityId: String): Int =
        areaPlaceDao.getNumberOfPlacesByArea(entityId)
}
