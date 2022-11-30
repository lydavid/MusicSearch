package ly.david.mbjc.ui.recording.stats

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.recording.ReleasesRecordingsDao
import ly.david.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.relation.stats.RelationsStats

@HiltViewModel
internal class RecordingStatsViewModel @Inject constructor(
    override val relationDao: RelationDao,
    private val releasesRecordingsDao: ReleasesRecordingsDao
) : ViewModel(), RelationsStats {

    suspend fun getTotalReleases(recordingId: String) =
        relationDao.getBrowseResourceCount(recordingId, MusicBrainzResource.RELEASE)?.remoteCount ?: 0

    suspend fun getNumberOfLocalReleasesByRecording(recordingId: String) =
        releasesRecordingsDao.getNumberOfReleasesByRecording(recordingId)
}
