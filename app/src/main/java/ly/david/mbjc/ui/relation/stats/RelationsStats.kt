package ly.david.mbjc.ui.relation.stats

import ly.david.mbjc.data.persistence.relation.RelationDao

internal interface RelationsStats {
    val relationDao: RelationDao

    suspend fun getNumberOfRelationsByResource(resourceId: String) =
        relationDao.getNumberOfRelationsByResource(resourceId)

    suspend fun getCountOfEachRelationshipType(resourceId: String) =
        relationDao.getCountOfEachRelationshipType(resourceId)
}
