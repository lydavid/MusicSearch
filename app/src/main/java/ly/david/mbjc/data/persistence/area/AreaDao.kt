package ly.david.mbjc.data.persistence.area

import androidx.room.Dao
import androidx.room.Query
import ly.david.mbjc.data.persistence.AreaRoomModel
import ly.david.mbjc.data.persistence.BaseDao

@Dao
internal abstract class AreaDao : BaseDao<AreaRoomModel> {

    @Query("SELECT * FROM areas WHERE id = :areaId")
    abstract suspend fun getArea(areaId: String): AreaRoomModel?
}
