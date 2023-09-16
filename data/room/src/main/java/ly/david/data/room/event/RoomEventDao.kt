package ly.david.data.room.event

import androidx.room.Dao
import ly.david.data.room.BaseDao

@Dao
abstract class RoomEventDao : BaseDao<EventRoomModel>()
