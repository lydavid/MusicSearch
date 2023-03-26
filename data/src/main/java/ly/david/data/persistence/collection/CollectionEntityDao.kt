package ly.david.data.persistence.collection

import androidx.room.Dao
import androidx.room.Query
import ly.david.data.persistence.BaseDao

@Dao
abstract class CollectionEntityDao : BaseDao<CollectionEntityRoomModel>(),
    EventsByCollectionDao,
    PlacesByCollectionDao,
    RecordingsByCollectionDao,
    ReleasesByCollectionDao,
    ReleaseGroupsByCollectionDao {

    @Query("DELETE FROM collection_entity WHERE id = :collectionId")
    abstract suspend fun deleteCollectionEntityLinks(collectionId: String)
}
