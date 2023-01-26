package ly.david.mbjc.ui.stats

import ly.david.data.persistence.relation.RelationDao

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
