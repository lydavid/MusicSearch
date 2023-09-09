package ly.david.mbjc.ui.work.stats

import androidx.lifecycle.ViewModel
import ly.david.data.room.relation.RelationDao
import ly.david.data.room.work.recordings.RecordingWorkDao
import ly.david.ui.stats.RecordingStats
import ly.david.ui.stats.RelationsStats
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class WorkStatsViewModel(
    override val relationDao: RelationDao,
    private val recordingWorkDao: RecordingWorkDao,
) : ViewModel(),
    RelationsStats,
    RecordingStats {

    override suspend fun getTotalLocalRecordings(entityId: String) =
        recordingWorkDao.getNumberOfRecordingsByWork(entityId)
}
