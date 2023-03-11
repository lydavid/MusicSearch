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
    @Query("SELECT * FROM collection")
    abstract fun getAllCollections(): PagingSource<Int, CollectionWithEntities>

    @Transaction
    @Query(
        """
        SELECT * 
        FROM collection
        WHERE name LIKE :query
        """
    )
    abstract fun getAllCollectionsFiltered(query: String): PagingSource<Int, CollectionWithEntities>


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
        WHERE mbid IS NOT NULL
        """
    )
    abstract suspend fun deleteMusicBrainzCollections()
}
