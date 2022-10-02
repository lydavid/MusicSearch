package ly.david.mbjc.ui.releasegroup.relations

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.mbjc.data.network.Lookup
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.persistence.relation.RelationDao
import ly.david.mbjc.data.persistence.relation.RelationRoomModel
import ly.david.mbjc.data.persistence.relation.toRelationRoomModel
import ly.david.mbjc.ui.relation.RelationViewModel

@HiltViewModel
internal class ReleaseGroupRelationsViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val relationDao: RelationDao
) : RelationViewModel(relationDao) {
    override suspend fun lookupRelationsAndStore(resourceId: String) {

        val releaseGroupMusicBrainzModel = musicBrainzApiService.lookupReleaseGroup(
            releaseGroupId = resourceId,
            include = Lookup.INC_ALL_RELATIONS
        )

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

        markResourceHasRelations()
    }
}
