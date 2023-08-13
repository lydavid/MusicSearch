package ly.david.data.room.relation

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.network.MusicBrainzEntity
import ly.david.data.room.BaseDao

@Dao
abstract class RelationDao : BaseDao<RelationRoomModel>() {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun markEntityHasRelations(hasRelations: HasRelations): Long

    @Query("SELECT * FROM has_relations WHERE entity_id = :entityId")
    abstract suspend fun hasRelations(entityId: String): HasRelations?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun markEntityHasUrls(hasUrls: HasUrls): Long

    @Query("SELECT * FROM has_urls WHERE entity_id = :entityId")
    abstract suspend fun hasUrls(entityId: String): HasUrls?

    // TODO: we're excluding urls from everywhere, but not showing urls in non-artist details
    @Transaction
    @Query(
        """
            SELECT *
            FROM relation
            WHERE entity_id = :entityId AND linked_entity != "url" AND
            (name LIKE :query OR disambiguation LIKE :query OR label LIKE :query OR
            attributes LIKE :query OR additional_info LIKE :query)
            ORDER BY linked_entity, label, `order`
        """
    )
    abstract fun getEntityRelationships(
        entityId: String,
        query: String = "%%",
    ): PagingSource<Int, RelationRoomModel>

    @Transaction
    @Query(
        """
            SELECT *
            FROM relation
            WHERE entity_id = :entityId AND linked_entity = "url" AND
            (name LIKE :query OR disambiguation LIKE :query OR label LIKE :query OR
            attributes LIKE :query OR additional_info LIKE :query)
            ORDER BY linked_entity, label, `order`
        """
    )
    abstract fun getEntityUrls(
        entityId: String,
        query: String = "%%",
    ): PagingSource<Int, RelationRoomModel>

    @Query(
        """
        DELETE FROM relation 
        WHERE entity_id = :entityId AND linked_entity != "url"
        """
    )
    abstract suspend fun deleteRelationshipsByEntity(entityId: String)

    @Query(
        """
        DELETE FROM relation 
        WHERE entity_id = :entityId AND linked_entity == "url"
        """
    )
    abstract suspend fun deleteUrlsByEntity(entityId: String)

    // region Relationship stats
    /**
     * Null means we have not successfully fetched its relationships.
     */
    @Query(
        """
        SELECT
            CASE 
                WHEN (SELECT has_relations FROM has_relations WHERE entity_id = :entityId) IS NULL THEN
                    NULL
                ELSE
                    (SELECT COUNT(*)
                    FROM relation
                    WHERE entity_id = :entityId)
            END
        AS numRelations
    """
    )
    abstract suspend fun getNumberOfRelationsByEntity(entityId: String): Int?

    @Query(
        """
        SELECT linked_entity, COUNT(entity_id) as count
        FROM relation
        WHERE entity_id = :entityId
        GROUP BY linked_entity
    """
    )
    abstract suspend fun getCountOfEachRelationshipType(entityId: String): List<RelationTypeCount>
    // endregion

    // region BrowseEntityCount
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertBrowseEntityCount(browseEntityCount: BrowseEntityCount): Long

    @Query(
        """
            SELECT *
            FROM browse_entity_count
            WHERE entity_id = :entityId AND browse_entity = :browseEntity
        """
    )
    abstract suspend fun getBrowseEntityCount(
        entityId: String,
        browseEntity: MusicBrainzEntity,
    ): BrowseEntityCount?

    @Query(
        """
            UPDATE browse_entity_count
            SET local_count = :localCount
            WHERE entity_id = :entityId AND browse_entity = :browseEntity
        """
    )
    abstract suspend fun updateLocalCountForEntity(
        entityId: String,
        browseEntity: MusicBrainzEntity,
        localCount: Int,
    )

    @Transaction
    open suspend fun incrementLocalCountForEntity(
        entityId: String,
        browseEntity: MusicBrainzEntity,
        additionalOffset: Int,
    ) {
        val currentOffset = getBrowseEntityCount(entityId, browseEntity)?.localCount ?: 0
        updateLocalCountForEntity(entityId, browseEntity, currentOffset + additionalOffset)
    }

    @Query(
        """
        DELETE FROM browse_entity_count
        WHERE entity_id = :entityId AND browse_entity = :browseEntity
        """
    )
    abstract suspend fun deleteBrowseEntityCountByEntity(entityId: String, browseEntity: MusicBrainzEntity)

    @Query(
        """
        DELETE FROM browse_entity_count
        WHERE entity_id IN
            (SELECT id FROM collection 
            WHERE is_remote)
        """
    )
    abstract suspend fun deleteAllBrowseEntityCountByRemoteCollections()
    // endregion
}
