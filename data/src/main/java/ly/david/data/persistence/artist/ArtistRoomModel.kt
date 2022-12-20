package ly.david.data.persistence.artist

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.data.Artist
import ly.david.data.LifeSpan
import ly.david.data.network.ArtistMusicBrainzModel
import ly.david.data.persistence.RoomModel

@Entity(
    tableName = "artist"
)
data class ArtistRoomModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override val id: String,

    @ColumnInfo(name = "name")
    override val name: String = "",
    @ColumnInfo(name = "sort_name")
    override val sortName: String = "",
    @ColumnInfo(name = "disambiguation")
    override val disambiguation: String? = null,

    @ColumnInfo(name = "type")
    override val type: String? = null,
//    @ColumnInfo(name = "type-id")
//    val typeId: String? = null,

    @ColumnInfo(name = "gender")
    override val gender: String? = null,
//    @ColumnInfo(name = "gender-id")
//    val genderId: String? = null,

    @ColumnInfo(name = "country_code")
    override val countryCode: String? = null,

    // Allow nested fields to be part of this Room table. Good for data that doesn't require its own table.
    @Embedded
    override val lifeSpan: LifeSpan? = null,
) : RoomModel, Artist

fun ArtistMusicBrainzModel.toArtistRoomModel() = ArtistRoomModel(
    id = id,
    name = name,
    sortName = sortName,
    disambiguation = disambiguation,
    type = type,
    gender = gender,
    countryCode = countryCode,
    lifeSpan = lifeSpan
)
