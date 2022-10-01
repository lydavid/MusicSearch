package ly.david.mbjc.ui.label.relations

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.mbjc.data.network.Lookup
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.persistence.label.LabelDao
import ly.david.mbjc.data.persistence.relation.RelationDao
import ly.david.mbjc.data.persistence.relation.RelationRoomModel
import ly.david.mbjc.data.persistence.relation.toRelationRoomModel
import ly.david.mbjc.ui.relation.RelationViewModel

@HiltViewModel
internal class LabelRelationsViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val labelDao: LabelDao,
    private val relationDao: RelationDao
) : RelationViewModel(relationDao) {

    override suspend fun hasRelationsBeenStored(): Boolean {
        return labelDao.getLabel(resourceId.value)?.hasDefaultRelations == true
    }

    override suspend fun lookupRelationsAndStore(resourceId: String) {

        val labelMusicBrainzModel = musicBrainzApiService.lookupLabel(
            labelId = resourceId,
            include = Lookup.INC_ALL_RELATIONS
        )

        val relations = mutableListOf<RelationRoomModel>()
        labelMusicBrainzModel.relations?.forEachIndexed { index, relationMusicBrainzModel ->
            relationMusicBrainzModel.toRelationRoomModel(
                resourceId = resourceId,
                order = index
            )?.let { relationRoomModel ->
                relations.add(relationRoomModel)
            }
        }
        relationDao.insertAll(relations)

        // Called last so that we only flag it after everything else succeeded.
        labelDao.setHasDefaultRelations(labelId = resourceId, hasDefaultRelations = true)
    }
}
