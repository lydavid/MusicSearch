package ly.david.mbjc.ui.label.relations

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.mbjc.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.relation.RelationViewModel

@HiltViewModel
internal class LabelRelationsViewModel @Inject constructor(
    private val labelRelationsRepository: LabelRelationsRepository,
    relationDao: RelationDao
) : RelationViewModel(relationDao) {

    suspend fun lookupLabel(labelId: String) = labelRelationsRepository.lookupLabel(labelId).also {
        this.resourceId.value = it.id
    }
}
