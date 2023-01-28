package ly.david.mbjc.ui.recording.stats

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.persistence.recording.RecordingReleaseDao
import ly.david.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.stats.RelationsStats
import ly.david.mbjc.ui.stats.ReleasesStats

@HiltViewModel
internal class RecordingStatsViewModel @Inject constructor(
    override val relationDao: RelationDao,
    private val recordingReleaseDao: RecordingReleaseDao
) : ViewModel(), RelationsStats, ReleasesStats {

    override suspend fun getTotalLocalReleases(resourceId: String) =
        recordingReleaseDao.getNumberOfReleasesByRecording(resourceId)
}
