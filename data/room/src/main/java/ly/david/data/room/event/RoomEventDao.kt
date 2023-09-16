package ly.david.data.room.event

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import ly.david.data.room.BaseDao

@Dao
abstract class RoomEventDao : BaseDao<EventRoomModel>() {

    @Transaction
    @Query("SELECT * FROM event WHERE id = :eventId")
    abstract suspend fun getEvent(eventId: String): EventWithAllData?
}
