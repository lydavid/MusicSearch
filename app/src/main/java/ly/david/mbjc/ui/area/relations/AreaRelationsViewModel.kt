package ly.david.mbjc.ui.area.relations

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.mbjc.data.network.Lookup
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.persistence.area.AreaDao
import ly.david.mbjc.data.persistence.area.toAreaRoomModel
import ly.david.mbjc.data.persistence.relation.RelationDao
import ly.david.mbjc.data.persistence.relation.RelationRoomModel
import ly.david.mbjc.data.persistence.relation.toRelationRoomModel
import ly.david.mbjc.ui.relation.RelationViewModel

@HiltViewModel
internal class AreaRelationsViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val areaDao: AreaDao,
    private val relationDao: RelationDao,
) : RelationViewModel(relationDao) {
    override suspend fun lookupRelationsAndStore(resourceId: String) {

        val areaMusicBrainzModel = musicBrainzApiService.lookupArea(
            areaId = resourceId,
            include = Lookup.INC_ALL_RELATIONS
        )

        // Also store this Area.
        if (areaDao.getArea(resourceId) == null) {
            areaDao.insert(areaMusicBrainzModel.toAreaRoomModel())
        }

        val relations = mutableListOf<RelationRoomModel>()
        areaMusicBrainzModel.relations?.forEachIndexed { index, relationMusicBrainzModel ->
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
