package ly.david.mbjc.ui.label.relations

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.network.api.Lookup
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.relation.RelationRoomModel
import ly.david.data.persistence.relation.toRelationRoomModel
import ly.david.mbjc.ui.relation.RelationViewModel

@HiltViewModel
internal class LabelRelationsViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val relationDao: RelationDao
) : RelationViewModel(relationDao) {
    override suspend fun lookupRelationsAndStore(resourceId: String, forceRefresh: Boolean) {

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

        markResourceHasRelations()
    }
}
