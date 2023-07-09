package ly.david.mbjc.ui.label.stats

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.room.label.releases.ReleaseLabelDao
import ly.david.data.room.relation.RelationDao
import ly.david.mbjc.ui.stats.RelationsStats
import ly.david.mbjc.ui.stats.ReleasesStats

@HiltViewModel
class LabelStatsViewModel @Inject constructor(
    override val relationDao: RelationDao,
    private val releaseLabelDao: ReleaseLabelDao
) : ViewModel(), ReleasesStats, RelationsStats {

    override suspend fun getTotalLocalReleases(entityId: String): Int =
        releaseLabelDao.getNumberOfReleasesByLabel(entityId)
}
