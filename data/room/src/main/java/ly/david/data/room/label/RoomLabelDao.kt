package ly.david.data.room.label

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.room.BaseDao

@Dao
abstract class RoomLabelDao : BaseDao<LabelRoomModel>() {

    @Transaction
    @Query("SELECT * FROM label WHERE id = :labelId")
    abstract suspend fun getLabel(labelId: String): LabelWithAllData?
}
