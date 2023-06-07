package ly.david.mbjc.ui.stats

import ly.david.data.room.relation.RelationDao

/**
 * Stats about relations.
 */
interface RelationsStats {
    val relationDao: RelationDao

    suspend fun getNumberOfRelationsByResource(resourceId: String) =
        relationDao.getNumberOfRelationsByResource(resourceId)

    suspend fun getCountOfEachRelationshipType(resourceId: String) =
        relationDao.getCountOfEachRelationshipType(resourceId)
}
