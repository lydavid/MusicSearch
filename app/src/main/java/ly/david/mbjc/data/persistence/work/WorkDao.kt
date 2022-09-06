package ly.david.mbjc.data.persistence.work

import androidx.room.Dao
import androidx.room.Query
import ly.david.mbjc.data.persistence.BaseDao

@Dao
internal abstract class WorkDao : BaseDao<WorkRoomModel> {

    @Query("SELECT * FROM works WHERE id = :workId")
    abstract suspend fun getWork(workId: String): WorkRoomModel?
}
