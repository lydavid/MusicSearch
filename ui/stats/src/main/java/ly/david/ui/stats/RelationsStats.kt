package ly.david.ui.stats

import ly.david.data.room.relation.RelationDao

/**
 * Stats about relations.
 */
interface RelationsStats {
    val relationDao: RelationDao

    suspend fun getNumberOfRelationsByEntity(entityId: String) =
        relationDao.getNumberOfRelationsByEntity(entityId)

    suspend fun getCountOfEachRelationshipType(entityId: String) =
        relationDao.getCountOfEachRelationshipType(entityId)
}
