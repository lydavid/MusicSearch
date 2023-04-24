package ly.david.data.persistence.collection

import androidx.room.Dao
import androidx.room.Query
import ly.david.data.persistence.BaseDao

@Dao
abstract class CollectionEntityDao : BaseDao<CollectionEntityRoomModel>(),
    AreasByCollectionDao,
    ArtistsByCollectionDao,
    EventsByCollectionDao,
    InstrumentsByCollectionDao,
    LabelsByCollectionDao,
    PlacesByCollectionDao,
    RecordingsByCollectionDao,
    ReleasesByCollectionDao,
    ReleaseGroupsByCollectionDao,
    SeriesByCollectionDao,
    WorksByCollectionDao {

    @Query("DELETE FROM collection_entity WHERE id = :collectionId")
    abstract suspend fun deleteAllFromCollection(collectionId: String)

    @Query(
        """
            UPDATE collection_entity
            SET deleted = :deleted
            WHERE id = :collectionId AND entity_id = :collectableId
        """
    )
    abstract suspend fun markAsDeletedFromCollection(collectionId: String, collectableId: String, deleted: Boolean)

    @Query(
        """
          DELETE FROM collection_entity 
          WHERE id = :collectionId
          AND entity_id = :collectableId
    """
    )
    abstract suspend fun deleteFromCollection(collectionId: String, collectableId: String)
}

