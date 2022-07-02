package ly.david.mbjc.data.persistence.place

import androidx.room.Dao
import androidx.room.Query
import ly.david.mbjc.data.persistence.BaseDao
import ly.david.mbjc.data.persistence.PlaceRoomModel

@Dao
internal abstract class PlaceDao : BaseDao<PlaceRoomModel> {

    @Query("SELECT * FROM places WHERE id = :placeId")
    abstract suspend fun getPlace(placeId: String): PlaceRoomModel?
}
