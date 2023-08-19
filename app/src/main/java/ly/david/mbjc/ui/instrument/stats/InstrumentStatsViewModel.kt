package ly.david.mbjc.ui.instrument.stats

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.room.relation.RelationDao
import ly.david.ui.stats.RelationsStats

@HiltViewModel
class InstrumentStatsViewModel @Inject constructor(
    override val relationDao: RelationDao,
) : ViewModel(),
    RelationsStats
