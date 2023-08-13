package ly.david.data.room.work

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.room.BaseDao

@Dao
abstract class WorkDao : BaseDao<WorkRoomModel>() {

    @Transaction
    @Query("SELECT * FROM work WHERE id = :workId")
    abstract suspend fun getWork(workId: String): WorkWithAllData?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertAllAttributes(entities: List<WorkAttributeRoomModel>)
}
