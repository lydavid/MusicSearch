package ly.david.mbjc.data.persistence.label

import androidx.room.Dao
import androidx.room.Query
import ly.david.mbjc.data.persistence.BaseDao
import ly.david.mbjc.data.persistence.LabelRoomModel

@Dao
internal abstract class LabelDao : BaseDao<LabelRoomModel> {

    @Query("SELECT * FROM labels WHERE id = :labelId")
    abstract suspend fun getLabel(labelId: String): LabelRoomModel?
}
