package ly.david.data.room.relation

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.core.network.MusicBrainzEntity
import ly.david.data.room.BaseDao

@Dao
abstract class RoomRelationDao : BaseDao<RelationRoomModel>() {

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
