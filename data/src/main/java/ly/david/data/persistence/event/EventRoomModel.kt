package ly.david.data.persistence.event

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.data.Event
import ly.david.data.LifeSpan
import ly.david.data.network.EventMusicBrainzModel
import ly.david.data.persistence.RoomModel

@Entity(tableName = "events")
data class EventRoomModel(
    @PrimaryKey @ColumnInfo(name = "id") override val id: String,
    @ColumnInfo(name = "name") override val name: String,
    @ColumnInfo(name = "disambiguation") override val disambiguation: String?,
    @ColumnInfo(name = "type") override val type: String?,
    @ColumnInfo(name = "time") override val time: String? = null,
    @ColumnInfo(name = "cancelled") override val cancelled: Boolean? = null,
    @Embedded override val lifeSpan: LifeSpan?,
) : RoomModel, Event

fun EventMusicBrainzModel.toEventRoomModel() =
    EventRoomModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        time = time,
        cancelled = cancelled,
        lifeSpan = lifeSpan,
    )
