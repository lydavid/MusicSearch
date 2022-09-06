package ly.david.mbjc.ui.area

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.mbjc.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.relation.RelationViewModel

@HiltViewModel
internal class AreaViewModel @Inject constructor(
    private val areaRepository: AreaRepository,
    relationDao: RelationDao
) : RelationViewModel(relationDao) {

    suspend fun lookupArea(areaId: String) = areaRepository.lookupArea(areaId).also {
        this.resourceId.value = it.id
    }
}
