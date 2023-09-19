package ly.david.mbjc.ui.area.stats

import androidx.lifecycle.ViewModel
import ly.david.data.room.area.releases.ReleaseCountryDao
import ly.david.data.room.relation.RoomRelationDao
import ly.david.musicsearch.data.database.dao.AreaPlaceDao
import ly.david.ui.stats.PlacesStats
import ly.david.ui.stats.RelationsStats
import ly.david.ui.stats.ReleasesStats
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class AreaStatsViewModel(
    override val relationDao: RoomRelationDao,
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
