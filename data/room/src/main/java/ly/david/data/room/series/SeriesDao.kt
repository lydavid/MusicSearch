package ly.david.data.room.series

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.room.BaseDao

@Dao
abstract class SeriesDao : BaseDao<SeriesRoomModel>() {

    @Transaction
    @Query("SELECT * FROM series WHERE id = :seriesId")
    abstract suspend fun getSeries(seriesId: String): SeriesWithAllData?
}
