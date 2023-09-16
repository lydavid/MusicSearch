package ly.david.ui.stats

import ly.david.data.room.relation.RoomRelationDao

// TODO: remove in favor of relationrepository
/**
 * Stats about relations.
 */
interface RelationsStats {
    val relationDao: RoomRelationDao

    suspend fun getNumberOfRelationsByEntity(entityId: String) =
        relationDao.getNumberOfRelationsByEntity(entityId)

    suspend fun getCountOfEachRelationshipType(entityId: String) =
        relationDao.getCountOfEachRelationshipType(entityId)
}
