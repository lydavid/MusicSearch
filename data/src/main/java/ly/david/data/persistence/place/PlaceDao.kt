package ly.david.data.persistence.place

import androidx.room.Dao
import androidx.room.Query
import ly.david.data.persistence.BaseDao

@Dao
abstract class PlaceDao : BaseDao<PlaceRoomModel>() {

    @Query("SELECT * FROM place WHERE id = :placeId")
    abstract suspend fun getPlace(placeId: String): PlaceWithArea?
}
