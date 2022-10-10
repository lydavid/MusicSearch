package ly.david.mbjc.ui.label.stats

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.mbjc.data.persistence.label.LabelDao
import ly.david.mbjc.data.persistence.label.ReleasesLabelsDao
import ly.david.mbjc.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.relation.stats.RelationsStats

@HiltViewModel
internal class LabelStatsViewModel @Inject constructor(
    override val relationDao: RelationDao,
    private val labelDao: LabelDao,
    private val releasesLabelsDao: ReleasesLabelsDao
) : ViewModel(), RelationsStats {

    suspend fun getTotalReleases(labelId: String) =
        labelDao.getLabel(labelId)?.releaseCount ?: 0

    suspend fun getNumberOfReleasesByLabel(labelId: String) =
        releasesLabelsDao.getNumberOfReleasesByLabel(labelId)
}
