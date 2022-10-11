package ly.david.mbjc.data.persistence.area

import androidx.room.Dao
import androidx.room.Query
import ly.david.mbjc.data.persistence.BaseDao

@Dao
internal abstract class AreaDao : BaseDao<AreaRoomModel> {

    @Query("SELECT * FROM areas WHERE id = :areaId")
    abstract suspend fun getArea(areaId: String): AreaRoomModel?

    @Query(
        """
        UPDATE areas 
        SET release_count = :releaseCount
        WHERE id = :areaId
        """
    )
    abstract suspend fun setReleaseCount(areaId: String, releaseCount: Int)
}
