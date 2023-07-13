package ly.david.mbjc.ui.series.stats

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.room.relation.RelationDao
import ly.david.mbjc.ui.stats.RelationsStats

@HiltViewModel
internal class SeriesStatsViewModel @Inject constructor(
    override val relationDao: RelationDao,
) : ViewModel(),
    RelationsStats
