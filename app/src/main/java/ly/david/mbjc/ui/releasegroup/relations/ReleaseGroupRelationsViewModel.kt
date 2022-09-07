package ly.david.mbjc.ui.releasegroup.relations

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.mbjc.data.ReleaseGroup
import ly.david.mbjc.data.persistence.relation.RelationDao
import ly.david.mbjc.ui.relation.RelationViewModel

@HiltViewModel
internal class ReleaseGroupRelationsViewModel @Inject constructor(
    private val releaseGroupRelationsRepository: ReleaseGroupRelationsRepository,
    relationDao: RelationDao
) : RelationViewModel(relationDao) {

    suspend fun lookupReleaseGroupRelations(releaseGroupId: String): ReleaseGroup =
        releaseGroupRelationsRepository.lookupReleaseGroupRelations(releaseGroupId).also {
            this.resourceId.value = it.id
        }
}
