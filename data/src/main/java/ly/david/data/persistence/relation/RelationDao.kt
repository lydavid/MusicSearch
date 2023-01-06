package ly.david.data.persistence.relation

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.BaseDao

@Dao
abstract class RelationDao : BaseDao<RelationRoomModel>() {

    // TODO: right now we're unsure if there's collision with mbid between resources
    //  If we we find a resource shows the wrong relationships, then there must have been a collision.
    //  We tried using OnConflictStrategy.ABORT, but that would mess up refreshing relationships.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun markResourceHasRelations(hasRelationsRoomModel: HasRelationsRoomModel): Long

    @Query("SELECT * FROM has_relations WHERE resource_id = :resourceId")
    abstract suspend fun getHasRelationsModel(resourceId: String): HasRelationsRoomModel?

    @Transaction
    @Query(
        """
            SELECT *
            FROM relation
            WHERE resource_id = :resourceId
            ORDER BY linked_resource, label, `order`
        """
    )
    abstract fun getRelationsForResource(
        resourceId: String
    ): PagingSource<Int, RelationRoomModel>

    @Query(
        """
        DELETE FROM relation WHERE resource_id = :resourceId
        """
    )
    abstract suspend fun deleteRelationsByResource(resourceId: String)

    // region Relationship stats
    /**
     * Null means we have not successfully fetched its relationships.
     */
    @Query(
        """
        SELECT
            CASE 
                WHEN (SELECT has_relations FROM has_relations WHERE resource_id = :resourceId) IS NULL THEN
                    NULL
                ELSE
                    (SELECT COUNT(*)
                    FROM relation
                    WHERE resource_id = :resourceId)
            END
        AS numRelations
    """
    )
    abstract suspend fun getNumberOfRelationsByResource(resourceId: String): Int?

    @Query(
        """
        SELECT linked_resource, COUNT(resource_id) as count
        FROM relation
        WHERE resource_id = :resourceId
        GROUP BY linked_resource
    """
    )
    abstract suspend fun getCountOfEachRelationshipType(resourceId: String): List<RelationTypeCount>
    // endregion

    // region BrowseResourceCount
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertBrowseResourceCount(browseResourceCount: BrowseResourceCount): Long

    @Query(
        """
            SELECT *
            FROM browse_resource_count
            WHERE resource_id = :resourceId AND browse_resource = :browseResource
        """
    )
    abstract suspend fun getBrowseResourceCount(
        resourceId: String,
        browseResource: MusicBrainzResource
    ): BrowseResourceCount?

    @Query(
        """
            UPDATE browse_resource_count
            SET local_count = :localCount
            WHERE resource_id = :resourceId AND browse_resource = :browseResource
        """
    )
    abstract suspend fun updateLocalCountForResource(
        resourceId: String,
        browseResource: MusicBrainzResource,
        localCount: Int
    )

    @Transaction
    open suspend fun incrementLocalCountForResource(
        resourceId: String,
        browseResource: MusicBrainzResource,
        additionalOffset: Int
    ) {
        val currentOffset = getBrowseResourceCount(resourceId, browseResource)?.localCount ?: 0
        updateLocalCountForResource(resourceId, browseResource, currentOffset + additionalOffset)
    }

    @Query(
        """
        DELETE FROM browse_resource_count
        WHERE resource_id = :resourceId AND browse_resource = :browseResource
        """
    )
    abstract suspend fun deleteBrowseResourceCountByResource(resourceId: String, browseResource: MusicBrainzResource)
    // endregion
}
