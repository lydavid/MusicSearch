package ly.david.mbjc.ui.release.stats

import androidx.lifecycle.ViewModel
import ly.david.data.room.relation.RelationDao
import ly.david.ui.stats.RelationsStats
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
internal class ReleaseStatsViewModel(
    override val relationDao: RelationDao,
) : ViewModel(),
    RelationsStats
