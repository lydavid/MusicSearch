package ly.david.mbjc.ui.label

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.mbjc.data.persistence.RelationDao
import ly.david.mbjc.ui.relation.RelationViewModel

@HiltViewModel
internal class LabelViewModel @Inject constructor(
    private val labelRepository: LabelRepository,
    relationDao: RelationDao
) : RelationViewModel(relationDao) {

    suspend fun lookupLabel(labelId: String) = labelRepository.lookupLabel(labelId).also {
        this.resourceId.value = it.id
    }
}
