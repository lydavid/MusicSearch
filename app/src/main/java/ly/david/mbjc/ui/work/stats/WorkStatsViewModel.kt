package ly.david.mbjc.ui.work.stats

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.room.relation.RelationDao
import ly.david.data.room.work.recordings.RecordingWorkDao
import ly.david.mbjc.ui.stats.RecordingStats
import ly.david.mbjc.ui.stats.RelationsStats

@HiltViewModel
internal class WorkStatsViewModel @Inject constructor(
    override val relationDao: RelationDao,
    private val recordingWorkDao: RecordingWorkDao
) : ViewModel(), RelationsStats, RecordingStats {

    override suspend fun getTotalLocalRecordings(entityId: String) =
        recordingWorkDao.getNumberOfRecordingsByWork(entityId)
}
