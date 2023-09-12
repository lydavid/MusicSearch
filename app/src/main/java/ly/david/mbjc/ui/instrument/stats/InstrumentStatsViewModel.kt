package ly.david.mbjc.ui.instrument.stats

import androidx.lifecycle.ViewModel
import ly.david.data.room.relation.RelationDao
import ly.david.ui.stats.RelationsStats
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class InstrumentStatsViewModel(
    override val relationDao: RelationDao,
) : ViewModel(),
    RelationsStats
