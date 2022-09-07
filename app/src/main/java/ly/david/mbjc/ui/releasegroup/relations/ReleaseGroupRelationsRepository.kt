package ly.david.mbjc.ui.releasegroup.relations

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.mbjc.data.ReleaseGroup
import ly.david.mbjc.data.network.Lookup
import ly.david.mbjc.data.network.MusicBrainzApiService
import ly.david.mbjc.data.persistence.relation.RelationDao
import ly.david.mbjc.data.persistence.relation.RelationRoomModel
import ly.david.mbjc.data.persistence.relation.toRelationRoomModel
import ly.david.mbjc.data.persistence.releasegroup.ReleaseGroupDao
import ly.david.mbjc.data.persistence.releasegroup.toReleaseGroupRoomModel

@Singleton
internal class ReleaseGroupRelationsRepository @Inject constructor(
    private val musicBrainzApiService: MusicBrainzApiService,
    private val releaseGroupDao: ReleaseGroupDao,
    private val relationDao: RelationDao,
) {
    private var releaseGroup: ReleaseGroup? = null

    suspend fun lookupReleaseGroupRelations(releaseGroupId: String): ReleaseGroup =
        releaseGroup ?: run {

            // Use cached model.
            val releaseGroupRoomModel = releaseGroupDao.getReleaseGroup(releaseGroupId)

            if (releaseGroupRoomModel != null && releaseGroupRoomModel.hasDefaultRelations) {
                return releaseGroupRoomModel
            }

            val releaseGroupMusicBrainzModel = musicBrainzApiService.lookupReleaseGroup(
                releaseGroupId = releaseGroupId,
                include = Lookup.INC_ALL_RELATIONS
            )

            if (releaseGroupRoomModel == null) {
                releaseGroupDao.insert(releaseGroupMusicBrainzModel.toReleaseGroupRoomModel(hasDefaultRelations = true))
            } else {
                releaseGroupDao.setHasDefaultRelations(releaseGroupId = releaseGroupId, hasDefaultRelations = true)
            }

            val relations = mutableListOf<RelationRoomModel>()
            releaseGroupMusicBrainzModel.relations?.forEachIndexed { index, relationMusicBrainzModel ->
                relationMusicBrainzModel.toRelationRoomModel(
                    resourceId = releaseGroupId,
                    order = index
                )?.let { relationRoomModel ->
                    relations.add(relationRoomModel)
                }
            }
            relationDao.insertAll(relations)

            releaseGroupMusicBrainzModel
        }
}
