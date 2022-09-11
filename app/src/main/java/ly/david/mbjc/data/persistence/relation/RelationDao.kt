package ly.david.mbjc.data.persistence.relation

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import ly.david.mbjc.data.persistence.BaseDao

@Dao
internal abstract class RelationDao : BaseDao<RelationRoomModel> {

    @Query(
        """
            SELECT *
            FROM relations
            WHERE resource_id = :resourceId
            ORDER BY `order`
        """
    )
    abstract fun getRelationsForResource(
        resourceId: String
    ): PagingSource<Int, RelationRoomModel>

    @Query(
        """
        DELETE FROM relations WHERE resource_id = :resourceId
        """
    )
    abstract suspend fun deleteRelationsByResource(resourceId: String)

    @Query(
        """
        SELECT IFNULL(
            (SELECT COUNT(*)
            FROM relations
            WHERE resource_id = :resourceId
            ),
            0
        ) AS count
    """
    )
    abstract suspend fun getNumberOfRelationsByResource(resourceId: String): Int

    @Query(
        """
        SELECT linked_resource, COUNT(resource_id) as count
        FROM relations
        WHERE resource_id = :resourceId
        GROUP BY linked_resource
    """
    )
    abstract suspend fun getCountOfEachRelationshipType(resourceId: String): List<RelationTypeCount>
}
