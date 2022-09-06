package ly.david.mbjc.data.persistence.event

import androidx.room.Dao
import androidx.room.Query
import ly.david.mbjc.data.persistence.BaseDao

@Dao
internal abstract class EventDao : BaseDao<EventRoomModel> {

    @Query("SELECT * FROM events WHERE id = :eventId")
    abstract suspend fun getEvent(eventId: String): EventRoomModel?
}
