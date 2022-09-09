package ly.david.mbjc.ui.area

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.mbjc.data.Area
import ly.david.mbjc.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.relation.RelationViewModel

@HiltViewModel
internal class AreaViewModel @Inject constructor(
    private val areaRepository: AreaRepository,
    relationDao: RelationDao
) : RelationViewModel(relationDao) {

    suspend fun getArea(areaId: String): Area = areaRepository.lookupArea(areaId).also {
        fetchRelationsForResource(it.id)
    }

    override suspend fun hasRelationsBeenStored(): Boolean {
        return super.hasRelationsBeenStored()
    }

    override suspend fun lookupRelationsAndStore(resourceId: String) {
        super.lookupRelationsAndStore(resourceId)
    }
}
