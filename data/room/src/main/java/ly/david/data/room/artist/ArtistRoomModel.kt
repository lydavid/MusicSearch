package ly.david.data.room.artist

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.data.core.Artist
import ly.david.data.core.LifeSpanRoomModel
import ly.david.data.musicbrainz.ArtistMusicBrainzModel
import ly.david.data.room.RoomModel
import ly.david.data.core.toLifeSpanRoomModel

@Entity(
    tableName = "artist"
)
data class ArtistRoomModel(
    @PrimaryKey @ColumnInfo(name = "id") override val id: String,
    @ColumnInfo(name = "name") override val name: String = "",
    @ColumnInfo(name = "sort_name") override val sortName: String = "",
    @ColumnInfo(name = "disambiguation") override val disambiguation: String? = null,
    @ColumnInfo(name = "type") override val type: String? = null,
    @ColumnInfo(name = "type_id") val typeId: String? = null,
    @ColumnInfo(name = "gender") override val gender: String? = null,
    @ColumnInfo(name = "country_code") override val countryCode: String? = null,
    @Embedded override val lifeSpan: LifeSpanRoomModel? = null,
) : RoomModel, Artist

fun ArtistMusicBrainzModel.toArtistRoomModel() = ArtistRoomModel(
    id = id,
    name = name,
    sortName = sortName,
    disambiguation = disambiguation,
    type = type,
    typeId = typeId,
    gender = gender,
    countryCode = countryCode,
    lifeSpan = lifeSpan?.toLifeSpanRoomModel(),
)
