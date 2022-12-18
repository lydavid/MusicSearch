package ly.david.data.persistence.series

import androidx.room.Dao
import androidx.room.Query
import ly.david.data.persistence.BaseDao

@Dao
abstract class SeriesDao : BaseDao<SeriesRoomModel>() {

    @Query("SELECT * FROM series WHERE id = :seriesId")
    abstract suspend fun getSeries(seriesId: String): SeriesRoomModel?
}
