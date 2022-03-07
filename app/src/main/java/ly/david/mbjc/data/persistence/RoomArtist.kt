package ly.david.mbjc.data.persistence

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ly.david.mbjc.data.Artist
import ly.david.mbjc.data.LifeSpan
import ly.david.mbjc.data.network.MusicBrainzArtist

@Entity(
    tableName = "artists"
)
data class RoomArtist(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override val id: String,

    @ColumnInfo(name = "name")
    override val name: String = "",
    @ColumnInfo(name = "sort-name")
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

    @ColumnInfo(name = "country")
    override val country: String? = null,

    // Allow nested fields to be part of this Room table. Good for data that doesn't require its own table.
    @Embedded
    override val lifeSpan: LifeSpan? = null,

    // TODO: include number of release groups once available

): Artist

fun MusicBrainzArtist.toRoomArtist() =
    RoomArtist(
        id = id,
        name = name,
        sortName = sortName,
        disambiguation = disambiguation,
        type = type,
        gender = gender,
        country = country,
        lifeSpan = lifeSpan
    )
