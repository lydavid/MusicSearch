package ly.david.data.persistence.collection

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
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


    // TODO: can we compare types due to typeconverter?
    // TODO: paged? should be able to if bottom sheet presenting this is lazy column
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

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertEntityIntoCollection(collectionEntityRoomModel: CollectionEntityRoomModel): Long
}
