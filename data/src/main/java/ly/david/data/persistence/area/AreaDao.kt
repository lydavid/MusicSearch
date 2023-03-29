package ly.david.data.persistence.area

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.persistence.BaseDao

@Dao
abstract class AreaDao : BaseDao<AreaRoomModel>() {

    @Transaction
    @Query("SELECT * FROM area WHERE id = :areaId")
    abstract suspend fun getArea(areaId: String): AreaWithCountryCodes?

    // We don't expect these to change.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertAllCountryCodes(iso31661s: List<CountryCode>)
}
