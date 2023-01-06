package ly.david.mbjc.ui.label.stats

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.label.ReleaseLabelDao
import ly.david.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.stats.RelationsStats

@HiltViewModel
internal class LabelStatsViewModel @Inject constructor(
    override val relationDao: RelationDao,
    private val releaseLabelDao: ReleaseLabelDao
) : ViewModel(), RelationsStats {

    suspend fun getTotalReleases(labelId: String) =
        relationDao.getBrowseResourceCount(labelId, MusicBrainzResource.RELEASE)?.remoteCount ?: 0

    suspend fun getNumberOfReleasesByLabel(labelId: String) =
        releaseLabelDao.getNumberOfReleasesByLabel(labelId)
}
