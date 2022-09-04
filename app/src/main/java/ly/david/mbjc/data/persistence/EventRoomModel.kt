package ly.david.mbjc.data.persistence

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.mbjc.data.Event
import ly.david.mbjc.data.LifeSpan
import ly.david.mbjc.data.network.EventMusicBrainzModel

@Entity(tableName = "events")
internal data class EventRoomModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override val id: String,

    @ColumnInfo(name = "name")
    override val name: String,

    @ColumnInfo(name = "disambiguation")
    override val disambiguation: String?,

    @ColumnInfo(name = "type")
    override val type: String?,

    @Embedded
    override val lifeSpan: LifeSpan?,
) : RoomModel(), Event

internal fun EventMusicBrainzModel.toEventRoomModel() =
    EventRoomModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        lifeSpan = lifeSpan,
    )
