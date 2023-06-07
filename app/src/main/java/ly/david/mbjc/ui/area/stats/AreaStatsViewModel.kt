package ly.david.mbjc.ui.area.stats

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.room.area.places.AreaPlaceDao
import ly.david.data.room.area.releases.ReleaseCountryDao
import ly.david.data.room.relation.RelationDao
import ly.david.mbjc.ui.stats.PlacesStats
import ly.david.mbjc.ui.stats.RelationsStats
import ly.david.mbjc.ui.stats.ReleasesStats

@HiltViewModel
class AreaStatsViewModel @Inject constructor(
    override val relationDao: RelationDao,
    private val releaseCountryDao: ReleaseCountryDao,
    private val areaPlaceDao: AreaPlaceDao
) : ViewModel(), RelationsStats, ReleasesStats, PlacesStats {

    override suspend fun getTotalLocalReleases(resourceId: String): Int =
        releaseCountryDao.getNumberOfReleasesByCountry(resourceId)

    override suspend fun getTotalLocalPlaces(resourceId: String): Int =
        areaPlaceDao.getNumberOfPlacesByArea(resourceId)
}
