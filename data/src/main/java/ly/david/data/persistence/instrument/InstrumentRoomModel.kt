package ly.david.data.persistence.instrument

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.data.Instrument
import ly.david.data.network.InstrumentMusicBrainzModel
import ly.david.data.persistence.RoomModel

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
