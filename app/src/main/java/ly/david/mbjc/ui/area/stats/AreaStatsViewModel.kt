package ly.david.mbjc.ui.area.stats

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.area.AreaDao
import ly.david.data.persistence.area.ReleasesCountriesDao
import ly.david.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.relation.stats.RelationsStats

@HiltViewModel
internal class AreaStatsViewModel @Inject constructor(
    override val relationDao: RelationDao,
    private val areaDao: AreaDao,
    private val releasesCountriesDao: ReleasesCountriesDao
) : ViewModel(), RelationsStats {

    suspend fun getTotalReleases(areaId: String) =
        relationDao.getBrowseResourceCount(areaId, MusicBrainzResource.RELEASE)?.remoteCount ?: 0

    suspend fun getNumberOfReleasesByLabel(areaId: String) =
        releasesCountriesDao.getNumberOfReleasesByCountry(areaId)
}
