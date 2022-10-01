package ly.david.mbjc.ui.label.stats

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.mbjc.data.persistence.relation.RelationDao

@HiltViewModel
internal class LabelStatsViewModel @Inject constructor(
    private val relationDao: RelationDao
) : ViewModel() {

    suspend fun getNumberOfRelationsByResource(resourceId: String) =
        relationDao.getNumberOfRelationsByResource(resourceId)

    suspend fun getCountOfEachRelationshipType(resourceId: String) =
        relationDao.getCountOfEachRelationshipType(resourceId)
}
