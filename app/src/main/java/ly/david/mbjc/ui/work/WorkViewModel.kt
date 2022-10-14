package ly.david.mbjc.ui.work

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.mbjc.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.relation.RelationViewModel

@HiltViewModel
internal class WorkViewModel @Inject constructor(
    private val workRepository: WorkRepository,
    relationDao: RelationDao
) : RelationViewModel(relationDao) {

    suspend fun lookupWork(workId: String) = workRepository.lookupWork(workId).also {
        fetchRelationsForResource(it.id)
    }
}
