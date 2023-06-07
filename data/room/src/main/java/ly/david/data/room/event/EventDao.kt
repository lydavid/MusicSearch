package ly.david.data.room.event

import androidx.room.Dao
import androidx.room.Query
import ly.david.data.room.BaseDao
import ly.david.data.room.event.EventRoomModel

@Dao
abstract class EventDao : BaseDao<EventRoomModel>() {

    @Query("SELECT * FROM event WHERE id = :eventId")
    abstract suspend fun getEvent(eventId: String): EventRoomModel?
}
