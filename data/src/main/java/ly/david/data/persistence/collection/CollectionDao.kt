package ly.david.data.persistence.collection

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.network.MusicBrainzResource
import ly.david.data.persistence.BaseDao

@Dao
abstract class CollectionDao : BaseDao<CollectionRoomModel>() {

    @Transaction
    @Query(
        """
        SELECT * FROM collection
        WHERE id IN
          (SELECT id FROM collection
          WHERE NOT is_remote AND :showLocal
          UNION
          SELECT id FROM collection
          WHERE is_remote AND :showRemote)
        AND name LIKE :query
        """
    )
    abstract fun getAllCollectionsFiltered(
        showLocal: Boolean = true,
        showRemote: Boolean = true,
        query: String
    ): PagingSource<Int, CollectionWithEntities>

    @Transaction
    @Query(
        """
        SELECT * 
        FROM collection
        WHERE entity = :resource
        ORDER BY name
        """
    )
    abstract fun getAllCollectionsOfType(resource: MusicBrainzResource): PagingSource<Int, CollectionWithEntities>

    @Query(
        """
        DELETE FROM collection 
        WHERE is_remote
        """
    )
    abstract suspend fun deleteMusicBrainzCollections()

    @Query(
        """
        SELECT * 
        FROM collection
        WHERE id = :id
    """
    )
    abstract suspend fun getCollection(id: String): CollectionRoomModel

    @Transaction
    @Query(
        """
        SELECT * 
        FROM collection
        WHERE id = :id
    """
    )
    abstract suspend fun getCollectionWithEntities(id: String): CollectionWithEntities?
}
