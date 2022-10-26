package ly.david.mbjc.ui.release.stats

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.relation.stats.RelationsStats

@HiltViewModel
internal class ReleaseStatsViewModel @Inject constructor(
    override val relationDao: RelationDao
) : ViewModel(), RelationsStats {
}
