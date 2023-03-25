package ly.david.data.persistence.collection

import androidx.room.Dao
import ly.david.data.persistence.BaseDao

@Dao
abstract class CollectionEntityDao : BaseDao<CollectionEntityRoomModel>(),
    CollectionRecordingDao,
    CollectionReleaseDao,
    CollectionReleaseGroupDao
