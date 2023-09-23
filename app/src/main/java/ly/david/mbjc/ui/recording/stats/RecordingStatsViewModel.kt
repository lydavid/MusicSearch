package ly.david.mbjc.ui.recording.stats

import androidx.lifecycle.ViewModel
import ly.david.data.room.recording.releases.RoomRecordingReleaseDao
import ly.david.data.room.relation.RoomRelationDao
import ly.david.ui.stats.RelationsStats
import ly.david.ui.stats.ReleasesStats
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class RecordingStatsViewModel(
    override val relationDao: RoomRelationDao,
    private val recordingReleaseDao: RoomRecordingReleaseDao,
) : ViewModel(),
    RelationsStats,
    ReleasesStats {

    override suspend fun getTotalLocalReleases(entityId: String) =
        recordingReleaseDao.getNumberOfReleasesByRecording(entityId)
}
