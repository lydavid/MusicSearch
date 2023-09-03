package ly.david.data.domain.relation

import javax.inject.Inject
import javax.inject.Singleton
import ly.david.data.musicbrainz.RelationMusicBrainzModel
import ly.david.data.room.relation.HasUrls
import ly.david.data.room.relation.RelationDao
import ly.david.data.room.relation.RelationRoomModel
import ly.david.data.room.relation.toRelationRoomModel

@Singleton
class RelationRepository @Inject constructor(
    private val relationDao: RelationDao,
) {
    suspend fun hasUrlsBeenSavedFor(entityId: String): Boolean =
        relationDao.hasUrls(entityId)?.hasUrls == true

    suspend fun insertAllRelations(entityId: String, relationMusicBrainzModels: List<RelationMusicBrainzModel>?) {
        val relationRoomModels = mutableListOf<RelationRoomModel>()
        relationMusicBrainzModels?.forEachIndexed { index, relationMusicBrainzModel ->
            relationMusicBrainzModel.toRelationRoomModel(
                entityId = entityId,
                order = index
            )?.let { relationRoomModel ->
                relationRoomModels.add(relationRoomModel)
            }
        }
        relationDao.insertAll(relationRoomModels)
        relationDao.markEntityHasUrls(
            hasUrls = HasUrls(
                entityId = entityId,
                hasUrls = true
            )
        )
    }
}
