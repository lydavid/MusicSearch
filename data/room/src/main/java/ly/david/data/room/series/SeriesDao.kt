package ly.david.data.room.series

import androidx.room.Dao
import androidx.room.Query
import ly.david.data.room.BaseDao

@Dao
abstract class SeriesDao : BaseDao<SeriesRoomModel>() {

    @Query("SELECT * FROM series WHERE id = :seriesId")
    abstract suspend fun getSeries(seriesId: String): SeriesWithAllData?
}
