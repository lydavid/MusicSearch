package ly.david.data.persistence.work

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ly.david.data.persistence.BaseDao

@Dao
abstract class WorkDao : BaseDao<WorkRoomModel>() {

    @Query("SELECT * FROM works WHERE id = :workId")
    abstract suspend fun getWork(workId: String): WorkWithAttributes?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertAllAttributes(entities: List<WorkAttributeRoomModel>)
}
