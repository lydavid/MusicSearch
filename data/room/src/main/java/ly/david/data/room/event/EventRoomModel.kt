package ly.david.data.room.event

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.data.core.Event
import ly.david.data.room.common.LifeSpanRoomModel
import ly.david.data.musicbrainz.EventMusicBrainzModel
import ly.david.data.room.RoomModel
import ly.david.data.room.common.toLifeSpanRoomModel

@Entity(tableName = "event")
data class EventRoomModel(
    @PrimaryKey @ColumnInfo(name = "id") override val id: String,
    @ColumnInfo(name = "name") override val name: String,
    @ColumnInfo(name = "disambiguation") override val disambiguation: String?,
    @ColumnInfo(name = "type") override val type: String?,
    @ColumnInfo(name = "type_id") val typeId: String? = null,
    @ColumnInfo(name = "time") override val time: String? = null,
    @ColumnInfo(name = "cancelled") override val cancelled: Boolean? = null,
    @Embedded override val lifeSpan: LifeSpanRoomModel?,
) : RoomModel, Event

fun EventMusicBrainzModel.toEventRoomModel() =
    EventRoomModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        typeId = typeId,
        time = time,
        cancelled = cancelled,
        lifeSpan = lifeSpan?.toLifeSpanRoomModel(),
    )
