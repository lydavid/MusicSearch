package ly.david.mbjc.ui.area.stats

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.persistence.area.AreaDao
import ly.david.data.persistence.area.ReleasesCountriesDao
import ly.david.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.stats.RelationsStats
import ly.david.mbjc.ui.stats.ReleasesStats

@HiltViewModel
internal class AreaStatsViewModel @Inject constructor(
    override val relationDao: RelationDao,
    private val areaDao: AreaDao,
    private val releasesCountriesDao: ReleasesCountriesDao
) : ViewModel(), RelationsStats, ReleasesStats {

    override suspend fun getTotalLocalReleases(resourceId: String) =
        releasesCountriesDao.getNumberOfReleasesByCountry(resourceId)
}
