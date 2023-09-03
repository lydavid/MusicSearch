package ly.david.data.room.instrument

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.data.core.Instrument
import ly.david.data.musicbrainz.InstrumentMusicBrainzModel
import ly.david.data.room.RoomModel

@Entity(tableName = "instrument")
data class InstrumentRoomModel(
    @PrimaryKey @ColumnInfo(name = "id") override val id: String,
    @ColumnInfo(name = "name") override val name: String,
    @ColumnInfo(name = "disambiguation") override val disambiguation: String?,
    @ColumnInfo(name = "description") override val description: String?,
    @ColumnInfo(name = "type") override val type: String?,
    @ColumnInfo(name = "type_id") val typeId: String? = null,
) : RoomModel, Instrument

fun InstrumentMusicBrainzModel.toInstrumentRoomModel() =
    InstrumentRoomModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        typeId = typeId,
        description = description
    )
