package ly.david.mbjc.data.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
internal abstract class RelationDao : BaseDao<RelationRoomModel> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertAllIgnoreDuplicates(entities: List<RelationRoomModel>)
}
