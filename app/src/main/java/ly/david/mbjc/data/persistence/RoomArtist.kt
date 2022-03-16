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

    /**
     * The total number of release groups this artist has in Music Brainz's database.
     *
     * We track this number so that we know whether or not we've collected them all in our local database.
     *
     * When not set, it means we have not queried for the number of release groups by this artist.
     * Some artists may have 0 release groups, so 0 is considered set.
     */
    @ColumnInfo(name = "release_group_count")
    val releaseGroupsCount: Int? = null

) : RoomData(), Artist

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
