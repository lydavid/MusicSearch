package ly.david.mbjc.ui.releasegroup.relations

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.mbjc.data.network.Lookup
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.persistence.relation.RelationDao
import ly.david.mbjc.data.persistence.relation.RelationRoomModel
import ly.david.mbjc.data.persistence.relation.toRelationRoomModel
import ly.david.mbjc.data.persistence.releasegroup.ReleaseGroupDao
import ly.david.mbjc.ui.relation.RelationViewModel

@HiltViewModel
internal class ReleaseGroupRelationsViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val releaseGroupDao: ReleaseGroupDao,
    private val relationDao: RelationDao
) : RelationViewModel(relationDao) {

//    suspend fun lookupReleaseGroupRelations(releaseGroupId: String): ReleaseGroup =
//        releaseGroupRelationsRepository.lookupReleaseGroupRelations(releaseGroupId).also {
//            this.resourceId.value = it.id
//        }

    override suspend fun hasRelationsBeenStored(): Boolean {
        return releaseGroupDao.getReleaseGroup(resourceId.value)?.hasDefaultRelations == true
    }

    override suspend fun lookupRelationsAndStore(resourceId: String) {
        // Use cached model.
//        val releaseGroupRoomModel = releaseGroupDao.getReleaseGroup(resourceId)
//
//        if (releaseGroupRoomModel != null && releaseGroupRoomModel.hasDefaultRelations) {
//            return releaseGroupRoomModel
//        }

        val releaseGroupMusicBrainzModel = musicBrainzApiService.lookupReleaseGroup(
            releaseGroupId = resourceId,
            include = Lookup.INC_ALL_RELATIONS
        )

//        if (releaseGroupRoomModel == null) {
//            releaseGroupDao.insert(releaseGroupMusicBrainzModel.toReleaseGroupRoomModel(hasDefaultRelations = true))
//        } else {
            releaseGroupDao.setHasDefaultRelations(releaseGroupId = resourceId, hasDefaultRelations = true)
//        }

        val relations = mutableListOf<RelationRoomModel>()
        releaseGroupMusicBrainzModel.relations?.forEachIndexed { index, relationMusicBrainzModel ->
            relationMusicBrainzModel.toRelationRoomModel(
                resourceId = resourceId,
                order = index
            )?.let { relationRoomModel ->
                relations.add(relationRoomModel)
            }
        }
        relationDao.insertAll(relations)
    }
}
