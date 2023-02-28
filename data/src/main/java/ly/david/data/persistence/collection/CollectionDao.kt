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
    abstract fun getAllCollections(): PagingSource<Int, CollectionRoomModel>

    @Transaction
    @Query(
        """
        SELECT * 
        FROM collection
        WHERE name LIKE :query
        """
    )
    abstract fun getAllCollectionsFiltered(query: String): PagingSource<Int, CollectionRoomModel>


    // TODO: "resource" or "entity"? for now, just call it resource, do renaming later. delete this todo
    // TODO: add artist. Would need .insert for CollectionArtistRoomModel with artist.id and
    //  collection id (from getAllCollectionsOfType -> when we click on a collection to add to, we should have its id)
//    abstract suspend fun addEntityToCollection(mbid: String, resource: MusicBrainzResource)
    // TODO: instead of CollectionArtistRoomModel, CollectionReleaseRoomModel,
    //  can we just have one to make many-to-many between collection and mb resource?
    //  would lose on FK only. This could be a good thing, because deleting a resource should not delete it from collections

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
    abstract fun getAllCollectionsOfType(resource: MusicBrainzResource): PagingSource<Int, CollectionRoomModel>
}
