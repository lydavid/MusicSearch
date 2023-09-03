package ly.david.data.room.series

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.data.core.Series
import ly.david.data.musicbrainz.SeriesMusicBrainzModel
import ly.david.data.room.RoomModel

@Entity(tableName = "series")
data class SeriesRoomModel(
    @PrimaryKey @ColumnInfo(name = "id") override val id: String,
    @ColumnInfo(name = "name") override val name: String,
    @ColumnInfo(name = "disambiguation") override val disambiguation: String?,
    @ColumnInfo(name = "type") override val type: String?,
    @ColumnInfo(name = "type_id") val typeId: String?,
) : RoomModel, Series

fun SeriesMusicBrainzModel.toSeriesRoomModel() =
    SeriesRoomModel(
        id = id,
        name = name,
        disambiguation = disambiguation,
        type = type,
        typeId = typeId
    )
