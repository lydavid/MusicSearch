package ly.david.data.room.area

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.room.BaseDao

@Dao
abstract class RoomAreaDao : BaseDao<AreaRoomModel>() {

    @Transaction
    @Query("SELECT * FROM area WHERE id = :areaId")
    abstract suspend fun getArea(areaId: String): AreaWithAllData?

    // We don't expect these to change.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertAllCountryCodes(iso31661s: List<CountryCode>)
}
