package ly.david.data.persistence.label

import androidx.room.Dao
import androidx.room.Query
import ly.david.data.persistence.BaseDao

@Dao
abstract class LabelDao : BaseDao<LabelRoomModel> {

    @Query("SELECT * FROM labels WHERE id = :labelId")
    abstract suspend fun getLabel(labelId: String): LabelRoomModel?

    @Query(
        """
        UPDATE labels 
        SET release_count = :releaseCount
        WHERE id = :labelId
        """
    )
    abstract suspend fun setReleaseCount(labelId: String, releaseCount: Int)
}
