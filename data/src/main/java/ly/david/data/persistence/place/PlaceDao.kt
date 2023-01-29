package ly.david.data.persistence.place

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.persistence.BaseDao

@Dao
abstract class PlaceDao : BaseDao<PlaceRoomModel>() {

    @Transaction
    @Query("SELECT * FROM place WHERE id = :placeId")
    abstract suspend fun getPlace(placeId: String): PlaceWithArea?
}
