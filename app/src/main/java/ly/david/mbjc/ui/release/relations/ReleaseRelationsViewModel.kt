package ly.david.mbjc.ui.release.relations

import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import ly.david.data.network.api.LookupApi
import ly.david.data.network.api.MusicBrainzApiService
import ly.david.data.persistence.relation.RelationDao
import ly.david.data.persistence.relation.RelationRoomModel
import ly.david.data.persistence.relation.toRelationRoomModel
import ly.david.mbjc.ui.relation.RelationViewModel

@HiltViewModel
internal class ReleaseRelationsViewModel @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val relationDao: RelationDao
) : RelationViewModel(relationDao) {
    override suspend fun lookupRelationsAndStore(resourceId: String, forceRefresh: Boolean) {

        val releaseMusicBrainzModel = musicBrainzApiService.lookupRelease(
            releaseId = resourceId,
            include = LookupApi.INC_ALL_RELATIONS
        )

        val relations = mutableListOf<RelationRoomModel>()
        releaseMusicBrainzModel.relations?.forEachIndexed { index, relationMusicBrainzModel ->
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
