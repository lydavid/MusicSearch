package ly.david.data.persistence.area

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ly.david.data.persistence.BaseDao

@Dao
abstract class AreaDao : BaseDao<AreaRoomModel>() {

    @Query("SELECT * FROM areas WHERE id = :areaId")
    abstract suspend fun getArea(areaId: String): AreaRoomModel?

    // We don't expect these to change.
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertAllCountryCodes(iso31661s: List<Iso3166_1>)

    @Query("SELECT * FROM iso_3166_1 WHERE area_id = :areaId")
    abstract suspend fun getCountryCodes(areaId: String): List<Iso3166_1>?
}
