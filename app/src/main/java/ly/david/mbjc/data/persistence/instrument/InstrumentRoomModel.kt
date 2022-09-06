package ly.david.mbjc.data.persistence.instrument

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.mbjc.data.Instrument
import ly.david.mbjc.data.network.InstrumentMusicBrainzModel
import ly.david.mbjc.data.persistence.RoomModel

@Entity(tableName = "instruments")
internal data class InstrumentRoomModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override val id: String,

    @ColumnInfo(name = "name")
    override val name: String,

    @ColumnInfo(name = "disambiguation")
    override val disambiguation: String?,

    @ColumnInfo(name = "type")
    override val type: String?,

    @ColumnInfo(name = "description")
    override val description: String?,
) : RoomModel, Instrument

internal fun InstrumentMusicBrainzModel.toInstrumentRoomModel() =
    InstrumentRoomModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        description = description
    )
