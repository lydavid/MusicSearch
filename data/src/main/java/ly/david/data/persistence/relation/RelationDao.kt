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
            FROM relations
            WHERE resource_id = :resourceId
            ORDER BY linked_resource, label, `order`
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

    // TODO: it never actually returns null
    /**
     * Null means we have not tried fetching its relationships.
     */
    @Query(
        """
            SELECT COUNT(*)
            FROM relations
            WHERE resource_id = :resourceId
    """
    )
    abstract suspend fun getNumberOfRelationsByResource(resourceId: String): Int?

    @Query(
        """
        SELECT linked_resource, COUNT(resource_id) as count
        FROM relations
        WHERE resource_id = :resourceId
        GROUP BY linked_resource
    """
    )
    abstract suspend fun getCountOfEachRelationshipType(resourceId: String): List<RelationTypeCount>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertBrowseResource(browseResourceRoomModel: BrowseResourceOffset): Long

    @Query(
        """
            SELECT *
            FROM browse_resource_counts
            WHERE resource_id = :resourceId AND browse_resource = :browseResource
        """
    )
    abstract suspend fun getBrowseResourceOffset(resourceId: String, browseResource: MusicBrainzResource): BrowseResourceOffset?

    @Query(
        """
            UPDATE browse_resource_counts
            SET local_count = :localCount
            WHERE resource_id = :resourceId AND browse_resource = :browseResource
        """
    )
    abstract suspend fun updateLocalCountForResource(resourceId: String, browseResource: MusicBrainzResource, localCount: Int)

    @Transaction
    open suspend fun incrementOffsetForResource(resourceId: String, browseResource: MusicBrainzResource, additionalOffset: Int) {
        val currentOffset = getBrowseResourceOffset(resourceId, browseResource)?.localCount ?: 0
        updateLocalCountForResource(resourceId, browseResource, currentOffset + additionalOffset)
    }

    @Query(
        """
        DELETE FROM browse_resource_counts 
        WHERE resource_id = :resourceId AND browse_resource = :browseResource
        """
    )
    abstract suspend fun deleteBrowseResourceOffsetByResource(resourceId: String, browseResource: MusicBrainzResource)
}
