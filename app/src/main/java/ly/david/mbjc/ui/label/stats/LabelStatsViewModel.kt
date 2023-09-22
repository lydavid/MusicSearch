package ly.david.mbjc.ui.label.stats

import androidx.lifecycle.ViewModel
import ly.david.data.room.label.releases.RoomReleaseLabelDao
import ly.david.data.room.relation.RoomRelationDao
import ly.david.ui.stats.RelationsStats
import ly.david.ui.stats.ReleasesStats
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LabelStatsViewModel(
    override val relationDao: RoomRelationDao,
    private val releaseLabelDao: RoomReleaseLabelDao,
) : ViewModel(),
    ReleasesStats,
    RelationsStats {

    override suspend fun getTotalLocalReleases(entityId: String): Int =
        releaseLabelDao.getNumberOfReleasesByLabel(entityId)
}
