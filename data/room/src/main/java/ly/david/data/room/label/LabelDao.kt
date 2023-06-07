package ly.david.data.room.label

import androidx.room.Dao
import androidx.room.Query
import ly.david.data.room.BaseDao

@Dao
abstract class LabelDao : BaseDao<LabelRoomModel>() {

    @Query("SELECT * FROM label WHERE id = :labelId")
    abstract suspend fun getLabel(labelId: String): LabelRoomModel?
}
