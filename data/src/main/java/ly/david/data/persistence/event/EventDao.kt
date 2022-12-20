package ly.david.data.persistence.event

import androidx.room.Dao
import androidx.room.Query
import ly.david.data.persistence.BaseDao

@Dao
abstract class EventDao : BaseDao<EventRoomModel>() {

    @Query("SELECT * FROM event WHERE id = :eventId")
    abstract suspend fun getEvent(eventId: String): EventRoomModel?
}
