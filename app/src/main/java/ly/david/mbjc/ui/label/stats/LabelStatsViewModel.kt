package ly.david.mbjc.ui.label.stats

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.persistence.label.ReleaseLabelDao
import ly.david.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.stats.RelationsStats
import ly.david.mbjc.ui.stats.ReleasesStats

@HiltViewModel
internal class LabelStatsViewModel @Inject constructor(
    override val relationDao: RelationDao,
    private val releaseLabelDao: ReleaseLabelDao
) : ViewModel(), RelationsStats, ReleasesStats {

    override suspend fun getTotalLocalReleases(resourceId: String): Int =
        releaseLabelDao.getNumberOfReleasesByLabel(resourceId)
}
